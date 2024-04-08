package com.patryklikus.publicchat.controller;

import com.patryklikus.publicchat.httpUtils.Request.mapping.GetMapping;
import com.patryklikus.publicchat.httpUtils.Request.mapping.RequestMapping;
import com.patryklikus.publicchat.httpUtils.Response.Response;
import com.patryklikus.publicchat.httpUtils.Response.ResponseStatusCode;
import com.sun.net.httpserver.Request;

@RequestMapping(path = "/tests")
public class TestController {

    @GetMapping(path = "/get")
    public Response get(Request request) {
        return new Response(ResponseStatusCode.OK, "Hello guyssss");
    }
}
