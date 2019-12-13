package com.example.scrabblegueraultblanquet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[] {Manifest.permission.READ_SMS}, 1);
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                char[] ch = {'b', 'j', 'o', 'o', 'n', 'r', '*'};
                ScrabbleComparator sc = new ScrabbleComparator(ch);
                Dictionary dic = new Dictionary(getApplicationContext());
                List<String> word = dic.getWordsThatCanBeComposed(ch);
                String[] newWord = word.toArray(new String[0]);
                Log.i("Scrabble", String.valueOf(word));
                Arrays.sort(newWord,sc);
                Log.i("Scrabble", String.valueOf(newWord));

            };
        });
        thread.run();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch(requestCode)
        {
            case 1:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast toast = Toast.makeText(getApplicationContext(),"Permission ReadSMS acceptée !",Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }
}
