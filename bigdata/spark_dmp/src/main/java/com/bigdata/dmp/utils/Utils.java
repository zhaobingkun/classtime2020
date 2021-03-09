package com.bigdata.dmp.utils;

import org.apache.commons.lang.StringUtils;

public  class Utils {

    public static void main(String[] args) {
        System.out.println(Utils.fmtDate("2018-12-12 12:11:10"));
        System.out.println(Utils.fmtTime("2018-12-12 12:11:10"));
    }

    public static String fmtDate(String s){
        if (StringUtils.isNotEmpty(s))
        {
            String[] s1 = s.split(" ");
            if (s1.length>1) {
                return s1[0].replace("-","");
            }
            else{
                return "unknow";
            }

        }
        else{
            return "unknow";
        }

    }

    public static String fmtTime(String s){
        if (StringUtils.isNotEmpty(s))
        {
            String[] s1 = s.split(" ");
            if (s1.length>1) {
                return s1[1].substring(0,2);
            }
            else{
                return "unknow";
            }

        }
        else{
            return "unknow";
        }
    }
}
