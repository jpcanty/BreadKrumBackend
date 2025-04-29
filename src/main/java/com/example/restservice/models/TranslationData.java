package com.example.restservice.models;

import java.util.List;

public class TranslationData {
    private List<Translation> translations;

    public List<Translation> geTranslations() {
        return this.translations;
    }

    public void setTranslations(List<Translation> translations) {
        this.translations = translations;
    }
}
