package net.beeapm.demo.service;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yuan on 2018/8/2.
 */
public class HelloServiceImpl implements IHelloService {
    @Override
    public String sayHello(String context) {
        try {
            System.out.println("     ============HelloServiceImpl.sayHello:" + context);
            testA("a", "b");
            //testB(new FileInputStream(new File("C:\\Users\\yuan\\Desktop\\service.txt")));
        }catch (Exception e){
            e.printStackTrace();
        }
        return context;
    }

    public void testA(String a,String b){
        testA1(12311L,"str111");
        List list = new ArrayList();
        list.add("list123");
        testA2(list);
    }

    public void testA1(Long num,String str){

    }

    public void testA2(List list){

    }

    public void testB(FileInputStream fis){
        try{
            fis.close();
            testB1();
            Map<String,Object> p = new HashMap<>();
            p.put("bbb1","bbb1");
            p.put("bbb2","bbb");
            testB2(p);
        }catch (Exception e){

        }
    }

    public void testB1(){
        testB1_1();
    }

    public void testB1_1(){

    }
    public void testB2(Map<String,Object> p){

    }
}
