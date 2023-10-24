package com.ac.ojbackendjudgeservice.judge.impl;

import cn.hutool.json.JSONUtil;
import com.ac.model.codesandbox.ExecuteCodeRepose;
import com.ac.model.codesandbox.ExecuteCodeRequest;
import com.ac.model.codesandbox.JudgeInfo;
import com.ac.model.dto.question.JudgeCase;
import com.ac.model.entity.Question;
import com.ac.model.entity.QuestionSubmit;
import com.ac.model.enums.StatusEnum;
import com.ac.ojbackendcommon.common.ErrorCode;
import com.ac.ojbackendcommon.exception.BusinessException;
import com.ac.ojbackendjudgeservice.judge.JudgeManager;
import com.ac.ojbackendjudgeservice.judge.JudgeService;
import com.ac.ojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import com.ac.ojbackendjudgeservice.judge.codesandbox.CodeSandBoxFactory;
import com.ac.ojbackendjudgeservice.judge.codesandbox.CodeSandBoxProxy;
import com.ac.ojbackendjudgeservice.judge.strategy.JudgeContext;
import com.ac.ojbackendserviceclient.service.QuestionFeignClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName: JudgeServiceImpl
 * @author: mafangnian
 * @date: 2023/10/16 12:03
 */
@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionFeignClient questionFeignClient;
    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:example}")
    private String type;

    @Override
    public QuestionSubmit doJudge(long questionSubmitId) {

        // 1.传入题目的提交id,获取题目的对应信息
        QuestionSubmit questionSubmit = questionFeignClient.getQuestionSubmitById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "提交信息不存在");
        }
        Long questionId = questionSubmit.getQuestionId();
        Question question = questionFeignClient.getQuestionById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }
        // 1.1 判断题目的状态是否为等待中,如果不是则不需要进行判题,如果是则更改题目的状态为判题中,防止重复进行
        if (!questionSubmit.getStatus().equals(StatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "题目正在判题中");
        }
        // 1.2 更改判题（题目提交）的状态为 “判题中”，防止重复执行
        QuestionSubmit questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        questionSubmitUpdate.setStatus(StatusEnum.RUNNING.getValue());
        boolean update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }

        // 2.调用沙箱代码,获得执行结果
        CodeSandBox codeSandBox = CodeSandBoxFactory.newInstance(type);
        codeSandBox = new CodeSandBoxProxy(codeSandBox);
        String language = questionSubmit.getLanguage();
        String code = questionSubmit.getCode();

        // 获取输入用例
        String judgeCaseStr = question.getJudgeCase();
        List<JudgeCase> judgeCaseList = JSONUtil.toList(judgeCaseStr, JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).collect(Collectors.toList());
        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .code(code)
                .language(language)
                .inputList(inputList)
                .build();
        // 3.根据沙箱的执行结果,设置题目的判题状态和信息
        ExecuteCodeRepose executeCodeRepose = codeSandBox.executeCode(executeCodeRequest);

        List<String> outputList = executeCodeRepose.getOutputList();
        List<String> updateOutputList = new ArrayList<>();
        //截取字符\n
        if (outputList!=null) {
            for (String s : outputList) {
                if (s != null) {
                    String sxs = s.replaceAll("\\n", "");
                    updateOutputList.add(sxs);
                }
            }
        }
        // 4.根据沙箱的执行结果，设置题目的判题状态和信息
        JudgeContext judgeContext = new JudgeContext();
        //沙箱代码执行后的内存，数据，信息
        judgeContext.setJudgeInfo(executeCodeRepose.getJudgeInfo());
        //设置的是预期输入
        judgeContext.setInputList(inputList);
        //设置的是沙箱代码输出
        judgeContext.setOutputList(updateOutputList);
        //传入的的是预期JudgeCase,里面有预期输出和输入。
        judgeContext.setJudgeCaseList(judgeCaseList);
        judgeContext.setQuestion(question);
        judgeContext.setQuestionSubmit(questionSubmit);
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        // 5.修改数据库中的判题结果
        questionSubmitUpdate = new QuestionSubmit();
        questionSubmitUpdate.setId(questionSubmitId);
        Integer acceptedNum = question.getAcceptedNum();
        Integer submitNum = question.getSubmitNum();
        //出错设置状态为错误
        String message = judgeInfo.getMessage();
        if (!"成功".equals(message)){
            //总数+1
            submitNum=submitNum+1;
            question.setSubmitNum(submitNum);
            questionFeignClient.updateQuestion(question);
            questionSubmitUpdate.setStatus(StatusEnum.FAILED.getValue());
        }else {

            //通过数+1
            acceptedNum=acceptedNum+1;
            question.setAcceptedNum(acceptedNum);
            //总数+1
            submitNum=submitNum+1;
            question.setSubmitNum(submitNum);
            questionFeignClient.updateQuestion(question);
            questionSubmitUpdate.setStatus(StatusEnum.SUCCEED.getValue());
        }
        questionSubmitUpdate.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        update = questionFeignClient.updateQuestionSubmitById(questionSubmitUpdate);
        if (!update) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "题目状态更新错误");
        }
        QuestionSubmit questionSubmitResult = questionFeignClient.getQuestionSubmitById(questionId);
        return questionSubmitResult;

    }
}
