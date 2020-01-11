package net.beeapm.demo.service.test;

import org.apache.commons.lang3.RandomUtils;

public class FooCcc {
    public void say() {
        try {
            System.out.println("-------------------->FooCcc::say");
            tell();
            Thread.sleep(RandomUtils.nextInt(1,3000));
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
