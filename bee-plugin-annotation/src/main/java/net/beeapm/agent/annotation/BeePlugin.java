package net.beeapm.agent.annotation;


import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * @author yuan
 * @date 2019/12/19
 */
@Retention(SOURCE)
@Documented
@Target(TYPE)
public @interface BeePlugin {
    String name();
    String type();
}