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
        map.put("0169","039");
        map.put("0168","038");
        map.put("0167","037");
        map.put("0166","036");
        map.put("0165","035");
        map.put("0164","034");
        map.put("0163","033");
        map.put("0162","032");
        map.put("016966","03966");
        //prefix mobiphone
        map.put("0120","070");
        map.put("0121","079");
        map.put("0122","077");
        map.put("0126","076");
        map.put("0128","078");
        //prefix vinaphone
        map.put("0123","083");
        map.put("0124","084");
        map.put("0125","085");
        map.put("0127","081");
        map.put("0129","082");
        //prefix gmobile
        map.put("0199","059");
        //prefix vietnammobile
        map.put("0186","056");
        map.put("0188","058");
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
