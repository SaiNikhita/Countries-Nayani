/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.EntityBeans;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/*
The @Entity annotation designates this class as a JPA Entity POJO class
representing the Country table in the CountriesDB database.
 */
@Entity

// Name of the database table represented
@Table(name = "Country")

@NamedQueries({
        // This named query is used in CountryFacade
        @NamedQuery(name = "Country.findByName", query = "SELECT c FROM Country c WHERE c.nameCommon = :nameCommon")
})

public class Country implements Serializable {
    /*
    ========================================================
    Instance variables representing the attributes (columns)
    of the Country table in the CountriesDB database.

    CREATE TABLE Country
    (
        id INT UNSIGNED NOT NULL AUTO_INCREMENT,
        name_common VARCHAR(128) NOT NULL,
        cca2 VARCHAR(2) NOT NULL,
        capital_city VARCHAR(64) NOT NULL,
        population INT NOT NULL,
        total_area INT NOT NULL,             // in Square Kilometers
        language VARCHAR(64) NOT NULL,
        currency VARCHAR(64) NOT NULL,
        latitude DECIMAL(8, 6) NOT NULL,     // Ranges from  -90 S to  90 N
        longitude DECIMAL(9, 6) NOT NULL,    // Ranges from -180 W to 180 E
        PRIMARY KEY (id)
    );
    ========================================================
     */
    private static final long serialVersionUID = 1L;
    /*
    Primary Key id is auto generated by the system as an Integer value
    starting with 1 and incremented by 1, i.e., 1,2,3,...
    A deleted entity object's primary key number is not reused.
     */
    // id INT UNSIGNED NOT NULL AUTO_INCREMENT
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;

    // Country's commonly used name
    // name_common VARCHAR(128) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "name_common")
    private String nameCommon;

    // Country Code Alpha 2 (cca2) 2-letter unique country code
    // cca2 VARCHAR(2) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 2)
    @Column(name = "cca2")
    private String cca2;

    // Country's capital city name
    // capital_city VARCHAR(64) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "capital_city")
    private String capitalCity;

    // Country's population
    // population INT NOT NULL
    @Basic(optional = false)
    @NotNull
    @Column(name = "population")
    private Integer population;

    // Country's total area in Square Kilometers
    // total_area INT NOT NULL
    @Basic(optional = false)
    @NotNull
    @Column(name = "total_area")
    private Integer totalArea;

    // Country's most-used language name
    // language VARCHAR(64) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "language")
    private String language;

    // Country's currency name
    // currency VARCHAR(64) NOT NULL
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "currency")
    private String currency;

    /*
     Country's geolocation latitude. Ranges from  -90 S to 90 N
     latitude DECIMAL(8, 6) NOT NULL

     MySQL data type DECIMAL(M,D) can be represented as Java type Double
      - M is the maximum number of digits (the precision)
      - D is the number of digits to the right of the decimal point (the scale)
     Examples: 39.074208, -32.522779
     */
    @Basic(optional = false)
    @NotNull
    @Column(name = "latitude")
    private Double latitude;

    /*
     Country's geolocation longitude. Ranges from -180 W to 180 E
     longitude DECIMAL(9, 6) NOT NULL

     MySQL data type DECIMAL(M,D) can be represented as Java type Double
      - M is the maximum number of digits (the precision)
      - D is the number of digits to the right of the decimal point (the scale)
     Examples: 127.766922, -106.346771
     */
    @Basic(optional = false)
    @NotNull
    @Column(name = "longitude")
    private Double longitude;

    /*
    =================================================================
    Class constructors for instantiating a Country entity object to
    represent a row in the Country table in the CountriesDB database.
    =================================================================
     */
    // Used in PrepareCreate method in CountryController
    public Country() {
    }

    /*
    ======================================================
    Getter and Setter methods for the attributes (columns)
    of the Country table in the CountriesDB database.
    ======================================================
     */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    /*
    ================================
    Instance Methods Used Internally
    ================================
     */

    // Generate and return a hash code value for the object with database primary key id
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    /*
     Checks if the Country object identified by 'object' is the same as the Country object identified by 'id'
     Parameter object = Country object identified by 'object'
     Returns True if the Country 'object' and 'id' are the same; otherwise, return False
     */
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Country)) {
            return false;
        }
        Country other = (Country) object;
        return (this.id != null || other.id == null) && (this.id == null || this.id.equals(other.id));
    }

    // Return String representation of database primary key id
    @Override
    public String toString() {
        return id.toString();
    }

}
