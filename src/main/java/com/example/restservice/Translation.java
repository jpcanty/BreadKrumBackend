package com.example.restservice;

public class Translation {
    private String translatedText;
    private String detectedSourceLanguage;

    public void setTranslatedTest(String text) {
        this.translatedText = text;
    }

    public String getTranslatedText(){
        return this.translatedText;
    }

    public void setDetectedSourceLanguage(String source) {
        this.detectedSourceLanguage = source;
    }

    public String getDetectedSourceLanguage(){
        return this.detectedSourceLanguage;
    }
}
