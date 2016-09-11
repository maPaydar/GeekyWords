package ir.amin.vocabularymaneger.entities;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by amin on 9/8/16.
 */

public class Word {

    public String word, translate, definition, synonym, opposite, example;

    public Word(String word, String translate, String definition, String synonym, String opposite, String example) {
        this.word = word;
        this.translate = translate;
        this.definition = definition;
        this.synonym = synonym;
        this.opposite = opposite;
        this.example = example;
    }

    public static Word createFromCursor(Cursor c) {
        return new
                Word(
                c.getString(1),
                c.getString(2),
                c.getString(3),
                c.getString(4),
                c.getString(5),
                c.getString(6)
        );
    }

    public String[] getArray() {
        return new String[]{
                word,
                translate, definition, synonym, opposite, example
        };
    }

    public JSONObject getJSONWord() throws JSONException {
        JSONObject w = new JSONObject();
        w.put("value", word);
        w.put("translate", translate);
        w.put("definition", definition);
        w.put("synonym", synonym);
        w.put("opposite", opposite);
        w.put("example", example);
        return w;
    }

    public String getXmlWord() {
        String xmlWord = "";
        xmlWord += "<value>" + word + "</value>\n";
        xmlWord += "<translate>" + translate + "</translate>\n";
        xmlWord += "<definition>" + definition + "</definition>\n";
        xmlWord += "<synonym>" + synonym + "</synonym>\n";
        xmlWord += "<opposite>" + opposite + "</opposite>\n";
        xmlWord += "<example>" + example + "</example>\n";
        return xmlWord;
    }

    public ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put("value", word);
        values.put("translate", translate);
        values.put("definition", definition);
        values.put("synonym", synonym);
        values.put("opposite", opposite);
        values.put("example", example);
        return values;
    }
}