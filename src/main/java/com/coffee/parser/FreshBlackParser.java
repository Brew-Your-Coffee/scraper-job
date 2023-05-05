package com.coffee.parser;

import com.coffee.domain.CoffeeDto;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.emptySet;
import static java.util.Map.entry;

@Component
@Slf4j
public class FreshBlackParser {
    private static final String SERVICE_NAME = "Fresh Black";

    private static final String COUNTRY_TITLE = "Країна";
    private static final String ROAST_TITLE = "Обсмаження";
    private static final String PROCESSING_METHODS_TITLE = "Обробка";
    private static final String TASTES_TITLE = "Смакові ноти";

    private static final Map<String, String> RECOMMENDED_BREW_METHODS_IMAGE_TO_STING = Map.ofEntries(
            entry("/assets/quiz/1.svg", "еспресо"),
            entry("/assets/quiz/3.svg", "гейзерна кавоварка"),
            entry("/assets/quiz/4.svg", "еспресо"),
            entry("/assets/quiz/5.svg", "v60"),
            entry("/assets/quiz/6.svg", "кемекс"),
            entry("/assets/quiz/7.svg", "аеропрес"),
            entry("/assets/quiz/9.svg", "фільтр"),
            entry("/assets/quiz/10.svg", "френч-прес")
    );


    public CoffeeDto parse(String url, Element productElement, Document productDescription) {
        CoffeeDto coffeeDto = new CoffeeDto();

        Element productContainerElement = productDescription.select("div.container > main.product").first();
        setDescriptionParams(productElement, coffeeDto);

        coffeeDto.setSource(SERVICE_NAME);
        coffeeDto.setUrl(url);
        coffeeDto.setDescription(getDescription(productDescription));
        setMainSectionParams(productContainerElement, coffeeDto);

        return coffeeDto;
    }

    private String getDescription(Element productContainerElement) {
        Element descriptionSectionElement = productContainerElement.select("section.product__section > div.product__content").first();
        Element descriptionElement = descriptionSectionElement.select("div.product__description").first();

        return descriptionElement.text();
    }

    private void setDescriptionParams(Element productContainerElement, CoffeeDto coffeeDto) {
        Element infoElement = productContainerElement.select("div.product-card__info > div.product-card__info-params").first();

        coffeeDto.setCountry(getSingleElement(infoElement, COUNTRY_TITLE));
        coffeeDto.setRoast(getSingleElement(infoElement, ROAST_TITLE));
        coffeeDto.setProcessingMethod(getSingleElement(infoElement, PROCESSING_METHODS_TITLE));
        coffeeDto.setTastes(getElementsSet(infoElement, TASTES_TITLE));

        coffeeDto.setRecommendedBrewMethods(getRecommendedBrewMethods(productContainerElement));
    }

    private void setMainSectionParams(Element productContainerElement, CoffeeDto coffeeDto) {
        Element mainSectionElement = productContainerElement.select("section.product__section--main").first();

        String title = getTitle(mainSectionElement);

        coffeeDto.setTitle(title);
        coffeeDto.setId(String.join("_", SERVICE_NAME, title));
        coffeeDto.setImageUrl(getImageUrl(mainSectionElement));
        coffeeDto.setPrice(getPrice(mainSectionElement));
    }

    private String getTitle(Element mainSectionElement) {
        return mainSectionElement.select("div.product__info > h1.product__title").text();
    }

    private String getImageUrl(Element mainSectionElement) {
        Element imageElement = mainSectionElement.select("div.product__gallery > div.swiper-container > div.swiper-wrapper > div.swiper-slide > picture > img").first();
        return imageElement.attr("src");
    }

    private Integer getPrice(Element mainSectionElement) {
        Element priceElement = mainSectionElement.select("span#buy-price-span").first();
        return Integer.parseInt(priceElement.ownText());
    }

    private String getSingleElement(Element description, String title) {
        Element additionalParamValueElement = getAdditionalParamValueElement(description, title);
        return additionalParamValueElement != null ? additionalParamValueElement.text().toLowerCase() : null;
    }

    private Set<String> getElementsSet(Element description, String title) {
        Element additionalParamValueElement = getAdditionalParamValueElement(description, title);

        if (additionalParamValueElement == null) return emptySet();

        return Arrays.stream(additionalParamValueElement.text().split("[\\s,\\s]+"))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
    }

    private Element getAdditionalParamValueElement(Element description, String title) {
        List<Element> descriptionElements = description.select("div.product-card__info-param");

        for (Element element : descriptionElements) {
            String elementTitle = element.select("div.product-card__info-param-title").first().text();
            if (title.equals(elementTitle)) {
                return element.select("div.product-card__info-param-value").first();
            }
        }

        return null;
    }

    private Set<String> getRecommendedBrewMethods(Element description) {
        Element recommendedBrewMethodElement = description.select("div.product-card__info-bottom").first();

        if (recommendedBrewMethodElement == null) return emptySet();

        List<Element> values = recommendedBrewMethodElement.select("div.product-card__info-roasted > img");

        Set<String> result = new HashSet<>();

        for (Element valueElement : values) {
            String valueImage = valueElement.attr("src");
            String value = RECOMMENDED_BREW_METHODS_IMAGE_TO_STING.getOrDefault(valueImage, null);
            if (value != null) {
                result.add(value);
            }
        }

        return result;
    }
}
