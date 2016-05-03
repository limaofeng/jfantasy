package org.jfantasy.rpc.service;


import org.jfantasy.rpc.annotation.ServiceExporter;

@ServiceExporter(value = "demoSvr", targetInterface = HelloService.class, debugAddress = "127.0.0.1:9090")
public class HelloServiceImpl implements HelloService {

    public String say(String name) {
        return "hi:" + name;
    }

    public String say(RPCTestBean bean) {
        return "hi:" + bean.getName() + " - " + bean.getAge();
    }

    @Override
    public RPCTestBean say(String name, int age) {
        RPCTestBean testBean = new RPCTestBean();
        testBean.setName(name);
        testBean.setAge(age);
        return testBean;
    }

    @Override
    public Hello say(String name, int age, int sex) {
        return new HelloImpl(name, age, sex);
    }

    public class HelloImpl implements Hello {
        private String name;
        private int age;
        private int sex;

        public HelloImpl(String name, int age, int sex) {
            this.name = name;
            this.age = age;
            this.sex = sex;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        public int getAge() {
            return this.age;
        }

        @Override
        public int getSex() {
            return this.sex;
        }

    }

}
