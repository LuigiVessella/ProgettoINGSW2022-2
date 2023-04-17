package com.example.progettoingsw2022_2.Helper;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenSize {
    public enum screenSize{ SMALL ,MEDIUM , LARGE , XLARGE }
    public static screenSize screenWidth;
    public static screenSize screenHeight;

    public static void setScreenSize(Context context){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        if (width < 320) { screenWidth = screenSize.SMALL; }
        else if (width < 480) { screenWidth = screenSize.MEDIUM; }
        else if (width < 600) { screenWidth = screenSize.LARGE; }
        else screenWidth = screenSize.XLARGE;

        if (height < 320) { screenHeight = screenSize.SMALL; }
        else if (height < 480) { screenHeight = screenSize.MEDIUM; }
        else if (height < 600) { screenHeight = screenSize.LARGE; }
        else screenHeight = screenSize.XLARGE;
    }
}
