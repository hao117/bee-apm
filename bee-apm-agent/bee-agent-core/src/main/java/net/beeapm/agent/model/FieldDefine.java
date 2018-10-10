package net.beeapm.agent.model;

import net.bytebuddy.description.modifier.ModifierContributor;

import java.lang.reflect.Type;

/**
 * 用于增加类属性
 */
public class FieldDefine {
    public String name;
    public Type type;
    public ModifierContributor.ForField[] modifiers;

    /**
     *
     * @param name
     * @param type
     * @param modifiers Visibility.PUBLIC, Ownership.STATIC, FieldManifestation.FINAL
     */
    public FieldDefine(String name, Type type, ModifierContributor.ForField[] modifiers) {
        this.name = name;
        this.type = type;
        this.modifiers = modifiers;
    }
}
