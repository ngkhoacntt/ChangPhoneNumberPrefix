package com.example.nguyenkhoahung.changephonenumberprefix.util;

import android.provider.ContactsContract;

import com.example.nguyenkhoahung.changephonenumberprefix.Common;
import com.example.nguyenkhoahung.changephonenumberprefix.ContactDTO;

import java.util.ArrayList;
import java.util.List;

public class ConvertNumberUtil {
    private static String prefix = "";
    private static String number = "";
    private static String numberRemovePlusCharacter = "";
    public static boolean isPhoneNumberNeedConvert(String phoneNumber){
        number = phoneNumber.replace("-","").replaceAll(" ","");
        if(isValidPhoneNumber(number)){
            if(isNumberHasPlusCharacter(number)){
                if(isVietNamCountryCode(number)){
                    numberRemovePlusCharacter = number.replace(Constant.VIETNAM_COUNTRY_CODE,"0");
                    if(numberRemovePlusCharacter.length() != Constant.NUMBER_LENGTH_NEED_CHANGE){
                        return false;
                    } else {
                        if(numberRemovePlusCharacter.substring(0,6).equals("016966")){
                            return true;
                        }
                        if(Common.MAP_CONVERT_NUMBER.containsKey(numberRemovePlusCharacter.substring(0,4))){
                            return true;
                        }
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                if(number.length() != Constant.NUMBER_LENGTH_NEED_CHANGE){
                    return false;
                } else {
                    if(number.substring(0,6).equals("016966")){
                        return true;
                    }
                    if(Common.MAP_CONVERT_NUMBER.containsKey(number.substring(0,4))){
                        return true;
                    }
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    public static String checkMobileNetworkOperatorName(String phoneNumber){
        if(phoneNumber.substring(0,6).equals("016966")){
            return Constant.VIETTEL;
        }
        prefix = phoneNumber.substring(0,4);
        if (Common.MAP_MOBILE_NETWORK_OPERATOR.get(prefix) != null){
            return Common.MAP_MOBILE_NETWORK_OPERATOR.get(prefix);
        } else {
            return "";
        }
    }

    public static String getMobileNetworkOperatorName(String phoneNumber){
        if(phoneNumber.contains(Constant.PLUS_CHARACTER)){
            if(phoneNumber.substring(0,3).equals(Constant.VIETNAM_COUNTRY_CODE)){
                phoneNumber = phoneNumber.replace(Constant.VIETNAM_COUNTRY_CODE,"0");
                return checkMobileNetworkOperatorName(phoneNumber);
            } else {
                return "";
            }
        } else {
            return checkMobileNetworkOperatorName(phoneNumber);
        }
    }

    public static List<ContactDTO> changePrefixVietNam(List<ContactDTO> listContact){
        List<ContactDTO> listContactAfterChange = new ArrayList<>();
        for (ContactDTO contact: listContact) {
            number = contact.getPhoneNumber();
            if(isNumberHasPlusCharacter(number)){

            } else {
                if (Common.MAP_CONVERT_NUMBER.get(number.substring(0,6)) != null){

                }
            }
        }
        return listContactAfterChange;
    }

    public static List<ContactDTO> changePrefixManually(List<ContactDTO> listContact, String oldPrefix, String newPrefix){
        List<ContactDTO> listContactAfterChange = new ArrayList<>();
        String number = "";
        for (ContactDTO contact: listContact) {
            number = contact.getPhoneNumber();
            if(number.substring(0,oldPrefix.length()).equals(oldPrefix)){
                number.replace(oldPrefix,newPrefix);
                listContact.add(contact);
            }
        }
        return listContactAfterChange;
    }

    public static boolean isNumberHasPlusCharacter(String number){
        if(number.contains(Constant.PLUS_CHARACTER)){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isVietNamCountryCode(String number){
        if(number.substring(0,3).equals(Constant.VIETNAM_COUNTRY_CODE)){
            return true;
        } else {
            return false;
        }
    }

    public static boolean isValidPhoneNumber (String number){
        String regexValidPhoneNumber = "(?:\\+?(\\d{1,4}))?([-. (]*(\\d{2,3})[-. )]*)?((\\d{2,3})[-. ]*(\\d{2,4})(?:[-.x ]*(\\d+))?)";
        if(number.matches(regexValidPhoneNumber)){
            return true;
        } else {
            return false;
        }
    }
}
