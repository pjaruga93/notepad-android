package com.example.notepad;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    static ArrayList<String> notes = new ArrayList<>();
    static ArrayAdapter arrayAdapter;

    int userId;
    String authorId;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if (item.getItemId() == R.id.add_note) {

            Intent intent = new Intent(getApplicationContext(), AddNoteActivity.class);
            Bundle x = new Bundle();
            x.putInt("userId", userId);
            intent.putExtras(x);
            startActivity(intent);

            return true;

        }

        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authorId = String.valueOf(getIntent().getExtras().getInt("userId"));

        ListView listView = (ListView) findViewById(R.id.listView);

        if(!authorId.equals("")) {
            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    String[] field = new String[1];
                    field[0] = "author";
                    String[] data = new String[1];
                    data[0] = authorId;
                    PutData putData = new PutData("http://192.168.1.64/LoginRegister/getNotes.php", "POST", field, data);
                    if (putData.startPut()) {
                        if (putData.onComplete()) {
                            String[][] allNotes = {{putData.getResult()}};
                            if(allNotes.length != 0){ // sprawdzic ten warunek bo napieprza milion razy w apce
                                Toast.makeText(getApplicationContext(), "Notes downloaded", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                Bundle x = new Bundle();
                                x.putInt("userId", userId);
                                intent.putExtras(x);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                Toast.makeText(getApplicationContext(), "No notes", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepad", Context.MODE_PRIVATE);

        HashSet<String> set = (HashSet<String>) sharedPreferences.getStringSet("allNotes", null);

        if (set == null) {
            // tutaj wyswietlic notatke?
            allNotes.add("Example note");

        } else {
// tutaj wyswietlic notatke?
            allNotes = new ArrayList<>(set);

        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, notes); // cos w ten desen

        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Intent intent = new Intent(getApplicationContext(), VerifyNotePassword.class);
                intent.putExtra("noteId", i);
                Bundle x = new Bundle();
                x.putInt("userId", userId);
                x.putInt("noteId", i);
                intent.putExtras(x);
                startActivity(intent);

            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final int itemToDelete = i;

                Intent intent = new Intent(getApplicationContext(), VerifyNotePassword.class);
                intent.putExtra("noteId", i);
                startActivity(intent);

                new AlertDialog.Builder(MainActivity.this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Are you sure?")
                        .setMessage("This note will be deleted permamently.")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                notes.remove(itemToDelete);
                                arrayAdapter.notifyDataSetChanged();

                                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.example.notepad", Context.MODE_PRIVATE);

                                HashSet<String> set = new HashSet<>(MainActivity.notes);

                                sharedPreferences.edit().putStringSet("notes", set).apply();

                            }
                        })

                        .setNegativeButton("No", null)
                        .show();

                return true;
            }
        });
    }
}