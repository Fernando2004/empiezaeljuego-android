package es.uah.cc.todomanager;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import es.uah.cc.todomanager.R;

public class ContactActivity extends AppCompatActivity implements SendMessageDialog.OnSendMessageListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onContinue() {
        finish();
    }

    @Override
    public void onCancel() {
// Nothing to do.
    }
}

