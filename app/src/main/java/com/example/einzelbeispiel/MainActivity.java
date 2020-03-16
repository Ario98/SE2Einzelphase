package com.example.einzelbeispiel;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView displayResult = (TextView) findViewById(R.id.textView3);

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
                String recievedInput = dataExchange();
                displayResult.setText(recievedInput);
            }
        });
    }


    public void calculateDivisors() {
        EditText matrikelNummer = (EditText) findViewById(R.id.Matrikelnummer);
        TextView displayResult = (TextView) findViewById(R.id.textView4);
        String mNummer = matrikelNummer.getText().toString();

        //array creation
        int length = mNummer.length();
        int[] arr = new int[length];

        for (int i = 0; i < length; i++) {
            arr[i] = Character.getNumericValue(mNummer.charAt(i));
        }

        int randomIndexOne = new Random().nextInt(arr.length);
        int randomIndexTwo = new Random().nextInt(arr.length);

        while (randomIndexOne == randomIndexTwo) {
            randomIndexTwo = new Random().nextInt(arr.length);
        }

        int num1 = arr[randomIndexOne];
        int num2 = arr[randomIndexTwo];
        int gcd = 1;

        for (int i = 1; i <= num1 && i <= num2; i++) {
            if (num1 % i == 0 && num2 % i == 0)
                gcd = i;
        }

        if (gcd > 1) {
            displayResult.setText("The indexes of the randomly picked numbers that have the GCM > 1 are" + " " + randomIndexOne + " " + randomIndexTwo);
        } else {
            displayResult.setText("The two random picked numbers do not have a GCM >1. The indexes of picked numbers are" + " " + randomIndexOne + " " + randomIndexTwo);
        }
    }

    String serverResponse;
    private String dataExchange() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    EditText matrikelNummer = (EditText) findViewById(R.id.Matrikelnummer); //two times declared-change

                    Socket s = new Socket("se2-isys.aau.at", 53212);
                    OutputStream output = s.getOutputStream();

                    PrintWriter printWriter = new PrintWriter(output, true);
                    printWriter.println(matrikelNummer.getText().toString());

                    //answer from server
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                    serverResponse = bufferedReader.readLine();


                    //close and release system resources
                    printWriter.close();
                    output.close();
                    s.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return serverResponse;
    }
}
