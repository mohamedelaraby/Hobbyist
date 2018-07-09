package com.project.android.hobbyist.Utils;

/**
 * Created by hassa on 7/8/2018.
 */

public  class StringManipulation {
    public static String expandUsername(String username){
        return username.replace(".", " ");
    }

    public static String codenseUsername(String username){
        return username.replace(" ", ".");
    }

}
