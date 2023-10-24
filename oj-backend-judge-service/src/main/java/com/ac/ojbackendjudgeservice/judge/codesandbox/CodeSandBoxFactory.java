package com.ac.ojbackendjudgeservice.judge.codesandbox;


import com.ac.ojbackendjudgeservice.judge.codesandbox.impl.ExampleCodeSandBox;
import com.ac.ojbackendjudgeservice.judge.codesandbox.impl.RemoteCodeSandBox;
import com.ac.ojbackendjudgeservice.judge.codesandbox.impl.ThirdPartyCodeSandBox;

/**
 * 代码沙箱工厂 根据传入的字符串参数来指定代码沙箱实例
 */
public class CodeSandBoxFactory {


    /**
     * 创建沙箱实例 单例模式
     * 项目中的沙箱类型在配置文件中进行修改
     * @param type 沙箱类型
     * @return
     */
    public static CodeSandBox newInstance(String type) {
        switch (type){
            case "remote":
                return new RemoteCodeSandBox();
            case "thirdParty":
                return new ThirdPartyCodeSandBox();
            case "example":
            default:
                return new ExampleCodeSandBox();
        }
    }
}
