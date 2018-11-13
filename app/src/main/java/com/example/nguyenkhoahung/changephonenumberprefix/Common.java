package com.example.nguyenkhoahung.changephonenumberprefix;

import android.util.Log;

import com.example.nguyenkhoahung.changephonenumberprefix.util.Constant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Common {
    public static final HashMap<String, String> MAP_CONVERT_NUMBER = initNewPrefix();
    public static final HashMap<String, String> MAP_MOBILE_NETWORK_OPERATOR = getAllMobileNewworkOperator();
    private static HashMap<String, String>  initNewPrefix(){
        HashMap<String, String> map = new HashMap<>();
        //prefix viettel
        map.put("169","39");
        map.put("168","38");
        map.put("167","37");
        map.put("166","36");
        map.put("165","35");
        map.put("164","34");
        map.put("163","33");
        map.put("162","32");
        map.put("16966","3966");
        //prefix mobiphone
        map.put("120","70");
        map.put("121","79");
        map.put("122","77");
        map.put("126","76");
        map.put("128","78");
        //prefix vinaphone
        map.put("123","83");
        map.put("124","84");
        map.put("125","85");
        map.put("127","81");
        map.put("129","82");
        //prefix gmobile
        map.put("199","59");
        //prefix vietnammobile
        map.put("186","56");
        map.put("188","58");
        return map;
    }

    private static HashMap<String, String>  getAllMobileNewworkOperator(){
        HashMap<String, String> map = new HashMap<>();
        //prefix viettel
        map.put("0169",Constant.VIETTEL);
        map.put("0168",Constant.VIETTEL);
        map.put("0167",Constant.VIETTEL);
        map.put("0166",Constant.VIETTEL);
        map.put("0165",Constant.VIETTEL);
        map.put("0164",Constant.VIETTEL);
        map.put("0163",Constant.VIETTEL);
        map.put("0162",Constant.VIETTEL);
        map.put("016966",Constant.VIETTEL);
        //prefix mobiphone
        map.put("0120",Constant.MOBIFONE);
        map.put("0121",Constant.MOBIFONE);
        map.put("0122",Constant.MOBIFONE);
        map.put("0126",Constant.MOBIFONE);
        map.put("0128",Constant.MOBIFONE);
        //prefix vinaphone
        map.put("0123",Constant.VINAPHONE);
        map.put("0124",Constant.VINAPHONE);
        map.put("0125",Constant.VINAPHONE);
        map.put("0127",Constant.VINAPHONE);
        map.put("0129",Constant.VINAPHONE);
        //prefix gmobile
        map.put("0199",Constant.GMOBILE);
        //prefix vietnammobile
        map.put("0186",Constant.VIETNAMMOBILE);
        map.put("0188",Constant.VIETNAMMOBILE);
        return map;
    }

    public static String validateNumberPhone(String number) {
        number = number.replaceAll("-", "");
        number = number.replaceAll(" ", "");
        return number;
    }

    public static boolean isEmptyList(List<?> list){
        if(list != null && list.size() > 0){
            return false;
        } else {
            Log.e("ChangePhoneNumber","Null List" ,new Throwable());
            return true;
        }
    }

    public static boolean isEmptyString(String string){
        if(string != null && !string.equals("")){
            return false;
        } else {
            Log.e("ChangePhoneNumber","Null String",new Throwable() );
            return true;
        }
    }

    public static List<ContactDTO> filterByMobileNetworkOperator(List<ContactDTO> listContact, String operator){
        List<ContactDTO> listAfterFilter = new ArrayList<>();
        switch (operator){
            case Constant
                    .VIETTEL:
                for (ContactDTO contact: listContact
                     ) {

                }
                break;
            case Constant
                    .VINAPHONE:

                break;
            case Constant
                    .MOBIFONE:

                break;
            case Constant
                    .GMOBILE:

                break;
            case Constant
                    .VIETNAMMOBILE:

                break;
        }
        return  listAfterFilter;
    }
}
