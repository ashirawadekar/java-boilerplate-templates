package com.template.resttemplate;

import org.springframework.web.client.RestTemplate;

public class RestTemplateSupplier {

    private static RestTemplate restTemplate;

    static {
        restTemplate = new RestTemplate();
    }

    public static RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
