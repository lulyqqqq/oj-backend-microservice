package com.ac.ojbackendjudgeservice.judge;


import com.ac.model.entity.QuestionSubmit;

/**
 * 判题服务
 * @author da zhou
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(long questionSubmitId);
}
