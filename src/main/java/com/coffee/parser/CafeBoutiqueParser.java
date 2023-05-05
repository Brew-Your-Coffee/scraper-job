package com.coffee.parser;

import com.coffee.domain.CoffeeDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CafeBoutiqueParser {
    private static final String SERVICE_NAME = "CafeBoutique";

    public CoffeeDto parse(String url, Document productDescription) {
        CoffeeDto coffeeDto = new CoffeeDto();


        String title = getTitle(productDescription);

        coffeeDto.setId(String.join("_", SERVICE_NAME, title));
        coffeeDto.setUrl(url);
        coffeeDto.setTitle(title);
        coffeeDto.setPrice(getPrice(productDescription));
        coffeeDto.setSource(SERVICE_NAME);
        coffeeDto.setCountry(getCountry(title));
        setDescriptionParams(productDescription, coffeeDto);

        return coffeeDto;
    }

    private String getTitle(Document document) {
        Element productTitleElement = document.select("#content > h1").first();

        return productTitleElement != null ? productTitleElement.text() : null;
    }

    private Integer getPrice(Document document) {
        Element productPriceElement = document.select(".autocalc-product-price").first();


        String priceStr = productPriceElement != null ? productPriceElement.text() : null;
        if (priceStr == null) return null;

        return Integer.parseInt(priceStr.split("\\.")[0]);
    }

    private String getCountry(String title) {
        if(title == null) return null;
        String country = title.split("\\s+")[0];
        return SERVICE_NAME.equals(country) ? null : country;
    }

    private void setDescriptionParams(Document document, CoffeeDto coffeeDto) {
        Element content = document.select("#content").first();
        Element description = content.select("#tab-description").first();

        coffeeDto.setDescription(getSingleElement(description, 0));
        coffeeDto.setRoast(getSingleElement(description, 2).toLowerCase());
        coffeeDto.setTastes(getElementsSet(description, 4));
        coffeeDto.setRecommendedBrewMethods(getElementsSet(description, 6));

        Element imageElement = content.select("ul.thumbnails > li > a.thumbnail").first();
        if (imageElement != null) {
            coffeeDto.setImageUrl(imageElement.attr("href"));
        }
    }

    private String getSingleElement(Element description, int index) {
        Element descriptionElement = description.select("p").get(index);
        return descriptionElement.text();
    }

    private Set<String> getElementsSet(Element description, int index) {
        Element tastesElement = description.select("p").get(index);
        return Arrays.stream(tastesElement.text().split("[\\s,\\s]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
