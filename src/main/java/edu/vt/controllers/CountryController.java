/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Country;
import edu.vt.controllers.util.JsfUtil;
import edu.vt.controllers.util.JsfUtil.PersistAction;
import edu.vt.FacadeBeans.CountryFacade;
import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import org.primefaces.shaded.json.JSONArray;
import org.primefaces.shaded.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import jakarta.ejb.EJB;
import jakarta.ejb.EJBException;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.enterprise.context.SessionScoped;

/*
---------------------------------------------------------------------------
The @Named (jakarta.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "countryController" is used within
Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("countryController")

/*
The @SessionScoped annotation preserves the values of the CountryController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
-----------------------------------------------------------------------------
Marking the CountryController class as "implements Serializable" implies that
instances of the class can be automatically serialized and deserialized. 

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer, 
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
-----------------------------------------------------------------------------
 */
public class CountryController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
    */

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the object reference of the
    CountryFacade bean into the instance variable 'countryFacade' after it is instantiated at runtime.
     */
    @EJB
    private CountryFacade countryFacade;

    @Inject
    private CountryApiController countryApiController;

    // List of object references of Country objects
    private List<Country> listOfCountries = null;

    // selected = object reference of a selected Country object
    private Country selected;

    // Flag indicating if country data changed or not
    private Boolean countryDataChanged;

    // searchItems = List of object references of Country objects found in the search
    private List<Country> searchItems = null;

    // searchField refers to searching country name, capitalCity, language, or currency individually or search in each
    private String searchField;

    // searchString contains the character string the user entered for searching the selected searchField
    private String searchString;

    // Search type from 1 to 7
    private Integer searchType;

    // Search Query Variables (Q = Query)
    private String capitalCityQ;
    private String currencyQ;
    private String languageQ;
    private Integer maxPopulationQ;
    private Integer maxTotalAreaQ;
    private Integer minPopulationQ;
    private Integer minTotalAreaQ;
    private String nameQ;
    private Integer populationQ;
    private Integer totalAreaQ;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public List<Country> getListOfCountries() {
        if (listOfCountries == null) {
            listOfCountries = countryFacade.findAll();
        }
        return listOfCountries;
    }

    public Country getSelected() {
        return selected;
    }

    public void setSelected(Country selected) {
        this.selected = selected;
    }

    public Boolean getCountryDataChanged() {
        return countryDataChanged;
    }

    public void setCountryDataChanged(Boolean countryDataChanged) {
        this.countryDataChanged = countryDataChanged;
    }

    // getSearchItems() is given at the bottom

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

    // searchType is given as a parameter of a method call

    public String getCapitalCityQ() {
        return capitalCityQ;
    }

    public void setCapitalCityQ(String capitalCityQ) {
        this.capitalCityQ = capitalCityQ;
    }

    public String getCurrencyQ() {
        return currencyQ;
    }

    public void setCurrencyQ(String currencyQ) {
        this.currencyQ = currencyQ;
    }

    public String getLanguageQ() {
        return languageQ;
    }

    public void setLanguageQ(String languageQ) {
        this.languageQ = languageQ;
    }

    public Integer getMaxPopulationQ() {
        return maxPopulationQ;
    }

    public void setMaxPopulationQ(Integer maxPopulationQ) {
        this.maxPopulationQ = maxPopulationQ;
    }

    public Integer getMaxTotalAreaQ() {
        return maxTotalAreaQ;
    }

    public void setMaxTotalAreaQ(Integer maxTotalAreaQ) {
        this.maxTotalAreaQ = maxTotalAreaQ;
    }

    public Integer getMinPopulationQ() {
        return minPopulationQ;
    }

    public void setMinPopulationQ(Integer minPopulationQ) {
        this.minPopulationQ = minPopulationQ;
    }

    public Integer getMinTotalAreaQ() {
        return minTotalAreaQ;
    }

    public void setMinTotalAreaQ(Integer minTotalAreaQ) {
        this.minTotalAreaQ = minTotalAreaQ;
    }

    public String getNameQ() {
        return nameQ;
    }

    public void setNameQ(String nameQ) {
        this.nameQ = nameQ;
    }

    public Integer getPopulationQ() {
        return populationQ;
    }

    public void setPopulationQ(Integer populationQ) {
        this.populationQ = populationQ;
    }

    public Integer getTotalAreaQ() {
        return totalAreaQ;
    }

    public void setTotalAreaQ(Integer totalAreaQ) {
        this.totalAreaQ = totalAreaQ;
    }

    /*
    ================
    Instance Methods
    ================
     */

    /*
     *****************************
     *   Return Google API Key   *
     *****************************
     */
    public String googleApiKey() {
        return Constants.GOOGLE_API_KEY;
    }

    // Country flag URL requires 2-letter country code to be in lowercase
    public String countryCodeInLowerCase(String codeGiven) {
        return codeGiven.toLowerCase();
    }

    /**
     * Determine the map zoom level for the given country total area
     *
     * @param countryTotalArea Given country total area in square kilometers
     * @return Country Google map zoom level as a String
     */
    public String getZoomLevel(int countryTotalArea) {

        // Country Google Map Zoom Level
        String mapZoomLevel = "1";

        // Adjust map zoom level depending on countryTotalArea in square kilometers
        if (countryTotalArea < 7000) {
            mapZoomLevel = "11";
        } else if (countryTotalArea < 10000) {
            mapZoomLevel = "10";
        } else if (countryTotalArea < 20000) {
            mapZoomLevel = "9";
        } else if (countryTotalArea < 30000) {
            mapZoomLevel = "8";
        } else if (countryTotalArea < 100000) {
            mapZoomLevel = "7";
        } else if (countryTotalArea < 700000) {
            mapZoomLevel = "6";
        } else if (countryTotalArea < 2000000) {
            mapZoomLevel = "5";
        } else if (countryTotalArea < 9000000) {
            mapZoomLevel = "4";
        } else if (countryTotalArea < 10000000) {
            mapZoomLevel = "3";
        } else if (countryTotalArea < 20000000) {
            mapZoomLevel = "2";
        }

        return mapZoomLevel;
    }

    /*
     ****************************************
     *   Unselect Selected Country Object   *
     ****************************************
     */
    public void unselect() {
        selected = null;
    }

    /*
     *************************************
     *   Cancel and Display List.xhtml   *
     *************************************
     */
    public String cancel() {
        // Unselect previously selected country object if any
        selected = null;
        return "/country/List?faces-redirect=true";
    }

    /*
     ***********************************************************************************
     *   Add Country Selected from the API Search Results List to the Favorites List   *
     ***********************************************************************************
     */
    public String addToFavorites() {

        // Check to see if the country to be added is already in the database
        Methods.preserveMessages();
        List<Country> countriesInDatabase = null;
        countriesInDatabase = countryFacade.nameQuery(countryApiController.getSelected().getNameCommon());
        if (countriesInDatabase != null) {
            // The country to be added already exists in the database
            Methods.showMessage("Information", "Unable to Add",
                    "The country already exists in the database!");
            return "/apiSearch/ApiSearchResults?faces-redirect=true";
        }

        selected = new Country();

        selected.setNameCommon(countryApiController.getSelected().getNameCommon());
        selected.setCca2(countryApiController.getSelected().getCca2());
        selected.setCapitalCity(countryApiController.getSelected().getCapitalCity());
        selected.setPopulation(countryApiController.getSelected().getPopulation());
        selected.setTotalArea(countryApiController.getSelected().getTotalArea());
        selected.setLanguage(countryApiController.getSelected().getLanguage());
        selected.setCurrency(countryApiController.getSelected().getCurrency());
        selected.setLatitude(countryApiController.getSelected().getLatitude());
        selected.setLongitude(countryApiController.getSelected().getLongitude());

        create();

        return "/apiSearch/ApiSearchResults?faces-redirect=true";
    }

    /*
     ***************************************
     *   Prepare to Create a New Country   *
     ***************************************
     */
    public void prepareCreate() {
        /*
        Instantiate a new Country object and store its object reference into
        instance variable 'selected'. The Country class is defined in Country.java
         */
        selected = new Country();
    }

    /*
     ***********************************
     *   Create New Country If Valid   *
     ***********************************
     */
    public void createAfterValidation() throws Exception {
        Methods.preserveMessages();

        //---------------------------------------
        // Validate Country Code and Country Name
        //---------------------------------------

        try {
            String apiUrl = "https://restcountries.com/v3.1/alpha/" + selected.getCca2();
            String searchResultsJsonData = Methods.readUrlContent(apiUrl);

            JSONArray jsonArray = new JSONArray(searchResultsJsonData);

            /*
             If the apiUrl above returns no data from the API, it implies that
             the entered 2-letter country code (cca2) is invalid.
             */
            if (jsonArray.isEmpty()) {
                Methods.showMessage("Error",
                        "Unrecognized Country Code!", "Entered 2-letter country code is invalid!");
                selected = null;            // Remove selection
                return;
            }

            JSONObject countryJsonObject = jsonArray.getJSONObject(0);

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

            JSONObject nameJsonObject = countryJsonObject.getJSONObject("name");
            String countryNameForGivenCode = nameJsonObject.optString("common", "");

            if (!countryNameForGivenCode.equals(selected.getNameCommon())) {
                Methods.showMessage("Error",
                        "Country name does not match the given country code!",
                        "Please enter correct values!");
                selected = null;            // Remove selection
                return;
            }

        } catch (IOException ex) {
            Methods.showMessage("Fatal Error", "Unrecognized Data!",
                    "Unable to create the new country with the given data!");
            selected = null;            // Remove selection
        }

        //---------------------------------------------------
        // Validation is successful to create the new country
        //---------------------------------------------------

        create();
    }


    /*
     ********************************************
     *   CREATE a New Country in the Database   *
     ********************************************
     */
    public void create() {
        Methods.preserveMessages();
        /*
        An enum is a special Java type used to define a group of constants.
        The constants CREATE, DELETE and UPDATE are defined as follows in JsfUtil.java

                public enum PersistAction {
                    CREATE,
                    DELETE,
                    UPDATE
                }
         */

        /*
         The object reference of the country to be created is stored in the instance variable 'selected'
         See the persist method below.
         */
        persist(PersistAction.CREATE, "Country was Successfully Created!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The CREATE operation is successfully performed.
            selected = null;            // Remove selection
            listOfCountries = null;     // Invalidate listOfCountries to trigger re-query.
            countryDataChanged = true;
        }
    }

    /*
     ***********************************************
     *   UPDATE Selected Country in the Database   *
     ***********************************************
     */
    public void update() {
        Methods.preserveMessages();
        /*
         The object reference of the country to be updated is stored in the instance variable 'selected'
         See the persist method below.
         */
        persist(PersistAction.UPDATE, "Country was Successfully Updated!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The UPDATE operation is successfully performed.
            selected = null;        // Remove selection
            listOfCountries = null; // Invalidate listOfCountries to trigger re-query.
            countryDataChanged = true;
        }
    }

    /*
     *************************************************
     *   DELETE Selected Country from the Database   *
     *************************************************
     */
    public void destroy() {
        Methods.preserveMessages();
        /*
         The object reference of the country to be deleted is stored in the instance variable 'selected'
         See the persist method below.
         */
        persist(PersistAction.DELETE, "Country was Successfully Deleted!");

        if (!JsfUtil.isValidationFailed()) {
            // No JSF validation error. The DELETE operation is successfully performed.
            selected = null;            // Remove selection
            listOfCountries = null;     // Invalidate list of countries to trigger re-query.
            countryDataChanged = true;
        }
    }

    /*
     **********************************************************************************************
     *   Perform CREATE, UPDATE (EDIT), and DELETE (DESTROY, REMOVE) Operations in the Database   *
     **********************************************************************************************
     */

    /**
     * @param persistAction  refers to CREATE, UPDATE (Edit) or DELETE action
     * @param successMessage displayed to inform the user about the result
     */
    private void persist(PersistAction persistAction, String successMessage) {
        if (selected != null) {
            try {
                if (persistAction != PersistAction.DELETE) {
                    /*
                     -------------------------------------------------
                     Perform CREATE or EDIT operation in the database.
                     -------------------------------------------------
                     The edit(selected) method performs the SAVE (STORE) operation of the "selected"
                     object in the database regardless of whether the object is a newly
                     created object (CREATE) or an edited (updated) object (EDIT or UPDATE).
                    
                     CountryFacade inherits the edit(selected) method from the AbstractFacade class.
                     */
                    countryFacade.edit(selected);
                } else {
                    /*
                     -----------------------------------------
                     Perform DELETE operation in the database.
                     -----------------------------------------
                     The remove(selected) method performs the DELETE operation of the "selected"
                     object in the database.
                    
                     CountryFacade inherits the remove(selected) method from the AbstractFacade class.
                     */
                    countryFacade.remove(selected);
                }
                JsfUtil.addSuccessMessage(successMessage);
            } catch (EJBException ex) {
                String msg = "";
                Throwable cause = ex.getCause();
                if (cause != null) {
                    msg = cause.getLocalizedMessage();
                }
                if (msg.length() > 0) {
                    JsfUtil.addErrorMessage(msg);
                } else {
                    JsfUtil.addErrorMessage(ex, "A persistence error occurred!");
                }
            } catch (Exception ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                JsfUtil.addErrorMessage(ex, "A persistence error occurred");
            }
        }
    }

    /*
     ******************************************
     *   Display the Search Results JSF Page  *
     ******************************************
     */
    public String search(Integer type) {
        // Set search type given as input parameter
        searchType = type;

        // Unselect previously selected country if any before showing the search results
        selected = null;

        // Invalidate list of search items to trigger re-query.
        searchItems = null;

        return "/databaseSearch/DatabaseSearchResults?faces-redirect=true";
    }

    /*
     ****************************************************************************************************
     *   Return the list of object references of all those countries that satisfy the search criteria   *
     ****************************************************************************************************
     */
    // This is the Getter method for the instance variable searchItems
    public List<Country> getSearchItems() {
        /*
        =============================================================================================
        You must construct and return the search results List "searchItems" ONLY IF the List is null.
        Any List provided to p:dataTable to display must be returned ONLY IF the List is null
        ===> in order for the column-sort to work. <===
        =============================================================================================
         */
        if (searchItems == null) {
            switch (searchType) {
                case 1: // Search Type 1
                    switch (searchField) {
                        case "Country Name":
                            // Return the list of object references of all those countries where
                            // country name contains the searchString entered by the user.
                            searchItems = countryFacade.nameQuery(searchString);
                            break;
                        case "Capital City":
                            // Return the list of object references of all those countries where
                            // capital city name contains the searchString entered by the user.
                            searchItems = countryFacade.capitalCityQuery(searchString);
                            break;
                        case "Language":
                            // Return the list of object references of all those countries where
                            // language name contains the searchString entered by the user.
                            searchItems = countryFacade.languageQuery(searchString);
                            break;
                        case "Currency":
                            // Return the list of object references of all those countries where
                            // currency name contains the searchString entered by the user.
                            searchItems = countryFacade.currencyQuery(searchString);
                            break;
                        default:
                            // Return the list of object references of all those countries where country name OR
                            // capital city name OR language name OR currency name contains the searchString entered by the user.
                            searchItems = countryFacade.allQuery(searchString);
                    }
                    break;
                case 2: // Search Type 2
                    // Country population is between minPopulationQ and maxPopulationQ
                    searchItems = countryFacade.type2SearchQuery(minPopulationQ, maxPopulationQ);
                    break;
                case 3: // Search Type 3
                    // Country totalArea is between minTotalAreaQ and maxTotalAreaQ
                    searchItems = countryFacade.type3SearchQuery(minTotalAreaQ, maxTotalAreaQ);
                    break;
                case 4: // Search Type 4
                    // Country name contains nameQ AND population ≥ populationQ
                    searchItems = countryFacade.type4SearchQuery(nameQ, populationQ);
                    break;
                case 5: // Search Type 5
                    // Country currency contains currencyQ AND totalArea ≤ totalAreaQ
                    searchItems = countryFacade.type5SearchQuery(currencyQ, totalAreaQ);
                    break;
                case 6: // Search Type 6
                    // Country language contains languageQ AND population is between minPopulationQ and maxPopulationQ
                    searchItems = countryFacade.type6SearchQuery(languageQ, minPopulationQ, maxPopulationQ);
                    break;
                case 7: // Search Type 7
                    // Country capitalCity contains capitalCityQ AND totalArea is between minTotalAreaQ and maxTotalAreaQ
                    searchItems = countryFacade.type7SearchQuery(capitalCityQ, minTotalAreaQ, maxTotalAreaQ);
                    break;
                default:
                    Methods.showMessage("Fatal Error", "Search Type is Out of Range!",
                            "");
            }
        }
        return searchItems;
    }

}
