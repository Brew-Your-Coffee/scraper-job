package com.coffee.scraper;

import com.coffee.domain.CoffeeDto;
import com.coffee.exception.ScraperException;
import com.coffee.parser.FreshBlackParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.coffee.constants.ErrorConstants.CAFE_BOUTIQUE_SCRAPER_ERROR;
import static com.coffee.constants.ErrorConstants.FRESH_BLACK_SCRAPER_ERROR;

@Component
@Slf4j
@RequiredArgsConstructor
public class FreshBlackScraper implements Scraper {
    private static final String BASE_URL = "https://fresh.black";

    private final FreshBlackParser parser;

    @Override
    public List<CoffeeDto> scrape() {
        List<CoffeeDto> result = new ArrayList<>();
        try {
            Document mainPage = Jsoup.connect("https://fresh.black/catalog/speshalti").get();

            // Fetch the web page
            List<Element> elements = mainPage.select(".products-list > .product-card");

            for (Element element : elements) {

                CoffeeDto coffeeDto = scrapeCoffeeInfo(element);

                if (coffeeDto != null) {
                    result.add(coffeeDto);
                }

            }
        } catch (IOException e) {
            log.error("Exception during scraping coffee entities from Fresh Black", e);
            throw new ScraperException(FRESH_BLACK_SCRAPER_ERROR);
        }

        return result;
    }

    private CoffeeDto scrapeCoffeeInfo(Element element) {
        try {
            String ref = BASE_URL + element.select("div.product-card__img > a").first().attr("href");
            Document document = Jsoup.connect(ref).get();
            return parser.parse(ref, element, document);
        } catch (IOException e) {
            log.error("Exception during scraping coffee entities from CafeBoutique", e);
            throw new ScraperException(CAFE_BOUTIQUE_SCRAPER_ERROR);
        }
    }
}
