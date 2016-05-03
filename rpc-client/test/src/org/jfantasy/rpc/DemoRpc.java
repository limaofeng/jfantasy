package org.jfantasy.rpc;

import org.jfantasy.rpc.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DemoRpc implements CommandLineRunner {

    @Autowired
    HelloService helloService;

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(helloService.toString());
        System.out.println(helloService.say("乔峰"));
        HelloService.RPCTestBean testBean = new HelloService.RPCTestBean();
        testBean.setName("花子虚");
        testBean.setAge(20);
        System.out.println(helloService.say(testBean));
        testBean = helloService.say("李沧海", 30);
        System.out.println(testBean.getName() + " - " + testBean.getAge());
//        HelloService.Hello hello = helloService.say("余秋水", 30,0);
//        System.out.println(hello.getName() + " - " + hello.getAge());
    }
}
