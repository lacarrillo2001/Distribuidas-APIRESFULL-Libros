package com.espe.test.test.controllers;


import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
public class IndexController {
    @GetMapping("/authorized")
    public Map<String, String> authorized(
            @RegisteredOAuth2AuthorizedClient("libro-ms") OAuth2AuthorizedClient client
    ) {
        return Map.of(
                "message", "Login Exitoso",
                "access_token", client.getAccessToken().getTokenValue()
        );
    }
}
