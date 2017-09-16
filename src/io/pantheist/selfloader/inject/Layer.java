package io.pantheist.selfloader.inject;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that the given class may be injected into
 * layer classes (including the top layer), and may have other
 * layers injected into it.
 *
 * Singleton binding is used, so only one instance of each
 * layer class will be created by the injector.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Layer
{

}
