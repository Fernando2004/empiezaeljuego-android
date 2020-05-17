package es.uah.cc.todomanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;


/**
 * Una actividad que representa una lista de tareas.
 * @author Fernando García Molino Ejr.de Arturo
 * @version 1.0
 */
public class HelpActivity extends AppCompatActivity {

    private static final String BASE_URL = "file:///android_asset/html/";
    private static  final String HELP_FILE = "help.html";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        // Loads the hel.html local file.
        WebView webView = (WebView) findViewById(R.id.help_view);
        webView.loadUrl(BASE_URL + HELP_FILE);
    }
}
