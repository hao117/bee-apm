package net.beeapm.demo.service.test;

import org.apache.commons.lang3.RandomUtils;

public class FooEee {
    public void say(){
        System.out.println("-------------------->FooEee::say");
        say2();
    }
    public void say2(){
        System.out.println("-------------------->FooEee::say2");
        say3();
    }
    public void say3(){
        System.out.println("-------------------->FooEee::say3");
        say4();
    }
    public void say4(){
        System.out.println("-------------------->FooEee::say4");
        say5();
    }
    public void say5(){
        System.out.println("-------------------->FooEee::say5");
        say6();
    }
    public void say6(){
        System.out.println("-------------------->FooEee::say6");
        say7();
        try{
            Thread.sleep(RandomUtils.nextInt(1,1000));
        }catch (Exception e){

        }
    }
    public void say7(){
        System.out.println("-------------------->FooEee::say7");
        try{
            Thread.sleep(RandomUtils.nextInt(1,500));
        }catch (Exception e){

        }
        say8();
    }
    public void say8(){
        System.out.println("-------------------->FooEee::say8");
        say9();
    }
    public void say9(){
        System.out.println("-------------------->FooEee::say9");
        say10();
    }
    public void say10(){
        System.out.println("-------------------->FooEee::say10");
        say11();
    }
    public void say11(){
        System.out.println("-------------------->FooEee::say11");
        say12();
    }
    public void say12(){
        System.out.println("-------------------->FooEee::say12");
        say13();
    }
    public void say13(){
        System.out.println("-------------------->FooEee::say13");
        say14();
    }
    public void say14(){
        System.out.println("-------------------->FooEee::say14");
        say15();
    }
    public void say15(){
        System.out.println("-------------------->FooEee::say15");
        say16();
    }
    public void say16(){
        System.out.println("-------------------->FooEee::say16");
        say17();
    }
    public void say17(){
        System.out.println("-------------------->FooEee::say17");
        say18();
    }
    public void say18(){
        System.out.println("-------------------->FooEee::say18");
        say19();
    }
    public void say19(){
        System.out.println("-------------------->FooEee::say19");
        say20();
    }
    public void say20(){
        System.out.println("-------------------->FooEee::say20");
    }

/**
    public static void main(String[] args) {
        for(int i = 5; i < 21; i++){
            System.out.println("    public void say"+i+"(){");
            System.out.println("        System.out.println(\"-------------------->FooEee::say"+i+"\");");
            System.out.println("        say"+(i+1)+"();");
            System.out.println("    }");
        }
    }
**/
}
