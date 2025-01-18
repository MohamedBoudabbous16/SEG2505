package com.example.lemonade.ui.theme;

import android.graphics.Color;

public class ThemeColors {

    public static final int md_theme_light_primary = rgb(0x6A, 0x5F, 0x00);
    public static final int md_theme_light_onPrimary = rgb(0xFF, 0xFF, 0xFF);
    public static final int md_theme_light_primaryContainer = rgb(0xF9, 0xE4, 0x4C);
    public static final int md_theme_light_onPrimaryContainer = rgb(0x20, 0x1C, 0x00);
    public static final int md_theme_light_secondary = rgb(0x64, 0x5F, 0x41);
    public static final int md_theme_light_onSecondary = rgb(0xFF, 0xFF, 0xFF);
    // Add more light theme colors as needed

    public static final int md_theme_dark_primary = rgb(0xDC, 0xC8, 0x30);
    public static final int md_theme_dark_onPrimary = rgb(0x37, 0x31, 0x00);
    public static final int md_theme_dark_primaryContainer = rgb(0x50, 0x47, 0x00);
    public static final int md_theme_dark_onPrimaryContainer = rgb(0xF9, 0xE4, 0x4C);
    public static final int md_theme_dark_secondary = rgb(0xCF, 0xC7, 0xA2);
    public static final int md_theme_dark_onSecondary = rgb(0x35, 0x31, 0x17);
    // Add more dark theme colors as needed

    public static int rgb(int red, int green, int blue) {
        return (0xFF << 24) | (red << 16) | (green << 8) | blue;
    }
}
