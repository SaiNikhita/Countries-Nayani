/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.EntityBeans.Country;
import edu.vt.FacadeBeans.CountryFacade;

import edu.vt.globals.Methods;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;

import org.primefaces.model.charts.bar.BarChartDataSet;
import org.primefaces.model.charts.bar.BarChartModel;
import org.primefaces.model.charts.ChartData;

/*
---------------------------------------------------------------------------
The @Named (jakarta.inject.Named) annotation indicates that the objects
instantiated from this class will be managed by the Contexts and Dependency
Injection (CDI) container. The name "populationChartController" is used
within Expression Language (EL) expressions in JSF (XHTML) facelets pages
to access the properties and invoke methods of this class.
---------------------------------------------------------------------------
 */
@Named("populationChartController")

/*
The @SessionScoped annotation preserves the values of the PopulationChartController
object's instance variables across multiple HTTP request-response cycles
as long as the user's established HTTP session is alive.
 */
@SessionScoped

/*
--------------------------------------------------------------------------------
Marking the PopulationChartController class as "implements Serializable" implies
that instances of the class can be automatically serialized and deserialized.

Serialization is the process of converting a class instance (object)
from memory into a suitable format for storage in a file or memory buffer,
or for transmission across a network connection link.

Deserialization is the process of recreating a class instance (object)
in memory from the format under which it was stored.
--------------------------------------------------------------------------------
 */
public class PopulationChartController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    private BarChartModel barChartModel;

    /*
    The @Inject annotation directs the CDI Container Manager to inject (store) the
    object reference of the CDI container-managed PickListController bean into the
    instance variable 'pickListController' after it is instantiated at runtime.
     */
    @Inject
    PickListController pickListController;

    /*
    The @EJB annotation directs the EJB Container Manager to inject (store) the
    object reference of the CountryFacade bean into the instance variable
    'countryFacade' after it is instantiated at runtime.
     */
    @EJB
    private CountryFacade countryFacade;

    /*
    =========================
    Getter and Setter Methods
    =========================
     */

    public BarChartModel getBarChartModel() {
        return barChartModel;
    }

    public void setBarChartModel(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }

    public PickListController getPickListController() {
        return pickListController;
    }

    public void setPickListController(PickListController pickListController) {
        this.pickListController = pickListController;
    }

    public CountryFacade getCountryFacade() {
        return countryFacade;
    }

    public void setCountryFacade(CountryFacade countryFacade) {
        this.countryFacade = countryFacade;
    }

    /*
    ================
    Instance Methods
    ================
     */

    public String displayPopulationChart() {

        Methods.preserveMessages();

        barChartModel = new BarChartModel();
        ChartData chartData = new ChartData();

        BarChartDataSet barChartDataSet = new BarChartDataSet();
        barChartDataSet.setLabel("Population in Millions");

        // Obtain the list of selected country names
        List<String> selectedCountryNamesList = pickListController.getCountryNames().getTarget();

        List<Integer> countryPopulations = new ArrayList<>();

        // Iteration using forEach() method with lamda
        selectedCountryNamesList.forEach((countryName) -> {

            // Obtain the object reference of the country object whose name is countryName
            Country countryObject = countryFacade.findByCountryName(countryName);

            // Obtain the country's population in millions
            Integer population = countryObject.getPopulation() / 1000000;

            countryPopulations.add(population);
        });

        barChartDataSet.setData(Collections.unmodifiableList(countryPopulations));

        List<String> bgColor = new ArrayList<>();
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");
        bgColor.add("rgba(153, 102, 255, 0.2)");
        bgColor.add("rgba(201, 203, 207, 0.2)");
        bgColor.add("rgba(255, 99, 132, 0.2)");
        bgColor.add("rgba(255, 159, 64, 0.2)");
        bgColor.add("rgba(255, 205, 86, 0.2)");
        bgColor.add("rgba(75, 192, 192, 0.2)");
        bgColor.add("rgba(54, 162, 235, 0.2)");

        barChartDataSet.setBackgroundColor(bgColor);

        List<String> borderColor = new ArrayList<>();
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");
        borderColor.add("rgb(153, 102, 255)");
        borderColor.add("rgb(201, 203, 207)");
        borderColor.add("rgb(255, 99, 132)");
        borderColor.add("rgb(255, 159, 64)");
        borderColor.add("rgb(255, 205, 86)");
        borderColor.add("rgb(75, 192, 192)");
        borderColor.add("rgb(54, 162, 235)");

        barChartDataSet.setBorderColor(borderColor);
        barChartDataSet.setBorderWidth(1);

        chartData.addChartDataSet(barChartDataSet);

        chartData.setLabels(selectedCountryNamesList);
        barChartModel.setData(chartData);

        return "/chart/PopulationChartResults?faces-redirect=true";
    }

}
