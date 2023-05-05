package com.coffee.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "RECOMMENDATION-SERVICE", url = "http://localhost:8000")
public interface RecommendationServiceClient {
    @PostMapping(value = "/reset/")
    void resetRecommendations();
}
