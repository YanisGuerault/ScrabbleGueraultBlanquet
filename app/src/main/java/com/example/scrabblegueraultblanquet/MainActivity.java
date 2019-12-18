package com.example.scrabblegueraultblanquet;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.content.ContextCompat;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private static final Handler handler = new Handler();
    private boolean threadSearchEnable = false;
    private static Dictionary dic;
    private ProgressBar progress;
    private EditText edit;
    private Button button;
    ArrayList<HashMap<String, String>> mainList = new ArrayList<>(0);
    SimpleAdapter adapter;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        themeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progress = findViewById(R.id.progressBar);
        edit = findViewById(R.id.edit);
        button = findViewById(R.id.button);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_SMS}, 1);
        }

        if (dic == null) {
            handler.postDelayed(threadDico, 1000);
            loading();
        }

        setListView();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!threadSearchEnable) {
                    EditText myEdit = findViewById(R.id.edit);
                    MyTask task = new MyTask();
                    task.execute(myEdit.getText().toString().toCharArray());
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "La recherche est déjà en cours, vous ne pouvez pas la lancer deux fois", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private final Runnable threadDico = new Runnable() {
        @Override
        public void run() {
            dic = new Dictionary(getApplicationContext());
            endLoading();
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

    private void loading() {
        progress.setVisibility(View.VISIBLE);
        edit.setEnabled(false);
    }

    private void endLoading() {
        progress.setVisibility(View.GONE);
        edit.setEnabled(true);
    }

    void setListView() {
        ListView listView = findViewById(R.id.myList);

        SimpleAdapter contact_adaptater = new SimpleAdapter(getApplicationContext(), mainList, R.layout.item_entry, new String[]{"word", "compose", "value"}, new int[]{R.id.word, R.id.compose, R.id.value});
        listView.setAdapter(contact_adaptater);
        adapter = contact_adaptater;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.classic:
                themeUtils.THEME = R.style.ThemeClassic;
                restartActivity();
                return true;
            case R.id.sombre:
                themeUtils.THEME = R.style.ThemeSombre;
                restartActivity();
                return true;
            case R.id.autre:
                themeUtils.THEME = R.style.ThemeAutre;
                restartActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void restartActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private class MyTask extends AsyncTask<char[], Void, Integer> {

        @Override
        protected void onPreExecute() {
            loading();
            threadSearchEnable = true;
            mainList.clear();
            adapter.notifyDataSetChanged();
            TextView tv = findViewById(R.id.nbmots);
            tv.setText("");
        }


        @Override
        protected void onPostExecute(Integer integer) {
            endLoading();
            threadSearchEnable = false;
            TextView tv = findViewById(R.id.nbmots);
            tv.setText("Nombres de mots : " + integer);
            adapter.notifyDataSetChanged();
        }

        @Override
        protected Integer doInBackground(char[]... voids) {
            EditText myEdit = findViewById(R.id.edit);
            char[] letters = voids[0];

            List<String> vocabList = dic.getWordsThatCanBeComposed(letters);
            ScrabbleComparator sc = new ScrabbleComparator(letters);

            Data[] list = new Data[vocabList.size()];

            int i = 0;
            for (String s : vocabList) {
                list[i] = new Data(s, Dictionary.getComposition(s, letters));
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

            return vocabList.size();
        }
    }
}
