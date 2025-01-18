package com.example.lemonade.ui.theme;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.view.View;
import android.view.Window;

public class Theme {

    private static final ColorScheme LightColorScheme = new ColorScheme(
            ThemeColors.md_theme_light_primary,
            ThemeColors.md_theme_light_onPrimary,
            ThemeColors.md_theme_light_primaryContainer,
            ThemeColors.md_theme_light_onPrimaryContainer,
            ThemeColors.md_theme_light_secondary,
            ThemeColors.md_theme_light_onSecondary
            // Add more light theme colors as needed
    );

    private static final ColorScheme DarkColorScheme = new ColorScheme(
            ThemeColors.md_theme_dark_primary,
            ThemeColors.md_theme_dark_onPrimary,
            ThemeColors.md_theme_dark_primaryContainer,
            ThemeColors.md_theme_dark_onPrimaryContainer,
            ThemeColors.md_theme_dark_secondary,
            ThemeColors.md_theme_dark_onSecondary
            // Add more dark theme colors as needed
    );

    public static ColorScheme getColorScheme(Context context) {
        boolean isDarkTheme = (context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
        return isDarkTheme ? DarkColorScheme : LightColorScheme;
    }

    public static void applyTheme(Activity activity) {
        ColorScheme colorScheme = getColorScheme(activity.getApplicationContext());
        Window window = activity.getWindow();
        View decorView = window.getDecorView();

        // Set the status bar color
        window.setStatusBarColor(colorScheme.primary);

        // Set light or dark status bar icons based on the theme
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = decorView.getSystemUiVisibility();
            if (colorScheme == DarkColorScheme) {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(flags);
        }
    }

    public static class ColorScheme {
        public final int primary;
        public final int onPrimary;
        public final int primaryContainer;
        public final int onPrimaryContainer;
        public final int secondary;
        public final int onSecondary;
        // Add additional colors as needed

        public ColorScheme(int primary, int onPrimary, int primaryContainer, int onPrimaryContainer,
                           int secondary, int onSecondary) {
            this.primary = primary;
            this.onPrimary = onPrimary;
            this.primaryContainer = primaryContainer;
            this.onPrimaryContainer = onPrimaryContainer;
            this.secondary = secondary;
            this.onSecondary = onSecondary;
            // Initialize additional colors as needed
        }
    }
}
