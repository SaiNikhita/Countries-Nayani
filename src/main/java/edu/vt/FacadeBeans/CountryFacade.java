/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.FacadeBeans;

import edu.vt.EntityBeans.Country;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

// @Stateless annotation implies that the conversational state with the client shall NOT be maintained.
@Stateless
public class CountryFacade extends AbstractFacade<Country> {
    /*
    ---------------------------------------------------------------------------------------------
    The EntityManager is an API that enables database CRUD (Create Read Update Delete) operations
    and complex database searches. An EntityManager instance is created to manage entities
    that are defined by a persistence unit. The @PersistenceContext annotation below associates
    the entityManager instance with the persistence unitName identified below.
    ---------------------------------------------------------------------------------------------
     */
    @PersistenceContext(unitName = "Countries-NayaniPU")
    private EntityManager entityManager;

    // Obtain the object reference of the EntityManager instance in charge of
    // managing the entities in the persistence context identified above.
    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }

    /*
    This constructor method invokes its parent AbstractFacade's constructor method,
    which in turn initializes its entity class type T and entityClass instance variable.
     */
    public CountryFacade() {
        super(Country.class);
    }

    /*
    *********************
    *   Other Methods   *
    *********************
     */

    // Returns the object reference of the Country object whose name is country_name
    public Country findByCountryName(String country_name) {
        /*
        The following @NamedQuery definition is given in Country.java entity class file:
        @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.name = :name")
         */

        if (entityManager.createNamedQuery("Country.findByName")
                .setParameter("nameCommon", country_name)
                .getResultList().isEmpty()) {
            
            // Return null if no country object exists by the name of country_name
            return null;
            
        } else {
            
            // Return the Object reference of the country object whose name is country_name
            return (Country) (entityManager.createNamedQuery("Country.findByName")
                    .setParameter("nameCommon", country_name)
                    .getSingleResult());
        }
    }

    /*
    ***********************************************************************************
    *   Java Persistence API (JPA) Query Formulation for Searching a MySQL Database   *
    ***********************************************************************************
    By default, MySQL does not distinguish between upper and lower case letters in searches.
    Therefore, searches based on the queries below are all case insensitive by default.

    The LIKE Expression
        SELECT c FROM Country c WHERE c.name LIKE :'in%'       All countries whose names begin with "in"
        SELECT c FROM Country c WHERE c.name LIKE :'%tan'      All countries whose names end with "tan"
        SELECT c FROM Country c WHERE c.name LIKE :'%ge%'      All countries whose names contain "ge"

    The LIKE expression uses wildcard character % to search for strings that match the wildcard pattern.

    ================================
    EntityManager Method createQuery
    ================================
    Query createQuery(String qlString)
        Create an instance of Query for executing a Java Persistence (JPA) query language statement.
    Parameter:
        qlString - a Java Persistence query string, e.g., "SELECT c FROM Country c WHERE c.name LIKE :searchString"
    Returns:
        the object reference of the newly created Query object

    =========================
    Query Method setParameter
    =========================
    Query setParameter(String name, Object value)
        Bind an argument value to a named parameter
    Parameters:
        name - parameter name (e.g., "searchString")
        value - parameter value (e.g., the searchString parameter that contains the search string the user entered for searching)
    Returns:
        the same object reference of the newly created Query object

    ==========================
    Query Method getResultList
    ==========================
    List getResultList()
        Execute a SELECT query and return the query results as an untyped List
    Returns:
        the object reference of the newly created List containing the search results
    */

    /*
     ***************************
     *   Search Query Type 1   *
     ***************************
     */

    /*
    -----------------------------
    Search Category: Country NAME
    -----------------------------
     */
    // Searches CountriesDB for countries where Country name contains the searchString entered by the user.
    public List<Country> nameQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the Country name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.nameCommon LIKE :searchString")
                .setParameter("searchString", searchString)
                .getResultList();
    }

    /*
    -----------------------------
    Search Category: CAPITAL CITY
    -----------------------------
     */
    // Searches CountriesDB for countries where Country's capitalCity contains the searchString entered by the user.
    public List<Country> capitalCityQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the capital city name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.capitalCity LIKE :searchString")
                .setParameter("searchString", searchString)
                .getResultList();
    }

    /*
    -------------------------
    Search Category: LANGUAGE
    -------------------------
     */
    // Searches CountriesDB for countries where language contains the searchString entered by the user.
    public List<Country> languageQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the language name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.language LIKE :searchString")
                .setParameter("searchString", searchString)
                .getResultList();
    }

    /*
    -------------------------
    Search Category: CURRENCY
    -------------------------
     */
    // Searches CountriesDB for countries where currency contains the searchString entered by the user.
    public List<Country> currencyQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in the currency name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.currency LIKE :searchString")
                .setParameter("searchString", searchString)
                .getResultList();
    }

    /*
    --------------------
    Search Category: ALL
    --------------------
     */
    // Searches CountriesDB for countries where Country name OR capitalCity name OR language name OR currency name contains the searchString entered by the user.
    public List<Country> allQuery(String searchString) {
        // Place the % wildcard before and after the search string to search for it anywhere in each attribute name
        searchString = "%" + searchString + "%";
        // Conduct the search in a case-insensitive manner and return the results in a list.
        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.nameCommon LIKE :searchString OR c.capitalCity LIKE :searchString OR c.language LIKE :searchString OR c.currency LIKE :searchString")
                .setParameter("searchString", searchString)
                .getResultList();
    }

    /*
     ***************************
     *   Search Query Type 2   *
     ***************************
     */
    // Country population is between minPopulationQ and maxPopulationQ
    public List<Country> type2SearchQuery(Integer minPopulationQ, Integer maxPopulationQ) {

        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.population BETWEEN :minPopulationQ AND :maxPopulationQ")
                .setParameter("minPopulationQ", minPopulationQ)
                .setParameter("maxPopulationQ", maxPopulationQ)
                .getResultList();
    }

    /*
     ***************************
     *   Search Query Type 3   *
     ***************************
     */
    // Country totalArea is between minTotalAreaQ and maxTotalAreaQ
    public List<Country> type3SearchQuery(Integer minTotalAreaQ, Integer maxTotalAreaQ) {

        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.totalArea BETWEEN :minTotalAreaQ AND :maxTotalAreaQ")
                .setParameter("minTotalAreaQ", minTotalAreaQ)
                .setParameter("maxTotalAreaQ", maxTotalAreaQ)
                .getResultList();
    }

    /*
     ***************************
     *   Search Query Type 4   *
     ***************************
     */
    // Country name contains nameQ AND population ≥ populationQ
    public List<Country> type4SearchQuery(String nameQ, Integer populationQ) {

        nameQ = "%" + nameQ + "%";

        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.nameCommon LIKE :nameQ AND c.population >= :populationQ")
                .setParameter("nameQ", nameQ)
                .setParameter("populationQ", populationQ)
                .getResultList();
    }

    /*
     ***************************
     *   Search Query Type 5   *
     ***************************
     */
    // Country currency contains currencyQ AND totalArea ≤ totalAreaQ
    public List<Country> type5SearchQuery(String currencyQ, Integer totalAreaQ) {

        currencyQ = "%" + currencyQ + "%";

        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.currency LIKE :currencyQ AND c.totalArea <= :totalAreaQ")
                .setParameter("currencyQ", currencyQ)
                .setParameter("totalAreaQ", totalAreaQ)
                .getResultList();
    }

    /*
     ***************************
     *   Search Query Type 6   *
     ***************************
     */
    // Country language contains languageQ AND population is between minPopulationQ and maxPopulationQ
    public List<Country> type6SearchQuery(String languageQ, Integer minPopulationQ, Integer maxPopulationQ) {

        languageQ = "%" + languageQ + "%";

        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.language LIKE :languageQ AND c.population BETWEEN :minPopulationQ AND :maxPopulationQ")
                .setParameter("languageQ", languageQ)
                .setParameter("minPopulationQ", minPopulationQ)
                .setParameter("maxPopulationQ", maxPopulationQ)
                .getResultList();
    }

    /*
     ***************************
     *   Search Query Type 7   *
     ***************************
     */
    // Country capitalCity contains capitalCityQ AND totalArea is between minTotalAreaQ and maxTotalAreaQ
    public List<Country> type7SearchQuery(String capitalCityQ, Integer minTotalAreaQ, Integer maxTotalAreaQ) {

        capitalCityQ = "%" + capitalCityQ + "%";

        return getEntityManager().createQuery(
                "SELECT c FROM Country c WHERE c.capitalCity LIKE :capitalCityQ AND c.totalArea BETWEEN :minTotalAreaQ AND :maxTotalAreaQ")
                .setParameter("capitalCityQ", capitalCityQ)
                .setParameter("minTotalAreaQ", minTotalAreaQ)
                .setParameter("maxTotalAreaQ", maxTotalAreaQ)
                .getResultList();
    }

}
