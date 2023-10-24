package com.ac.ojbackendjudgeservice.judge;



import com.ac.model.codesandbox.JudgeInfo;
import com.ac.model.entity.QuestionSubmit;
import com.ac.ojbackendjudgeservice.judge.strategy.DefaultJudgeStrategy;
import com.ac.ojbackendjudgeservice.judge.strategy.JavaLanguageJudgeStrategy;
import com.ac.ojbackendjudgeservice.judge.strategy.JudgeContext;
import com.ac.ojbackendjudgeservice.judge.strategy.JudgeStrategy;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 * @author 02
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if ("java".equals(language)) {
            judgeStrategy = new JavaLanguageJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }

}
