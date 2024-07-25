package com.zequence.ZequenceIms.service.token;

import org.springframework.stereotype.Service;

import java.util.function.Predicate;
import java.util.regex.Pattern;

@Service
public class EmailValidator implements Predicate<String> {

    public static Pattern EMAIL_PATTERN =
            Pattern.compile("^[a-zA-Z0-9#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9#$%&'*+/=?^_`{|}~-]+)*@" +
                    "(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?$");

    @Override
    public boolean test(String email) {
            if (email == null) {
                return false;
            }
            return EMAIL_PATTERN.matcher(email).matches();
        }

}
