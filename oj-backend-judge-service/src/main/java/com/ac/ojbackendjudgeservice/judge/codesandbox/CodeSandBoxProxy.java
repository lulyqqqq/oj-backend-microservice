package com.ac.ojbackendjudgeservice.judge.codesandbox;


import com.ac.model.codesandbox.ExecuteCodeRepose;
import com.ac.model.codesandbox.ExecuteCodeRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: CodeSandBoxProxy
 * 代理访问
 * 增强代码沙箱能力 添加日志能力
 * @author: mafangnian
 * @date: 2023/10/16 11:36
 */
@Slf4j
@AllArgsConstructor
public class CodeSandBoxProxy implements CodeSandBox {

    private final CodeSandBox codeSandBox;

    @Override
    public ExecuteCodeRepose executeCode(ExecuteCodeRequest executeCodeRequest) {
        log.info("代码沙箱请求前信息: " + executeCodeRequest.toString());
        ExecuteCodeRepose executeCodeRepose = codeSandBox.executeCode(executeCodeRequest);
        log.info("代码沙箱请求后信息: " + executeCodeRepose.toString());
        return executeCodeRepose;
    }
}
