package com.coffee.scraper;

import com.coffee.domain.CoffeeDto;
import com.coffee.exception.ScraperException;

import java.util.List;

public interface Scraper {
    List<CoffeeDto> scrape() throws ScraperException;
}
