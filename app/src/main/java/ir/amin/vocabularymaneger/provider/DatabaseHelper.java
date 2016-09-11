package ir.amin.vocabularymaneger.provider;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by amin on 9/8/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "VOCABULARY_MANAGER";
    public static final int DATABASE_VERSION = 1;

    private String tableName;


    public DatabaseHelper(Context context, String tableName) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.tableName = tableName;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query =
                " CREATE TABLE " + this.tableName +
                        " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        " value TEXT NOT NULL UNIQUE, " +
                        " translate TEXT NOT NULL, " +
                        " definition TEXT NOT NULL, " +
                        " synonym TEXT NOT NULL, " +
                        " opposite TEXT NOT NULL, " +
                        " example TEXT NOT NULL);";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + this.tableName);
        onCreate(sqLiteDatabase);
    }
}
