package net.beeapm.agent.boot;

/**
 * @author yuan
 * @date 2018-11-07
 */
public abstract class AbstractBootPlugin implements IBootPlugin{
    private String name;
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }
}
