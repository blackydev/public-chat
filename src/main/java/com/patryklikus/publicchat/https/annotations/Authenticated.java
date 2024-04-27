/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Controllers' methods with this annotation required being authorized.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Authenticated {
    boolean admin() default false;
}
