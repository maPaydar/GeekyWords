package ir.android.util;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    private Context context;
    private static SharedPreferences sharedPreferences;
    private static Preferences preferences;

    public int lastSeenWord;

    public static Preferences getInstance(Context context) {
        if(preferences == null) {
            sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
            preferences = new Preferences(context);
        }
        return preferences;
    }

    private Preferences(Context context) {
        this.context = context;
    }


    public void setLastSeenWord(int i) {
        this.lastSeenWord = i;
        SharedPreferences.Editor editor = this.sharedPreferences.edit();
        editor.putInt("lastSeenWord", this.lastSeenWord);
        editor.commit();
    }

    public int getLastSeenWord() {
        lastSeenWord = sharedPreferences.getInt("lastSeenWord", 0);
        return lastSeenWord;
    }
}
