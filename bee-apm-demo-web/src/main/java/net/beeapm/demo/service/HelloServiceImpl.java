package net.beeapm.demo.service;

/**
 * Created by yuan on 2018/8/2.
 */
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String context) {
        System.out.println("     ============HelloServiceImpl.sayHello:"+context);
        testA();
        testB();
        return context;
    }

    public void testA(){
        testA1();
        testA2();
    }

    public void testA1(){

    }

    public void testA2(){

    }

    public void testB(){
        testB1();
        testB2();
    }

    public void testB1(){
        testB1_1();
    }

    public void testB1_1(){

    }
    public void testB2(){

    }
}
