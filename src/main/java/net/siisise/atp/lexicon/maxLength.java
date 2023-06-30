package net.siisise.atp.lexicon;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import java.lang.annotation.Target;

/**
 *
 * ATPの方へ入れればいい?
 */
@java.lang.annotation.Retention(RUNTIME)
@Target({FIELD, PARAMETER})
public @interface maxLength {

    int value();
}
