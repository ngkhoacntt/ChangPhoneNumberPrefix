package com.example.nguyenkhoahung.changephonenumberprefix;

import com.example.nguyenkhoahung.changephonenumberprefix.util.ConvertNumberUtil;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ConvertNumberUT {
    @Test
    public void checkRegexPhoneNumber(){
        String number = "+841672691993";
        boolean isPhoneNumber = ConvertNumberUtil.isValidPhoneNumber(number);
        assertEquals(true,isPhoneNumber);
        number = "+84-167-2691-993";
        isPhoneNumber = ConvertNumberUtil.isValidPhoneNumber(number);
        assertEquals(true,isPhoneNumber);
        number = "+84-167269199-3";
        isPhoneNumber = ConvertNumberUtil.isValidPhoneNumber(number);
        assertEquals(true,isPhoneNumber);
        number = "+841672691993-";
        isPhoneNumber = ConvertNumberUtil.isValidPhoneNumber(number);
        assertEquals(false,isPhoneNumber);
        number = "+84 1672691993";
        isPhoneNumber = ConvertNumberUtil.isValidPhoneNumber(number);
        assertEquals(true,isPhoneNumber);
    }

    @Test
    public void isPhoneNeedToConvert(){
        String number = "+841672691993";
        boolean isNeedNumber = ConvertNumberUtil.isPhoneNumberNeedConvert(number);
        assertEquals(true,isNeedNumber);
        number = "01672691997";
        isNeedNumber = ConvertNumberUtil.isPhoneNumberNeedConvert(number);
        assertEquals(true,isNeedNumber);
        number = "0167 2691 997";
        isNeedNumber = ConvertNumberUtil.isPhoneNumberNeedConvert(number);
        assertEquals(true,isNeedNumber);
        number = "0167-2691-997";
        isNeedNumber = ConvertNumberUtil.isPhoneNumberNeedConvert(number);
        assertEquals(true,isNeedNumber);
        number = "0167(291)997";
        isNeedNumber = ConvertNumberUtil.isPhoneNumberNeedConvert(number);
        assertEquals(false,isNeedNumber);
    }
}
