package com.ac.ojbackendjudgeservice.judge.strategy;


import com.ac.model.codesandbox.JudgeInfo;

/**
 * 判断策略
 */
public interface JudgeStrategy {

    JudgeInfo doJudge(JudgeContext judgeContext);
}
