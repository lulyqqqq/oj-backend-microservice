package com.ac.ojbackendjudgeservice.judge.codesandbox.impl;

import com.ac.model.codesandbox.ExecuteCodeRepose;
import com.ac.model.codesandbox.ExecuteCodeRequest;
import com.ac.ojbackendjudgeservice.judge.codesandbox.CodeSandBox;


/**
 * @ClassName: 调用第三方的代码沙箱
 * @author: mafangnian
 * @date: 2023/10/15 23:32
 */
public class ThirdPartyCodeSandBox implements CodeSandBox {
    @Override
    public ExecuteCodeRepose executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("ThirdPartyCodeSandBox 第三方代码沙箱");
        return null;
    }
}
