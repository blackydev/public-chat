/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.https.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface PutMapping {
    String path() default "/";
}
