package com.example.scrabblegueraultblanquet;

import android.app.Activity;

import android.content.Intent;


public class themeUtils {

    public static int THEME = R.style.ThemeClassic;

    public static void applyTheme(Activity activity) {
        activity.getApplicationContext().setTheme(THEME);
        activity.setTheme(THEME);
    }
}

