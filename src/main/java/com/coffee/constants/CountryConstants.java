package com.coffee.constants;

import java.util.Map;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

public class CountryConstants {
    public static final Map<String, String> COUNTRY_ENGLISH_TO_UKRAINIAN = ofEntries(
            entry("Brazil", "Бразилія"),
            entry("Colombia", "Колумбія"),
            entry("Costa", "Коста-ріка"),
            entry("Costa rica", "Коста-ріка"),
            entry("Ethiopia", "Ефіопія"),
            entry("Guatemala", "Гватемала"),
            entry("India", "Індія"),
            entry("Indonesia", "Індонезія"),
            entry("Kenya", "Кенія"),
            entry("Rwanda", "Руанда"),
            entry("Burundi", "Бурунді"),
            entry("China", "Китай"),
            entry("Tanzania", "Танзанія"),
            entry("El salvador", "Ель-Сальвадор")
            );

    public static String getCountryTranslated(String country) {
        if (country == null) return null;

        return COUNTRY_ENGLISH_TO_UKRAINIAN.getOrDefault(country, country);
    }

    private CountryConstants() {
    }
}
