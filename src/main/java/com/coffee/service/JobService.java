package com.coffee.service;

import com.coffee.client.RecommendationServiceClient;
import com.coffee.domain.CoffeeDto;
import com.coffee.exception.ElasticSearchException;
import com.coffee.scraper.Scraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.coffee.constants.ErrorConstants.ES_INDEX_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class JobService {
    private final List<Scraper> scrapers;
    private final ElasticSearchService elasticSearchService;
    private final RecommendationServiceClient recommendationServiceClient;

    public void run() {
        for (Scraper scraper : scrapers) {
            List<CoffeeDto> coffeeDtoList = scraper.scrape();
            try {
                elasticSearchService.index(coffeeDtoList);
            } catch (IOException e) {
                log.error("Failed to index the butch coffee entities", e);
                throw new ElasticSearchException(ES_INDEX_ERROR);
            }
        }

        recommendationServiceClient.resetRecommendations();
    }
}
