package com.example.scrabblegueraultblanquet;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.content.ContextCompat;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.Normalizer;
import java.util.LinkedList;
import java.util.List;

import static java.util.Arrays.binarySearch;
import static java.util.Arrays.sort;

public class Dictionary {

    public String[] wordList;

    public Dictionary(Context C) {

        InputStream ips = C.getResources().openRawResource(R.raw.frutf8);
        InputStreamReader ipsr = new InputStreamReader(ips);
        BufferedReader br = new BufferedReader(ipsr);
        String line;
        try {
            line = br.readLine();
            //int max = 50000;
            wordList = new String[Integer.parseInt(line)];
            int i = 0;

            while ((line = br.readLine()) != null /*&& i < max*/) {
                wordList[i] = line;
                i++;
            }

            sort(wordList);
        } catch (Exception e) {
            Log.e("Scrabble","Error : "+e.getMessage());
        }
    }

    public boolean isValidWord(String word) {
        return binarySearch(wordList, word) >= 0;
    }

    public static boolean mayBeComposed(String word, char[] letters) {

        if(word.length() > letters.length)
            return false;

        LinkedList<Character> list = new LinkedList<Character>();
        LinkedList<Character> tabChar = new LinkedList<Character>();

        int nbJoker = 0;

        word = replaceFrenchCharacter(word);
        letters = replaceFrenchCharacter(String.valueOf(letters)).toCharArray();

        for (int i = 0; i < word.length(); i++) {
            tabChar.add(word.charAt(i));
        }

        for (char c : letters) {
            if(c == '*')
                nbJoker++;
            list.add(c);
        }

        for (char ch : list) {
            if (tabChar.contains(ch)) {
                tabChar.remove(Character.valueOf(ch));
            }
        }

        if(tabChar.size()-nbJoker <= 0)
            return true;
        else
            return false;
    }

    public static char[] getComposition(String word, char[] letters)
    {
        LinkedList<Character> list = new LinkedList<Character>();
        LinkedList<Character> tabChar = new LinkedList<Character>();

        word = replaceFrenchCharacter(word);
        letters = replaceFrenchCharacter(String.valueOf(letters)).toCharArray();

        for (char c : letters) {
            list.add(c);
        }

        for (char ch : word.toCharArray()) {
            if (list.contains(ch)) {
                tabChar.add(ch);
                list.remove(Character.valueOf(ch));
            }
        }

        char[] array = new char[word.length()];
        int i = 0;
        for(char ch : tabChar)
        {
            array[i] = ch;
            i++;
        }

        for(; i < word.length(); i++)
        {
            array[i] = '*';
        }

        return array;
    }

    public List<String> getWordsThatCanBeComposed(char[] letters) {
        LinkedList<String> result = new LinkedList<>();
        for (String word : wordList) {
            if (mayBeComposed(word, letters))
                result.add(word);
        }

        return result;
    }

    public static String replaceFrenchCharacter(String s) {
        s = s.toLowerCase();
        s = Normalizer.normalize(s,Normalizer.Form.NFD);
        return s.replaceAll("[\\p{InCOMBINING_DIACRITICAL_MARKS}]","");
    }

    public static char replaceFrenchCharacter(char s) {
        String n = String.valueOf(s);
        n = replaceFrenchCharacter(n);
        return n.charAt(0);
    }
}
