package ir.amin.vocabularymaneger.webservise;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import ir.amin.vocabularymaneger.entities.VocabManeger;
import ir.amin.vocabularymaneger.entities.Word;

/**
 * Created by amin on 9/11/16.
 */

public class WebService {

    private static WebService webService;
    private Activity activity;
    private ProgressDialog progress;

    public static WebService newInstance(Activity activity) {
        if(webService == null) {
            webService = new WebService(activity);
        }
        return webService;
    }

    private WebService(Activity activity) {
        this.activity = activity;
    }

    public void sync() {
        Log.d("sync", "sync");
        AsyncTask ast = new AsyncTask<String, Void, Void>() {
            @Override
            protected void onPreExecute() {
                Log.d("sync", "onPreExecute");
                progress = new ProgressDialog(activity);
                progress.setMessage("Please  wait...");
                progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progress.setIndeterminate(true);
                progress.show();
            }

            @Override
            protected Void doInBackground(String... params) {
                Log.d("sync", "doInBackground");
                InputStream input = null;
                HttpURLConnection connection = null;
                try {
                    URL url = new URL("http://192.168.1.3/vocabulaymanager/vocab.vocab");
                    connection = (HttpURLConnection) url.openConnection();
                    connection.connect();

                    int fileLength = connection.getContentLength();
                    Log.d("FILELWNGTH", fileLength+"");
                    // download the file
                    input = connection.getInputStream();

                    VocabManeger vb = new VocabManeger(input);
                    Word[] words = vb.getAllWords();
                    ((OnDownloadComplete)activity).onDownloadComplete(words);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                Log.d("sync", "onPostExecute");
                super.onPostExecute(aVoid);
                progress.cancel();
            }
        };
        ast.execute(new String[]{});
    }
}
