package ir.android.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;


public class TfTextview extends TextView {

    private Typeface tf;
    private Context context;

    public TfTextview(Context context) {
        super(context);
        this.context = context;
        initTypeface();
    }

    public TfTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initTypeface();
    }

    public TfTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initTypeface();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TfTextview(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        initTypeface();
    }

    private void initTypeface() {
        tf = Typeface.createFromAsset(context.getAssets(), "IranSans.ttf");
        this.setTypeface(tf);
    }
}
