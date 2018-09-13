/**
 * Created on 2016年10月26日
 * filename: StringUtil.java
 * Description: 
 *
 */
package com.springmvc.demo.test;

import org.springframework.util.StringUtils;

import java.util.Set;

/**
 * Title: Class StringUtil
 * Description:
 * 字符串特殊处理类
 *
 * @author dong.yuan
 * @email  dong.yuan@chinaredstar.com
 * @version 1.0.0
 */
public class StrUtil {

	/**
	 *
	 * 逗号连接属性
	 * Created on 2016年10月26日
	 * @author dong.yuan
	 * @version
	 * @param fields
	 * @return
	 */
	public static String contactField(Set<String> fields){
		StringBuffer buffer = new StringBuffer();
		int cnt = 0;
		for(String field : fields){
			if(cnt>0){
				buffer.append(","+field);
			}
			else{
				buffer.append(field);
				cnt++;
			}
		}
		return buffer.toString();
	}


	public static String toString(Object obj){
		if(obj!=null){
			return obj.toString();
		}
		return null;
	}

    public static String blankToNull(String str){
       return StringUtils.hasText(str) ? str : null;
    }

    public static String nullToBlank(Object str){
        return  null == str ? "" : str.toString();
    }

    /**
     * 全部都不为空
     * @param str
     * @return
     */
    public static boolean allRequired(String... str){
        for (String s :str){
            if (!StringUtils.hasText(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * 有一个不为空
     * @param str
     * @return
     */
    public static boolean hasNotBlank(String... str) {
        for (String s :str){
            if (StringUtils.hasText(s)){
                return true;
            }
        }
        return false;
    }

    public static String toHexString(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = "0x" + Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static String toHexStr(String s) {
        String str = "";
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch);
            str = str + s4;
        }
        return str;
    }

    public static boolean isNumeric(String str){
      if(StringUtils.isEmpty(str)){
    	  return false;
      }
      for (int i = 0; i < str.length(); i++){
    	   if (!Character.isDigit(str.charAt(i))){
    	    return false;
    	   }
    	  }
    	return true;
     }


    /**
     * 开户行 字段拼接
     * @param strings
     * @return
     */
	public static String bankAppend(String... strings){
        StringBuffer sb=new StringBuffer("");
        if (strings != null){
            for (String str:strings){
                if (!StringUtils.isEmpty(str)){
                    sb.append(str);
                }
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        String a = "";
        String b = "";
        if(!StrUtil.hasNotBlank(a, b)){
            System.out.println("===fdasfsa===");
        }
    }
}
