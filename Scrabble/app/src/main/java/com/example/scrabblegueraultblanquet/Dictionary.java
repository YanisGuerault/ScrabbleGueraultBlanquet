package com.example.scrabblegueraultblanquet;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import static java.util.Arrays.binarySearch;
import static java.util.Arrays.sort;

public class Dictionary {

    public String[] wordList;

    public Dictionary (Context C, String filename ){
        InputStream ips = C.getResources().openRawResource(R.raw.frutf8);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String line;
        try {
            while ((line = br.readLine()) != null) {
                Log.e("contenu", " : " + line);
            }
        } catch (Exception  e) {
            System.out.println("Error " + e.getMessage());
        }
    }

    public boolean isValidWord( String word ) {
        sort(wordList);
        return binarySearch(wordList, word) >= 0;
    }
}
