package com.zequence.ZequenceIms.utils;

import com.zequence.ZequenceIms.entity.User;
import com.zequence.ZequenceIms.entity.UserAuthenticationDetails;

import java.util.regex.Pattern;

public class UserDefaults {
    public static final String DEFAULT_PHONE_NUMBER = "default-phone";
    public static final String DEFAULT_COUNTRY = "default-country";
    public static final String DEFAULT_STATE = "default-state";
    public static final String DEFAULT_LGA_CITY_ZIPCODE = "default-lga-city-zipcode";
    public static final String DEFAULT_GENDER = "default-gender";
    public static final String DEFAULT_PHONE = "default-phone";

    public static User createDefaultUser(UserAuthenticationDetails userAuthDetails) {
        User user = new User();
        user.setFullName(userAuthDetails.getUsername());
        user.setPhoneNumber(DEFAULT_PHONE_NUMBER);
        user.setCountry(DEFAULT_COUNTRY);
        user.setState(DEFAULT_STATE);
        user.setLgaCityZipcode(DEFAULT_LGA_CITY_ZIPCODE);
        user.setCompanyName("");
        user.setCompanyLogo("");
        user.setGender(DEFAULT_GENDER);
        user.setPhone(DEFAULT_PHONE);

        user.setAuthenticationDetails(userAuthDetails);
        userAuthDetails.setUser(user);

        return user;
    }
}
