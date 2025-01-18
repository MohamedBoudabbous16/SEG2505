package com.example.lemonade.ui.theme;

import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.TextAppearanceSpan;

public class Type {

    public static final TextAppearanceSpan bodyLarge = new TextAppearanceSpan("sans-serif", Typeface.NORMAL, 16, null, null);

    public static TextPaint getBodyLargeTextPaint() {
        TextPaint textPaint = new TextPaint();
        textPaint.setTextSize(16 * textPaint.density);
        textPaint.setTypeface(Typeface.create("sans-serif", Typeface.NORMAL));
        textPaint.setAntiAlias(true);
        return textPaint;
    }
}
