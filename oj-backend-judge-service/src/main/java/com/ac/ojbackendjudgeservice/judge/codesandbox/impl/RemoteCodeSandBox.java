package com.ac.ojbackendjudgeservice.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;

import com.ac.model.codesandbox.ExecuteCodeRepose;
import com.ac.model.codesandbox.ExecuteCodeRequest;
import com.ac.ojbackendcommon.common.ErrorCode;
import com.ac.ojbackendcommon.exception.BusinessException;
import com.ac.ojbackendjudgeservice.judge.codesandbox.CodeSandBox;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName: 远程调用的代码沙箱
 * @author: mafangnian
 * @date: 2023/10/15 23:32
 */
public class RemoteCodeSandBox implements CodeSandBox {
    public static final String AUTH_REQUEST_HEADER = "auth";
    public static final String AUTH_REQUEST_SECRET = "secretKey";
    @Override
    public ExecuteCodeRepose executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("RemoteCodeSandBox 远程代码沙箱");
        String url = "http://localhost:8011/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .body(json)
                .header(AUTH_REQUEST_HEADER,AUTH_REQUEST_SECRET)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "execute remoteSandBox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeRepose.class);
    }
}
