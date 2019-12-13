package com.example.scrabblegueraultblanquet;

import android.provider.ContactsContract;

import java.util.Comparator;
import java.util.Date;

public class ScrabbleComparator implements Comparator<String> {

    char[] letters;

    public ScrabbleComparator(char[] letters) {
        this.letters = letters;
    }

    public int wordValue(String word) {
        return lettersValue(Dictionary.getComposition(word,letters));
    }

    public static int letterValue(char letter) {
        letter = Dictionary.replaceFrenchCharacter(letter);
        char[] pt1 = {'e', 'a', 'i', 'n', 'o', 'r', 's', 't', 'u', 'l'};
        char[] pt2 = {'d', 'm', 'g'};
        char[] pt3 = {'b', 'c', 'p'};
        char[] pt4 = {'f', 'h', 'v'};
        char[] pt8 = {'j', 'q'};
        char[] pt10 = {'k', 'w', 'x', 'y', 'z'};

        if (String.valueOf(pt1).contains("" + letter))
            return 1;
        else if (String.valueOf(pt2).contains("" + letter))
            return 2;
        else if (String.valueOf(pt3).contains("" + letter))
            return 3;
        else if (String.valueOf(pt4).contains("" + letter))
            return 4;
        else if (String.valueOf(pt8).contains("" + letter))
            return 8;
        else if (String.valueOf(pt10).contains("" + letter))
            return 10;
        return 0;
    }

    public static int lettersValue(char[] letters) {
        int nb = 0;
        for (char c : letters) {
            nb += letterValue(c);
        }
        return nb;
    }

    /*@Override
    public int compare(Data o1, Data o2) {
        int scoreO1 = wordValue(o1.compose);
        int scoreO2 = wordValue(o2.compose);

        if(scoreO1 < scoreO2)
        {
            return -1;
        }
        else if (scoreO1 > scoreO2)
        {
            return 1;
        }
        else
        {
           return 0;
        }

    }*/

    @Override
    public int compare(String o1, String o2) {
        int scoreO1 = wordValue(o1);
        int scoreO2 = wordValue(o2);

        if(scoreO1 < scoreO2)
        {
            return -1;
        }
        else if (scoreO1 > scoreO2)
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }
}
