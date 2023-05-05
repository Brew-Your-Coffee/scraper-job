package com.coffee.domain;

import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class CoffeeDto {
    private String id;
    private String url;
    private String imageUrl;
    private String source;
    private String title;
    private Integer price;
    private String description;
    private String roast;
    private Set<String> tastes;
    private Set<String> recommendedBrewMethods;
    private String country;
    private String processingMethod;
    private String variety;
    private String score;
    private boolean enabled;
    private Date lastUpdated;
}
