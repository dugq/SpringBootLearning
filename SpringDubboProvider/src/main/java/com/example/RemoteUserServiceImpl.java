package com.example;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class RemoteUserServiceImpl implements RemoteUserService {

    @Override
    public String sayHello(String name) {
        return "my name is hello! hello.";
    }


}
