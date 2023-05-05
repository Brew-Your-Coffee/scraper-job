package com.coffee.scraper;

import com.coffee.domain.CoffeeDto;

import java.util.List;

public interface Scraper {
    List<CoffeeDto> scrape();
}
