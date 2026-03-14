package it.garganovolpe.weendtray.logging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/* 
 * An annotation for injecting a logger provider based on the environment variable.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GetLoggerProviderFromEnv {
    String defaultType() default "COMBINED";
}
