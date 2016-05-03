package org.jfantasy.rpc.service;

public interface HelloService {

    String say(String name);

    String say(RPCTestBean testBean);

    RPCTestBean say(String name, int age);

    Hello say(String name, int age,int sex);


    interface Hello{

        String getName();

        int getAge();

        int getSex();

    }

    class RPCTestBean {
        private String name;
        private int age;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }
}
