/*
 * Created by Osman Balci on 2023.7.24
 * Copyright © 2023 Osman Balci. All rights reserved.
 */
package edu.vt.globals;

public final class Constants {
    /*
    ==================================================
    |   Use of Class Variables as Global Constants   |
    ==================================================
    All of the variables in this class are Class Variables (typed with the "static" keyword)
    with Constant values, which can be accessed in any class in the project by specifying
    Constants.CONSTANTNAME, i.e., ClassName.ClassVariableNameInCaps

    Constants are specified in capital letters.
    */

    /*
    *************************************
    CurrencyLayer API
    1.	Go to https://currencylayer.com/
    2.	Click Sign Up Free
    3.	Click Subscribe under Free Plan
    4.	Sign in with your Google account
    5.	Obtain your API key
    *************************************
     */

    /*
    -----------------------------------------
    Dr. Balci's Google Cloud Platform Account
    -----------------------------------------
    Project Name:	 VTQuest
    Project Number:	 1042281989478
    Project ID:	     vtquest-329013
    Credit Card:	 VISA (Nothing is charged since usage is very low.)

    Enabled Google APIs under this account all accessed with the same API key:
        * Directions API
        * Geocoding API
        * Maps Embed API
        * Maps JavaScript API
        * Maps Static API
        * Places API
    ============================================================================================
    1. Create your own Google Cloud Platform account using your personal Google (Gmail) account.
       Your VT Google account will not work since it requires authorization by Virginia Tech.
    2. Go to your Google Cloud Platform Console
    3. Create a project and obtain your API key.
    4. Click APIs & Services → Dashboard → Enable APIs & Services
    5. Enable the APIs listed above.
    ============================================================================================
     */
    public static final String GOOGLE_API_KEY = "ENTER-YOUR-OWN-API-KEY";

}
