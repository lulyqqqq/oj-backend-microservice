package com.ac.ojbackendjudgeservice.judge.strategy;

import com.ac.model.codesandbox.JudgeInfo;
import com.ac.model.dto.question.JudgeCase;
import com.ac.model.entity.Question;
import com.ac.model.entity.QuestionSubmit;
import lombok.Data;

import java.util.List;

/**
 * @ClassName: JudgeContent
 * @author: mafangnian
 * @date: 2023/10/16 13:21
 */
@Data
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private Question question;

    private QuestionSubmit questionSubmit;
}
