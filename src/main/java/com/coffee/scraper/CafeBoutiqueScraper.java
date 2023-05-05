package com.coffee.scraper;

import com.coffee.domain.CoffeeDto;
import com.coffee.exception.ScraperException;
import com.coffee.parser.CafeBoutiqueParser;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class CafeBoutiqueScraper implements Scraper {
    private final CafeBoutiqueParser parser;

    private static final String AVAILABLE = "Придбати";

    @Override
    public List<CoffeeDto> scrape() {
        List<CoffeeDto> result = new ArrayList<>();

        try {
            int i = 1;
            // Fetch the web page
            while (true) {
                Document mainPage = Jsoup.connect("https://cafeboutique.ua/ua/kofe/page-" + i).get();
                List<Element> elements = mainPage.select(".product-layout.product-list");

                for (Element element : elements) {
                    String availability = element.select("button > span").first().text();
                    String ref = element.select(".caption > h4 > a").first().attr("href");

                    if (availability.equals(AVAILABLE)) {

                        CoffeeDto coffeeDto = scrapeCoffeeInfo(ref);

                        if (coffeeDto != null) {
                            result.add(coffeeDto);
                        }
                    } else {
                        return result;
                    }
                }
                i++;
            }
        } catch (IOException e) {
            log.error("Exception during scraping coffee entities from CafeBoutique", e);
            throw new ScraperException(CAFE_BOUTIQUE_SCRAPER_ERROR);
        }
    }

    private CoffeeDto scrapeCoffeeInfo(String ref) {
        try {
            Document document = Jsoup.connect(ref).get();

            return parser.parse(ref, document);
        } catch (IOException e) {
            log.error("Exception during scraping coffee entities from CafeBoutique", e);
            throw new ScraperException(CAFE_BOUTIQUE_SCRAPER_ERROR);
        }
    }
}
