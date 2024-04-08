package com.patryklikus.publicchat.httpUtils.Request.mapping;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface GetMapping {
    String path() default "";
}
