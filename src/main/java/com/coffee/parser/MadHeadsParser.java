package com.coffee.parser;

import com.coffee.domain.CoffeeDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

@Component
@Slf4j
public class MadHeadsParser {
    private static final String SERVICE_NAME = "Mad Heads";

    private static final String DONATE_TITLE = "DONATE";

    public CoffeeDto parse(Element productElement) {
        String title = getTitle(productElement);

        if (DONATE_TITLE.equals(title)) return null;
        CoffeeDto coffeeDto = new CoffeeDto();

        coffeeDto.setId(String.join("_", SERVICE_NAME, title));
        coffeeDto.setTitle(title);
        setImageParams(productElement, coffeeDto);
        coffeeDto.setPrice(getPrice(productElement));
        coffeeDto.setSource(SERVICE_NAME);
        setDescriptionParams(productElement, coffeeDto);

        if(coffeeDto.getDescription() == null) return null;

        return coffeeDto;
    }

    private void setImageParams(Element element, CoffeeDto coffeeDto) {
        Element imageContainer = element.select("a").first();

        coffeeDto.setUrl(imageContainer.attr("href"));
        coffeeDto.setImageUrl(getImageUrl(imageContainer));
        coffeeDto.setCountry(getCountry(imageContainer));
    }

    private String getTitle(Element imageContainer) {
        Element titleElement = imageContainer.select("a > h2.woocommerce-loop-product__title").first();
        String titleText = titleElement.text();
        List<String> titleTokens = Arrays.stream(titleText.split("\\s+")).toList();
        if(titleTokens.size() == 1) return titleText;
        return String.join(" ", titleTokens.subList(0, titleTokens.size() - 1));
    }

    private String getImageUrl(Element imageContainer) {
        Element imageElement = imageContainer.select("img").first();
        return imageElement.attr("src");
    }

    private String getCountry(Element imageContainer) {
        Element countryElement = imageContainer.select("div.product-country").first();
        return StringUtils.capitalize(countryElement.text().toLowerCase());
    }

    private Integer getPrice(Element productElement) {
        Element priceElement = productElement.select("span.price > span.amount").first();

        if (priceElement == null) {
            return null;
        }
        String priceStr = priceElement.ownText().replaceAll("[^\\d.]", "");
        return Integer.parseInt(priceStr);
    }

    private void setDescriptionParams(Element productElement, CoffeeDto coffeeDto) {
        Element productDescriptionElement = productElement.select(".product-description-wrap").first();

        if(productDescriptionElement == null) return;

        coffeeDto.setTastes(getElementsSet(productDescriptionElement, 1));
        coffeeDto.setProcessingMethod(getSingleElement(productDescriptionElement, 2).toLowerCase());
        coffeeDto.setVariety(getSingleElement(productDescriptionElement, 3));
        coffeeDto.setRoast(getSingleElement(productDescriptionElement, 4).toLowerCase());
        coffeeDto.setScore(getSingleElement(productDescriptionElement, 5));
        coffeeDto.setDescription(getSingleElement(productDescriptionElement, 7));
        coffeeDto.setRecommendedBrewMethods(getRecommendedBrewMethod(productDescriptionElement));
    }

    private String getSingleElement(Element description, int index) {
        Element descriptionElement = description.select("div.product-description-line").get(index)
                .select("div.product-description-line-right").first();
        return descriptionElement.text();
    }

    private Set<String> getRecommendedBrewMethod(Element description) {
        Element recommendedBrewMethodElement = description.select("div.product-description-line").get(6)
                .select("div.product-description-line-left").first();
        String[] recommendedBrewMethodTokens = recommendedBrewMethodElement.text().split("[\\s,\\s]+");
        return singleton(recommendedBrewMethodTokens[recommendedBrewMethodTokens.length - 2]);
    }

    private Set<String> getElementsSet(Element description, int index) {
        Element tastesElement = description.select("div.product-description-line").get(index)
                .select("div.product-description-line-right").first();
        return Arrays.stream(tastesElement.text().split("[\\s,\\s]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }
}
