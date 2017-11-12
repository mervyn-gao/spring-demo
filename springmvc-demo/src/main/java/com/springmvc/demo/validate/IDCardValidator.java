package com.springmvc.demo.validate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * Created by mengran.gao on 2017/8/8.
 */
public class IDCardValidator implements ConstraintValidator<IDCard, String> {
    @Override
    public void initialize(IDCard constraintAnnotation) {

    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //15位和18位身份证号码的正则表达式
        String patternStr = "(^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$)|(^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])((\\d{4})|\\d{3}[Xx])$)";
        Pattern pattern = Pattern.compile(patternStr);
        //如果通过该验证，说明身份证格式正确，但准确性还需计算
        if (!pattern.matcher(value).matches()) {
            return false;
        }
        if (value.length() == 15) {
            return true;
        }
        if (value.length() == 18) {
            int[] idCardWi = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2}; //将前17位加权因子保存在数组里
            int[] idCardY = new int[]{1, 0, 10, 9, 8, 7, 6, 5, 4, 3, 2}; //这是除以11后，可能产生的11位余数、验证码，也保存成数组
            int idCardWiSum = 0; //用来保存前17位各自乖以加权因子后的总和
            for (int i = 0; i < 17; i++) {
                idCardWiSum += Integer.valueOf(value.substring(i, i + 1)) * idCardWi[i];
            }

            int idCardMod = idCardWiSum % 11;//计算出校验码所在数组的位置
            String idCardLast = value.substring(17);//得到最后一位身份证号码

            //如果等于2，则说明校验码是10，身份证号码最后一位应该是X
            if (idCardMod == 2) {
                if (idCardLast == "X" || idCardLast == "x") {
                    return true;
                } else {
                    return false;
                }
            } else {
                //用计算出的验证码与最后一位身份证号码匹配，如果一致，说明通过，否则是无效的身份证号码
                if (Integer.valueOf(idCardLast) == idCardY[idCardMod]) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }
}
