package com.example.nguyenkhoahung.changephonenumberprefix;

public class Common {
    public static String validateNumberPhone(String number) {
        number = number.replaceAll("-", "");
        number = number.replaceAll("\n", "");
        return number;
    }
}
