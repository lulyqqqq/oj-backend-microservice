package com.ac.ojbackendjudgeservice.judge.codesandbox;


import com.ac.model.codesandbox.ExecuteCodeRepose;
import com.ac.model.codesandbox.ExecuteCodeRequest;

/**
 * 代码沙箱 运行代码获得返回值
 */
public interface CodeSandBox {

    ExecuteCodeRepose executeCode(ExecuteCodeRequest executeCodeRequest);
}
