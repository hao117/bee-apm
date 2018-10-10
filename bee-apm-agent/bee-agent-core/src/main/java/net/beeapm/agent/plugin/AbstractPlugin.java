package net.beeapm.agent.plugin;

import net.beeapm.agent.model.FieldDefine;

public abstract class AbstractPlugin implements IPlugin {
    public FieldDefine[] buildFieldDefine(){
        return null;
    }
}
