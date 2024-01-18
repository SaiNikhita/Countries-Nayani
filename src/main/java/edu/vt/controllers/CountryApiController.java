/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.globals.Methods;
import edu.vt.pojo.CountryApi;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.faces.context.FacesContext;

import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;
import org.primefaces.shaded.json.JSONException;

@SessionScoped
@Named("countryApiController")

public class CountryApiController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private String searchField;
    private String searchString;

    // selected = object reference of the selected CountryApi object
    private CountryApi selected;
    private List<CountryApi> listOfCountriesFound = null;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public String getSearchField() {
        return searchField;
    }

    public void setSearchField(String searchField) {
        this.searchField = searchField;
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public CountryApi getSelected() {
        return selected;
    }

    public void setSelected(CountryApi selected) {
        this.selected = selected;
    }

    public List<CountryApi> getListOfCountriesFound() {
        return listOfCountriesFound;
    }

    public void setListOfCountriesFound(List<CountryApi> listOfCountriesFound) {
        this.listOfCountriesFound = listOfCountriesFound;
    }
    /*
    ================
    Instance Methods
    ================
     */

    /*
     *******************************************
     *   Unselect Selected CountryApi Object   *
     *******************************************
     */
    public void unselect() {
        selected = null;
    }

    /*
     ************************************
     *   Perform Countries API Search   *
     ************************************
     */
    public String performSearch() throws Exception {

        // Unselect previously selected CountryApi object if any before showing the search results
        selected = null;

        //--------------------------------------------------
        // Instantiate a new listOfCountriesFound array list
        //--------------------------------------------------
        listOfCountriesFound = new ArrayList<>();

        // Initializations of Local Variables
        String searchApiUrl = null;
        String id = null;
        String nameCommon = null;
        String cca2 = null;
        String capitalCity = null;
        Integer population = null;
        Integer totalArea = null;
        String language = null;
        String currency = null;
        Double latitude = null;
        Double longitude = null;

        // Replace all occurrences of space with %20 in searchString
        // e.g., South America, New Zealand, United States of America
        searchString = searchString.replaceAll(" ", "%20");

        switch (searchField) {
            case "Country Common Name":
                // Search query can be entire common name or just part of it.
                searchApiUrl = "https://restcountries.com/v3.1/name/" + searchString;
                break;
            case "Country Full Name":
                // Search by country full name
                searchApiUrl = "https://restcountries.com/v3.1/name/" + searchString + "?fullText=true";
                break;
            case "Country Code":
                // Search by cca2, ccn3, cca3 or cioc country code
                searchApiUrl = "https://restcountries.com/v3.1/alpha/" + searchString;
                break;
            case "Capital City Name":
                // Search by capital city name
                searchApiUrl = "https://restcountries.com/v3.1/capital/" + searchString;
                break;
            case "Currency Name or Code":
                // Search by currency name or currency code
                searchApiUrl = "https://restcountries.com/v3.1/currency/" + searchString;
                break;
            case "Language Name":
                // Search by language name or language iso639_2 code
                searchApiUrl = "https://restcountries.com/v3.1/lang/" + searchString;
                break;
            case "Region":
                // Search by Region: Africa, America, Asia, Europe, Oceania.
                // Search query can be region’s full name or just part of it.
                searchApiUrl = "https://restcountries.com/v3.1/region/" + searchString;
                break;
            case "Subregion":
                // Search by Subregion: South America, Southern Europe, Central America, Eastern Asia, etc.
                // Search query can be subregion’s full name or just part of it.
                searchApiUrl = "https://restcountries.com/v3.1/subregion/" + searchString;
                break;
            default:
                System.out.println("Search Field Value is Out of Range!");
                break;
        }

        /*
        Redirecting to show a JSF page involves more than one subsequent requests and
        the messages would die from one request to another if not kept in the Flash scope.
        Since we will redirect to show the search Results page, we invoke preserveMessages().
         */
        Methods.preserveMessages();

        try {
            // Obtain the JSON file from the searchApiUrl
            String searchResultsJsonData = Methods.readUrlContent(searchApiUrl);

            /*
             ----------------------------------------------
             |   REST Countries API JSON File Structure   |
             ----------------------------------------------

             REST Countries API always returns the search results in a
             JSON Array regardless of which search category is selected.

             The following JSON data is returned for each country found
             under each search category.

             The green checkmark icon marks the data we will use.

             [                      <== Top Level Array of JSON Objects representing countries
                 {                  <== JSON Object representing a country
                  ✅"name":{
                       ✅"common":"Germany",        // "South%20Africa" --> use %20 for space in URL
                       ✅"official":"Federal Republic of Germany",
                         "nativeName":{"deu":{"official":"Bundesrepublik Deutschland","common":"Deutschland"}}
                     },
                     "tld":[".de"],
                   ✅"cca2":"DE",         // Country Code Alpha 2 (CCA2) – 2 letter country code
                     "ccn3":"276",
                   ✅"cca3":"DEU",        // Country Code Alpha 3 (CCA3) – 3 letter country code
                     "cioc":"GER",
                     "independent":true,
                     "status":"officially-assigned",
                     "unMember":true,
                   ✅"currencies":{"EUR":{"name":"Euro","symbol":"€"}},
                     "idd":{"root":"+4","suffixes":["9"]},
                   ✅"capital":["Berlin"],
                     "altSpellings":["DE","Federal Republic of Germany","Bundesrepublik Deutschland"],
                     "region":"Europe",
                     "subregion":"Western Europe",
                   ✅"languages":{"deu":"German"},
                     "translations":{ … },
                   ✅"latlng":[51.0,9.0],
                     "landlocked":false,
                     "borders":["AUT","BEL","CZE","DNK","FRA","LUX","NLD","POL","CHE"],
                   ✅"area":357114.0,    // in square kilometers
                     "demonyms":{ … },
                   ✅"population":83240525,
                     "gini":{"2016":31.9},
                     "fifa":"GER",
                     "car":{"signs":["DY"],"side":"right"},
                     "timezones":["UTC+01:00"],
                     "continents":["Europe"],
                   ✅"flags":{
                           "png":"https://flagcdn.com/w320/de.png",
                         ✅"svg":"https://flagcdn.com/de.svg"},
                     "coatOfArms":{ … },
                     "startOfWeek":"monday",
                     "capitalInfo":{"latlng":[52.52,13.4]},
                     "postalCode":{"format":"#####","regex":"^(\\d{5})$"}
                 }
             ]
             */

            JSONArray jsonArray = new JSONArray(searchResultsJsonData);

            /*
             If no search results are returned from the API for the user's search query, then
             throw a new IOException so that the Catch block at the end below is executed.
             */
            if (jsonArray.isEmpty()) throw new IOException("IOException Occurred");

            int index = 0;
            while (jsonArray.length() > index) {

                JSONObject countryJsonObject = jsonArray.getJSONObject(index);

                //---------------------------
                // Commonly Used Country Name
                //---------------------------
                /*
                    "name":{
                        "common":"Germany",
                        "official":"Federal Republic of Germany",
                        "nativeName":{...}
                     },
                 */

                try {
                    JSONObject nameJsonObject = countryJsonObject.getJSONObject("name");
                    /*
                    Try to obtain the value for the key "common" as of String type.
                    If the value is actually of String type, return it as such.
                    If the value does not exist or it is of different type, return "".
                    */
                    nameCommon = nameJsonObject.optString("common", "Country Common Name Unavailable");
                } catch(JSONException je) {
                    // Skip this iteration
                    continue;
                }

                //----------------------
                // Country 2-Letter Code
                //----------------------
                // "cca2":"DE"

                cca2 = countryJsonObject.optString("cca2", "Country 2-Letter Code Unavailable!");

                //--------------------------
                // Country Capital City Name
                //--------------------------
                // "capital":["Berlin"],

                try {
                    JSONArray capitalJsonArray = countryJsonObject.getJSONArray("capital");
                    capitalCity = capitalJsonArray.optString(0, "Capital City Name Unavailable!");
                } catch(JSONException je)
                {
                    // Skip this iteration
                    continue;
                }

                //-------------------
                // Country Population
                //-------------------
                // "population":83240525

                /*
                Try to obtain the value for the key "population" as of Integer type.
                If the value is actually of Integer type, return it as such.
                If the value does not exist or it is of different type, return 0.
                population = 0 --> Indicates that the value is unavailable.
                */
                population = countryJsonObject.optInt("population", 0);

                //----------------------------------------
                // Country Total Area in Square Kilometers
                //----------------------------------------
                // "area":357114.0

                /*
                Try to obtain the value for the key "area" as of Double type.
                If the value is actually of Double type, return it as such.
                If the value does not exist or it is of different type, return 0.0.
                area = 0.0 --> Indicates that the value is unavailable.
                */
                double countryAreaInSquareKilometers = countryJsonObject.optDouble("area", 0.0);

                totalArea = (int) countryAreaInSquareKilometers;

                //------------------------
                // Country's Main Language
                //------------------------
                // "languages":{"deu":"German"}

                JSONObject languagesJsonObject = countryJsonObject.getJSONObject("languages");

                Iterator<String> keys = languagesJsonObject.keys();
                String keyName = keys.next();

                language = languagesJsonObject.optString(keyName, "Language Unavailable!");

                //--------------
                // Currency Name
                //--------------
                // "currencies":{"EUR":{"name":"Euro","symbol":"€"}},

                JSONObject currencyObject = countryJsonObject.getJSONObject("currencies");

                Iterator<String> keysList = currencyObject.keys();
                String firstKeyName = keysList.next();
                JSONObject currencyNameSymbolObject = currencyObject.getJSONObject(firstKeyName);

                currency = currencyNameSymbolObject.optString("name", "Currency Name Unavailable!");

                //--------------------------------------
                // Country Center Latitude and Longitude
                //--------------------------------------
                // "latlng":[51.0,9.0]

                JSONArray latLngJasonArray = countryJsonObject.getJSONArray("latlng");

                latitude = latLngJasonArray.getDouble(0);
                longitude = latLngJasonArray.getDouble(1);

                id = cca2;

                /*
                =================================================================================
                Create a new CountryApi object and dress it up with its attributes obtained above
                =================================================================================
                */
                CountryApi countryFound = new CountryApi(id, nameCommon, cca2, capitalCity, population, totalArea, language, currency, latitude, longitude);

                // Add the newly created CountryApi object to the list of countries found
                listOfCountriesFound.add(countryFound);
                index++;
            }

        } catch (IOException ex) {
            Methods.showMessage("Information", "Unrecognized Search Query!",
                    "The REST Countries API provides no data for the search query entered!");

            /*
             * *****************************************************************
             * Current JSF page, on top of which the search dialog is displayed,
             * should be shown when no result is obtained from the API.
             * *****************************************************************
             */

            // Obtain the current HTTP request object from the user's session instance
            HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

            String requestURI = request.getRequestURI();
            // requestURI = /Videos-Balci/publicVideo/SearchResults.xhtml

            String appName = request.getContextPath();
            // appName = /Videos-Balci

            // Remove app name from requestURI to obtain the current page name to return to
            String currentPage = requestURI.replace(appName, "");
            // currentPage = /publicVideo/SearchResults.xhtml

            return currentPage + "?faces-redirect=true";
        }

        // Reset search queries
        searchString = "";
        searchField = null;

        return "/apiSearch/ApiSearchResults?faces-redirect=true";
    }
}
