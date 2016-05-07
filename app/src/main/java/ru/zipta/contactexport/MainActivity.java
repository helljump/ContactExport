package ru.zipta.contactexport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            ContactDumpReceiver.dumpContacts(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
