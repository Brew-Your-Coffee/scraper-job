package com.coffee.constants;

import com.coffee.domain.ErrorDetails;
import org.springframework.http.HttpStatus;

public class ErrorConstants {
    public static final ErrorDetails ES_INDEX_ERROR = new ErrorDetails("SJERR-001", "Exception during indexing coffee entities to ElasticSearch", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorDetails CAFE_BOUTIQUE_SCRAPER_ERROR = new ErrorDetails("SJERR-002", "Exception during scraping coffee entities from CafeBoutique", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorDetails MAD_HEADS_SCRAPER_ERROR = new ErrorDetails("SJERR-003", "Exception during scraping coffee entities from Mad Heads", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorDetails FRESH_BLACK_SCRAPER_ERROR = new ErrorDetails("SJERR-004", "Exception during scraping coffee entities from Fresh Black", HttpStatus.INTERNAL_SERVER_ERROR);
    public static final ErrorDetails ES_UPDATE_DISABLED_ERROR = new ErrorDetails("SJERR-005", "Exception during updating disabled coffee entities in ElasticSearch", HttpStatus.INTERNAL_SERVER_ERROR);

    private ErrorConstants(){}
}
