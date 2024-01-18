/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.pojo;

// This class provides a Plain Old Java Object (POJO) representing a Country returned from the API
public class CountryApi {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */

    /*
     A unique 'id' is required by the <p:dataTable> attribute rowKey="#{aCountry.id}"
     for listing and sorting the countries found as a result of the API search.
     */
    private String id = null;
    private String nameCommon;
    private String cca2;
    private String capitalCity;
    private Integer population;
    private Integer totalArea;
    private String language;
    private String currency;
    private Double latitude;
    private Double longitude;

    /*
    ==================
    Constructor Method
    ==================
     */
    public CountryApi(String id, String nameCommon, String cca2, String capitalCity, Integer population, Integer totalArea, String language, String currency, Double latitude, Double longitude) {
        this.id = id;
        this.nameCommon = nameCommon;
        this.cca2 = cca2;
        this.capitalCity = capitalCity;
        this.population = population;
        this.totalArea = totalArea;
        this.language = language;
        this.currency = currency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameCommon() {
        return nameCommon;
    }

    public void setNameCommon(String nameCommon) {
        this.nameCommon = nameCommon;
    }

    public String getCca2() {
        return cca2;
    }

    public void setCca2(String cca2) {
        this.cca2 = cca2;
    }

    public String getCapitalCity() {
        return capitalCity;
    }

    public void setCapitalCity(String capitalCity) {
        this.capitalCity = capitalCity;
    }

    public Integer getPopulation() {
        return population;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getTotalArea() {
        return totalArea;
    }

    public void setTotalArea(Integer totalArea) {
        this.totalArea = totalArea;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
