package com.example.scrabblegueraultblanquet;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private static final Handler handler = new Handler();
    private static Dictionary dic;
    ArrayList<HashMap<String, String>> mainList = new ArrayList<>(0);
    SimpleAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, 1);
        }

        handler.postDelayed(Thread, 1000);
        setListView();
    }

    private final Runnable Thread = new Runnable() {
        @Override
        public void run() {
            char[] ch = {'c', 'l', 'a', 'v', 'i', 'r', '*'};
            ScrabbleComparator sc = new ScrabbleComparator(ch);
            dic = new Dictionary(getApplicationContext());
            /*List<String> word = dic.getWordsThatCanBeComposed(ch);
            String[] newWord = listToArray(word);
            Arrays.sort(newWord,sc);
            for(String s : newWord)
            {
                Log.i("Scrabble", s + " : " + sc.wordValue(s));
            }*/
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Permission ReadSMS acceptée !", Toast.LENGTH_LONG);
                    toast.show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    public static <T> String[] listToArray(List<T> list) {
        String[] array = new String[list.size()];
        for (int i = 0; i < array.length; i++)
            array[i] = list.get(i).toString();
        return array;
    }

    public void onSearch(View v) {
        EditText myEdit = findViewById(R.id.edit);
        char[] letters = myEdit.getText().toString().toCharArray();

        List<String> vocabList = dic.getWordsThatCanBeComposed(letters);
        ScrabbleComparator sc = new ScrabbleComparator(letters);

        Data[] list = new Data[vocabList.size()];

        int i = 0;
        for(String s : vocabList)
        {
            list[i] = new Data(s,Dictionary.getComposition(s,letters));
            i++;
        }

        Arrays.sort(list, sc);

        mainList.clear();

        for (Data data : list) {
            HashMap<String, String> nlist = new HashMap<>();
            nlist.put("word", data.word);
            nlist.put("compose", String.valueOf(data.compose));
            nlist.put("value", String.valueOf(ScrabbleComparator.lettersValue(data.compose)));
            mainList.add(nlist);
        }

        adapter.notifyDataSetChanged();

    }

    void setListView()
    {
        ListView listView = findViewById(R.id.myList);

        SimpleAdapter contact_adaptater = new SimpleAdapter(getApplicationContext(), mainList, R.layout.item_entry, new String[]{"word","compose","value"}, new int[]{R.id.word,R.id.compose,R.id.value});
        listView.setAdapter(contact_adaptater);
        adapter = contact_adaptater;
    }
}
