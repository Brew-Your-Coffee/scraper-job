package com.coffee.scraper;

import com.coffee.domain.CoffeeDto;
import com.coffee.exception.ScraperException;
import com.coffee.parser.MadHeadsParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.coffee.constants.ErrorConstants.MAD_HEADS_SCRAPER_ERROR;

@Service
@Slf4j
@RequiredArgsConstructor
public class MadHeadsScraper implements Scraper {
    private final MadHeadsParser parser;

    @Override
    public List<CoffeeDto> scrape() {
        List<CoffeeDto> result = new ArrayList<>();

        try {
            Document mainPage = Jsoup.connect("https://madheadscoffee.com/product-category/coffee/").get();

            List<Element> elements = mainPage.select(".product");

            for (Element element : elements) {
                CoffeeDto coffeeDto = scrapeCoffeeInfo(element);
                if (coffeeDto != null) {
                    result.add(coffeeDto);
                }
            }

        } catch (IOException e) {
            log.error("Exception during scraping coffee entities from Mad Heads", e);
            throw new ScraperException(MAD_HEADS_SCRAPER_ERROR);
        }

        return result;
    }

    private CoffeeDto scrapeCoffeeInfo(Element element) {
        return parser.parse(element);
    }
}
