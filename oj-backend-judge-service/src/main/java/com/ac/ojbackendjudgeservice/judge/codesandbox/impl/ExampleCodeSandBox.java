package com.ac.ojbackendjudgeservice.judge.codesandbox.impl;

import com.ac.model.codesandbox.ExecuteCodeRepose;
import com.ac.model.codesandbox.ExecuteCodeRequest;
import com.ac.model.codesandbox.JudgeInfo;
import com.ac.model.enums.JudgeInfoMessageEnum;
import com.ac.model.enums.StatusEnum;
import com.ac.ojbackendjudgeservice.judge.codesandbox.CodeSandBox;


import java.util.List;

/**
 * @ClassName: ExampleCodeSandBox
 * 仅为跑通代码流程
 * @author: mafangnian
 * @date: 2023/10/15 23:32
 */
public class ExampleCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeRepose executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();

        ExecuteCodeRepose executeCodeRepose = new ExecuteCodeRepose();
        executeCodeRepose.setOutputList(inputList);
        executeCodeRepose.setMessage("测试执行成功");
        executeCodeRepose.setStatus(StatusEnum.SUCCEED.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        executeCodeRepose.setJudgeInfo(judgeInfo);

        return executeCodeRepose;

    }
}
