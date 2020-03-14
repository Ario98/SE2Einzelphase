package com.example.einzelbeispiel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }


    public void sendData() {
        EditText matrikelNummer = (EditText) findViewById(R.id.Matrikelnummer);
        String output = matrikelNummer.getText().toString();

        BackgroundTask b1 = new BackgroundTask();
        b1.execute(output);

    }

    class BackgroundTask extends AsyncTask<String,Void,Void> {

        Socket s;
        PrintWriter writer;

        @Override
        protected Void doInBackground(String... voids) {
        try {
            String output = voids[0];
            s = new Socket("se2-isys.aau.at", 53212);
            writer = new PrintWriter(s.getOutputStream());
            writer.write(output);
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
        }
    }
}
