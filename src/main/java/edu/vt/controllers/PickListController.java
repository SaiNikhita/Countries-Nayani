/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Country;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.primefaces.event.TransferEvent;
import org.primefaces.model.DualListModel;

/*
---------------------------------------------------------------------------
The @Named (jakarta.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "pickListController" is used within
the Expression Language (EL) expressions in JSF (XHTML) facelets pages to
access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("pickListController")

/*
The @SessionScoped annotation preserves the values of the PickListController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped
public class PickListController implements Serializable {

    /*
    DualListModel (org.primefaces.model.DualListModel) defines
    - countryNames.getSource() : List of Countries
    - countryNames.getTarget() : Selected Countries
     */
    private DualListModel<String> countryNames;

    /*
    The @Inject annotation directs the CDI Container Manager to inject (store) the object reference of the
    CDI container-managed CountryController bean into the instance variable 'countryController' after it is instantiated at runtime.
     */
    @Inject
    CountryController countryController;

    /*
    The @PostConstruct annotation causes the method 'init()' to be executed to perform
    initializations right after the PickListController bean is instantiated at runtime.
    The method 'init()' is invoked before the PickListController class is put into service.
     */
    @PostConstruct
    public void init() {
        createDualListModel();
    }

    /*
    ========================
    Getter and Setter Method
    ========================
     */
    public DualListModel<String> getCountryNames() {
        return countryNames;
    }

    public void setCountryNames(DualListModel<String> countryNames) {
        this.countryNames = countryNames;
    }

    /*
    ================
    Instance Methods
    ================
     */
    public void createDualListModel() {

        // List of All Country Names as Source
        List<String> countryNamesSource = new ArrayList<>();

        // Selected List of Country Names as Target
        List<String> countryNamesTarget = new ArrayList<>();

        // Obtain object references of all of the countries in the database
        List<Country> countryObjectsList = countryController.getListOfCountries();

        // List of names of all of the countries in the database
        List<String> countryNamesList = new ArrayList<>();

        // Iteration in Java 8 using the forEach() method
        countryObjectsList.forEach((countryObject) -> {
            // Obtain the country name and add it to the country names list
            countryNamesList.add(countryObject.getNameCommon());
        });

        // Sort the list of country names in alphabetical order
        Collections.sort(countryNamesList);

        // Iteration in Java 8 using the forEach() method
        countryNamesList.forEach((countryName) -> {
            // Add the country name to the Source of the DualListModel
            countryNamesSource.add(countryName);
        });

        /*
        Instantiate the DualListModel with Source as countryNamesSource and 
        Target as empty countryNamesTarget and store its object reference 
        into the instance variable countryNames.
         */
        countryNames = new DualListModel<>(countryNamesSource, countryNamesTarget);
    }

    public void handleTransfer(TransferEvent event) {
        /*
         event.getItems()    : List of items transferred
         event.isAdd()       : True if transfer is from Source to Target
         event.isRemove()    : True if transfer is from Target to Source
         */

        if (event.isRemove()) {
            /*
             When a country name is transferred from the Target list back to the
             Source list, it is added to the end of the list. Therefore, we need to
             sort the Source list again to always keep it in alphabetical order.
             */

            Collections.sort(countryNames.getSource());
        }
    }

    /*
    When a Create, Edit or Delete operation is performed on the database
    we set the flag countryDataChanged to true in CountryController.java.
    
    Then, when the user clicks the Country Charts link in the header, 
    we check the flag in CountryCharts.xhtml using the following:
    
            <c:if test="#{countryController.countryDataChanged}">
                #{pickListController.recreateDualListModel()}
            </c:if>
    
    Thus, if the flag countryDataChanged is true, the following method
    is invoked in which, we recreate the DualListModel to update it
    with the new country data and set the flag back to false.
    */
    public void recreateDualListModel() {

        // Invoke the method above
        createDualListModel();
        
        // Set the flag back to false
        countryController.setCountryDataChanged(false);
    }

}
