package net.beeapm.agent.plugin;

import net.beeapm.agent.model.FieldDefine;

/**
 * @author yuan
 * @date 2018-10-09
 */
public abstract class AbstractPlugin implements IPlugin {
    private String name;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public FieldDefine[] buildFieldDefine() {
        return null;
    }

    public int order() {
        return Integer.MAX_VALUE;
    }

    ;
}
