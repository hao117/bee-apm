package net.beeapm.demo.service.test;

public class FooCcc {
    public void say() {
        try {
            System.out.println("-------------------->FooCcc::say");
            tell();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void tell() throws Exception {
        System.out.println("-------------------->FooCcc::tell");
        speak();
    }

    public void speak() throws Exception {
        System.out.println("-------------------->FooCcc::speak");
        talk();
    }

    public void talk() throws Exception {
        System.out.println("-------------------->FooCcc::talk");
        throw new Exception("oh my god!");
    }
}
