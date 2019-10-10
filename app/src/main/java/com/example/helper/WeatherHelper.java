package com.example.helper;

import java.util.List;

/**
 * This helper class is used
 * to map the json data corresponding
 * to all the objects
 */
public class WeatherHelper {

    private List<Weather> weather;
    private String base;
    private Main main;
    private Clouds clouds;
    private String name;

    public WeatherHelper(List<Weather> weather, String base, Main main, Clouds clouds, String name) {
        this.weather = weather;
        this.base = base;
        this.main = main;
        this.clouds = clouds;
        this.name = name;
    }
    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public Clouds getClouds() {
        return clouds;
    }

    public void setClouds(Clouds clouds) {
        this.clouds = clouds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
