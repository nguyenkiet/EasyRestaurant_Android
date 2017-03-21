package com.example.claudiochicodev.easyrestaurant;

import android.content.Context;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.View;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

public class SharedResources {

    public static String UserName = "";
    public static String HashedPassword = "";
    public static String coin = "£";
    public static MyDBHandler dbHandler;
    public static List<Menus> menuList;
    public static HashMap<String, Object> gridMap;
    public static int tableNumber = 0;
    private static String salt = "ThisSaltWillBeChangedOnTheFinalProduct__OfCourse:1927481927%$&/%&/%%%%)(/(";
    private static int encryptIterations = 9998;

    public SharedResources(Context context) {
        gridMap = new HashMap<String, Object>();
        dbHandler = new MyDBHandler(context, null, 1);
    }

    public static String hashString(String str) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (md != null) {
            md.update(str.getBytes());
            byte byteData[] = md.digest();
            String base64 = Base64.encodeToString(byteData, Base64.DEFAULT);
            for (int i = 0 ; i < encryptIterations ; i++){
                base64 += salt + (i-25);
                md.update(base64.getBytes());
                byteData = md.digest();
                base64 = Base64.encodeToString(byteData, Base64.DEFAULT);
            }
            return base64;
        }
        return str;
    }

    public static int pxToDp(View v, int px){
        float dp = v.getResources().getDisplayMetrics().density*px;
        return Math.round(dp);
    }

    public static int dpTopx(View v, int dp) {
        DisplayMetrics displayMetrics = v.getResources().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }



}
