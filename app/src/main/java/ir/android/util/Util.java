package ir.android.util;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Menu;
import android.view.View;

import ir.amin.vocabularymaneger.R;


public class Util {

    public static int getNavigationBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            return resources.getDimensionPixelSize(resourceId);
        }
        return 0;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getActionBarHeight(Context context) {
        TypedValue tv = new TypedValue();
        int result = 0;
        if (context.getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
            result = TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        }
        return result;
    }

    public static int getDeviceHeight(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int screenh = display.getHeight() - (9 * getStatusBarHeight(activity));
        return screenh;
    }

    public static int getDeviceWidth(Activity activity) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        int width = display.getHeight();
        return width;
    }

    public static int dpToPx(Context context, int dp) {
        int px = Math.round(dp * getPixelScaleFactor(context));
        return px;
    }

    public static int pxToDp(Context context, int px) {
        int dp = Math.round(px / getPixelScaleFactor(context));
        return dp;
    }

    private static float getPixelScaleFactor(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static class CustomizeDrawerMenuTypFace {

        public static void applyFontToMenuItems(Menu navMenu, Context context) {
            for (int i = 0; i < navMenu.size(); i++) {
                Typeface font = Typeface.createFromAsset(context.getAssets(), "IranSans.ttf");
                SpannableString mNewTitle = new SpannableString(navMenu.getItem(i).getTitle());
                mNewTitle.setSpan(new CustomTypefaceSpan("", font), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
                navMenu.getItem(i).setTitle(mNewTitle);
            }
        }

        static class CustomTypefaceSpan extends TypefaceSpan {

            private final Typeface newType;

            public CustomTypefaceSpan(String family, Typeface type) {
                super(family);
                newType = type;
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                applyCustomTypeFace(ds, newType);
            }

            @Override
            public void updateMeasureState(TextPaint paint) {
                applyCustomTypeFace(paint, newType);
            }

            private static void applyCustomTypeFace(Paint paint, Typeface tf) {
                int oldStyle;
                Typeface old = paint.getTypeface();
                if (old == null) {
                    oldStyle = 0;
                } else {
                    oldStyle = old.getStyle();
                }

                int fake = oldStyle & ~tf.getStyle();
                if ((fake & Typeface.BOLD) != 0) {
                    paint.setFakeBoldText(true);
                }

                if ((fake & Typeface.ITALIC) != 0) {
                    paint.setTextSkewX(-0.25f);
                }
                paint.setTypeface(tf);
            }
        }
    }
}
