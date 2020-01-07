package net.beeapm.demo.service.test;

public class FooBbb {
    public void say(){
        System.out.println("-------------------->FooBbb::say");
        tell();
    }
    public void tell(){
        System.out.println("-------------------->FooBbb::tell");
        speak();
    }
    public void speak(){
        System.out.println("-------------------->FooBbb::speak");
        talk();
    }
    public void talk(){
        System.out.println("-------------------->FooBbb::talk");
    }
}
