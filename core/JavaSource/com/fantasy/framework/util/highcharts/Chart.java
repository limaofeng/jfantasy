package com.fantasy.framework.util.highcharts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chart {
    private List<String> categories = new ArrayList<String>();
    private String title;
    private List<Serie> series = new ArrayList<Serie>();

    public Chart(String title) {
        this.title = title;
    }

    public List<String> getCategories() {
        return this.categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Serie> getSeries() {
        return this.series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }

    public void addCategorie(String categorie) {
        this.categories.add(categorie);
    }

    public void addSerie(Serie serie) {
        this.series.add(serie);
    }

    public void setCategories(String[] categories) {
        this.categories = Arrays.asList(categories);
    }
}