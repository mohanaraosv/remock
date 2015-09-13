package no.saua.remock;

import org.springframework.test.context.BootstrapWith;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface RemockWebAppTest {

    /**
     * The resource path to the root directory of the web application.
     * <p/>
     * <p>A path that does not include a Spring resource prefix (e.g., {@code classpath:},
     * {@code file:}, etc.) will be interpreted as a file system resource, and a
     * path should not end with a slash.
     * <p/>
     * <p>Defaults to {@code "src/main/webapp"} as a file system resource. Note
     * that this is the standard directory for the root of a web application in
     * a project that follows the standard Maven project layout for a WAR.
     */
    String value() default "src/main/webapp";

}
