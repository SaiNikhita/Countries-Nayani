/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.controllers;

import edu.vt.globals.Constants;
import edu.vt.globals.Methods;
import java.io.Serializable;
import java.text.NumberFormat;
import java.util.List;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import org.primefaces.shaded.json.JSONObject;

@Named("currencyConverterController")
@SessionScoped

public class CurrencyConverterController implements Serializable {
    /*
    ===============================
    Instance Variables (Properties)
    ===============================
     */
    // Provided by the user
    private String currencyNameWithIdFrom;
    private String currencyNameWithIdTo;
    private Double amountToConvert;

    // Computed in this class
    private String conversionResult;

    // Used for processing
    /*
     The following list of currencies with IDs is obtained from the CurrencyLayer API.
     It is provided as a constant list to prevent unnecessary API access.
     */
    //
    private List<String> currencyNamesWithIds = List.of("Afghan Afghani  (AFN)", "Albanian Lek  (ALL)", "Algerian Dinar  (DZD)", "Angolan Kwanza  (AOA)", "Argentine Peso  (ARS)", "Armenian Dram  (AMD)", "Aruban Florin  (AWG)", "Australian Dollar  (AUD)", "Azerbaijani Manat  (AZN)", "Bahamian Dollar  (BSD)", "Bahraini Dinar  (BHD)", "Bangladeshi Taka  (BDT)", "Barbadian Dollar  (BBD)", "Belarusian Ruble  (BYR)", "Belize Dollar  (BZD)", "Bermudan Dollar  (BMD)", "Bhutanese Ngultrum  (BTN)", "Bitcoin  (BTC)", "Bolivian Boliviano  (BOB)", "Bosnia-Herzegovina Convertible Mark  (BAM)", "Botswanan Pula  (BWP)", "Brazilian Real  (BRL)", "British Pound Sterling  (GBP)", "Brunei Dollar  (BND)", "Bulgarian Lev  (BGN)", "Burundian Franc  (BIF)", "Cambodian Riel  (KHR)", "Canadian Dollar  (CAD)", "Cape Verdean Escudo  (CVE)", "Cayman Islands Dollar  (KYD)", "CFA Franc BCEAO  (XOF)", "CFA Franc BEAC  (XAF)", "CFP Franc  (XPF)", "Chilean Peso  (CLP)", "Chilean Unit of Account (UF)  (CLF)", "Chinese Yuan  (CNY)", "Colombian Peso  (COP)", "Comorian Franc  (KMF)", "Congolese Franc  (CDF)", "Costa Rican Colón  (CRC)", "Croatian Kuna  (HRK)", "Cuban Convertible Peso  (CUC)", "Cuban Peso  (CUP)", "Czech Republic Koruna  (CZK)", "Danish Krone  (DKK)", "Djiboutian Franc  (DJF)", "Dominican Peso  (DOP)", "East Caribbean Dollar  (XCD)", "Egyptian Pound  (EGP)", "Eritrean Nakfa  (ERN)", "Ethiopian Birr  (ETB)", "Euro  (EUR)", "Falkland Islands Pound  (FKP)", "Fijian Dollar  (FJD)", "Gambian Dalasi  (GMD)", "Georgian Lari  (GEL)", "Ghanaian Cedi  (GHS)", "Gibraltar Pound  (GIP)", "Gold (troy ounce)  (XAU)", "Guatemalan Quetzal  (GTQ)", "Guernsey Pound  (GGP)", "Guinean Franc  (GNF)", "Guyanaese Dollar  (GYD)", "Haitian Gourde  (HTG)", "Honduran Lempira  (HNL)", "Hong Kong Dollar  (HKD)", "Hungarian Forint  (HUF)", "Icelandic Króna  (ISK)", "Indian Rupee  (INR)", "Indonesian Rupiah  (IDR)", "Iranian Rial  (IRR)", "Iraqi Dinar  (IQD)", "Israeli New Sheqel  (ILS)", "Jamaican Dollar  (JMD)", "Japanese Yen  (JPY)", "Jersey Pound  (JEP)", "Jordanian Dinar  (JOD)", "Kazakhstani Tenge  (KZT)", "Kenyan Shilling  (KES)", "Kuwaiti Dinar  (KWD)", "Kyrgystani Som  (KGS)", "Laotian Kip  (LAK)", "Latvian Lats  (LVL)", "Lebanese Pound  (LBP)", "Lesotho Loti  (LSL)", "Liberian Dollar  (LRD)", "Libyan Dinar  (LYD)", "Lithuanian Litas  (LTL)", "Macanese Pataca  (MOP)", "Macedonian Denar  (MKD)", "Malagasy Ariary  (MGA)", "Malawian Kwacha  (MWK)", "Malaysian Ringgit  (MYR)", "Maldivian Rufiyaa  (MVR)", "Manx pound  (IMP)", "Mauritanian Ouguiya  (MRO)", "Mauritian Rupee  (MUR)", "Mexican Peso  (MXN)", "Moldovan Leu  (MDL)", "Mongolian Tugrik  (MNT)", "Moroccan Dirham  (MAD)", "Mozambican Metical  (MZN)", "Myanma Kyat  (MMK)", "Namibian Dollar  (NAD)", "Nepalese Rupee  (NPR)", "Netherlands Antillean Guilder  (ANG)", "New Belarusian Ruble  (BYN)", "New Taiwan Dollar  (TWD)", "New Zealand Dollar  (NZD)", "Nicaraguan Córdoba  (NIO)", "Nigerian Naira  (NGN)", "North Korean Won  (KPW)", "Norwegian Krone  (NOK)", "Omani Rial  (OMR)", "Pakistani Rupee  (PKR)", "Panamanian Balboa  (PAB)", "Papua New Guinean Kina  (PGK)", "Paraguayan Guarani  (PYG)", "Peruvian Nuevo Sol  (PEN)", "Philippine Peso  (PHP)", "Polish Zloty  (PLN)", "Qatari Rial  (QAR)", "Romanian Leu  (RON)", "Russian Ruble  (RUB)", "Rwandan Franc  (RWF)", "Saint Helena Pound  (SHP)", "Salvadoran colón  (SVC)", "Samoan Tala  (WST)", "São Tomé and Príncipe Dobra  (STD)", "Saudi Riyal  (SAR)", "Serbian Dinar  (RSD)", "Seychellois Rupee  (SCR)", "Sierra Leonean Leone  (SLE)", "Sierra Leonean Leone  (SLL)", "Silver (troy ounce)  (XAG)", "Singapore Dollar  (SGD)", "Solomon Islands Dollar  (SBD)", "Somali Shilling  (SOS)", "South African Rand  (ZAR)", "South Korean Won  (KRW)", "Sovereign Bolivar  (VES)", "Special Drawing Rights  (XDR)", "Sri Lankan Rupee  (LKR)", "Sudanese Pound  (SDG)", "Surinamese Dollar  (SRD)", "Swazi Lilangeni  (SZL)", "Swedish Krona  (SEK)", "Swiss Franc  (CHF)", "Syrian Pound  (SYP)", "Tajikistani Somoni  (TJS)", "Tanzanian Shilling  (TZS)", "Thai Baht  (THB)", "Tongan Paʻanga   (TOP)", "Trinidad and Tobago Dollar  (TTD)", "Tunisian Dinar  (TND)", "Turkish Lira  (TRY)", "Turkmenistani Manat  (TMT)", "Ugandan Shilling  (UGX)", "Ukrainian Hryvnia  (UAH)", "United Arab Emirates Dirham  (AED)", "United States Dollar  (USD)", "Uruguayan Peso  (UYU)", "Uzbekistan Som  (UZS)", "Vanuatu Vatu  (VUV)", "Venezuelan Bolívar  (VEF)", "Vietnamese Dong  (VND)", "Yemeni Rial  (YER)", "Zambian Kwacha  (ZMW)", "Zambian Kwacha (pre-2013)  (ZMK)", "Zimbabwean Dollar (ZWL)");

    /*
    =========================
    Getter and Setter Methods
    =========================
     */
    public String getCurrencyNameWithIdFrom() {
        return currencyNameWithIdFrom;
    }

    public void setCurrencyNameWithIdFrom(String currencyNameWithIdFrom) {
        this.currencyNameWithIdFrom = currencyNameWithIdFrom;
    }

    public String getCurrencyNameWithIdTo() {
        return currencyNameWithIdTo;
    }

    public void setCurrencyNameWithIdTo(String currencyNameWithIdTo) {
        this.currencyNameWithIdTo = currencyNameWithIdTo;
    }

    public Double getAmountToConvert() {
        return amountToConvert;
    }

    public void setAmountToConvert(Double amountToConvert) {
        this.amountToConvert = amountToConvert;
    }

    public String getConversionResult() {
        return conversionResult;
    }

    public void setConversionResult(String conversionResult) {
        this.conversionResult = conversionResult;
    }

    public List<String> getCurrencyNamesWithIds() {
        return currencyNamesWithIds;
    }

    public void setCurrencyNamesWithIds(List<String> currencyNamesWithIds) {
        this.currencyNamesWithIds = currencyNamesWithIds;
    }

    /*
    ================
    Instance Methods
    ================
     */
    public void convertCurrency() {

        String[] fromParts = currencyNameWithIdFrom.split(" \\(");
        /*
         Split "United States Dollar  (USD)" before " (" and after:
            fromParts[0] = "United States Dollar "
            fromParts[1] = "USD)"
         */

        // Delete the last character
        String currencyIdFrom = fromParts[1].replaceAll("\\)", "");

        //--------------------------------------

        String[] toParts = currencyNameWithIdTo.split(" \\(");
        /*
         Split "European Euro (EUR)" before " (" and after:
            toParts[0] = "European Euro "
            toParts[1] = "EUR)"
         */

        // Delete the last character )
        String currencyIdTo = toParts[1].replaceAll("\\)", "");

        //--------------------------------------

                /*
        https://api.exchangerate.host/convert?to=EUR&from=USD&amount=100
        {
            "motd":{"msg":"If you use this project...","url":https://exchangerate.host/#/donate},
            "success":true,
            "query":{"from":"USD","to":"EUR","amount":100},
            "info":{"rate":0.936727},
            "historical":false,
            "date":"2023-09-16",
          ✅"result":93.672727
        }
        */

        String apiUrl = "https://api.exchangerate.host/convert?to=" + currencyIdTo +
        "&from=" + currencyIdFrom +
                "&amount=" + amountToConvert;


        try {
            // Obtain the JSON file from the apiUrl
            String jsonData = Methods.readUrlContent(apiUrl);

            // Convert the JSON data into a JSON object
            JSONObject jsonObject = new JSONObject(jsonData);

            // "result": 92.076
            double result = jsonObject.optDouble("result", 0.0);

            // Convert double 'result' into String with thousand-separators and 6 decimal points
            conversionResult = String.format("%,.6f", result);

        } catch (Exception e) {
            Methods.showMessage("Error", "Currency Conversion Failed!",
                    "Unable to obtain currency conversion from the API!");
        }
    }

    public void clear() {
        currencyNameWithIdFrom = "";
        currencyNameWithIdTo = "";
        amountToConvert = null;
        conversionResult = "";
    }

}
