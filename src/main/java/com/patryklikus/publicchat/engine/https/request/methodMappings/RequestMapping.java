/* Copyright Patryk Likus All Rights Reserved. */
package com.patryklikus.publicchat.engine.https.request.methodMappings;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface RequestMapping {
    String path() default "";
}
