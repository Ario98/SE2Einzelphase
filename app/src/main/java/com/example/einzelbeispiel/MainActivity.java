package com.example.einzelbeispiel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button calculateButton = (Button) findViewById(R.id.buttonCalculate);
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateDivisors();
            }
        });

        Button sendButton = (Button) findViewById(R.id.button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendData();
            }
        });
    }

    public void calculateDivisors() {
        EditText matrikelNummer = (EditText) findViewById(R.id.Matrikelnummer);
        TextView displayResult = (TextView) findViewById(R.id.textView4);
        String mNummer = matrikelNummer.getText().toString();
        int length = mNummer.length();

        int[] arr = new int[length];

        for(int i=0;i<length;i++) {
            arr[i] = Character.getNumericValue(mNummer.charAt(i));
        }

        int num1 = arr[0];
        int num2 = arr[1];
        int gcd = 1;

        for(int i = 1; i <= num1 && i <= num2; i++)
        {
            if(num1%i==0 && num2%i==0)
                gcd = i;
        }

        if (gcd > 1) {
            displayResult.setText("The indexes of the numbers are 1 and 2");
    } else {
            displayResult.setText("Nichts");
        }
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
