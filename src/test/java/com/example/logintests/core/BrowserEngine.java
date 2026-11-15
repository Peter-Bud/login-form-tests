package com.example.logintests.core;

import java.util.Locale;

public enum BrowserEngine {
    CHROMIUM("chromium", "chrome", "edge"),
    FIREFOX("firefox"),
    WEBKIT("webkit", "safari");

    private final String[] names;

    BrowserEngine(String... names) {
        this.names = names;
    }

    public static BrowserEngine from(String name) {
        String normalizedName = name.toLowerCase(Locale.ROOT);
        for (BrowserEngine browserEngine : values()) {
            for (String supportedName : browserEngine.names) {
                if (supportedName.equals(normalizedName)) {
                    return browserEngine;
                }
            }
        }
        throw new IllegalArgumentException("Unsupported browser: " + name);
    }
}
