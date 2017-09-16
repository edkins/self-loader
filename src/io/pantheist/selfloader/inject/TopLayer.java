package io.pantheist.selfloader.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the given class is the top layer of the application.
 *
 * There must be only one of these, and it gets created at the start, along
 * with all the dependencies.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface TopLayer
{
}
