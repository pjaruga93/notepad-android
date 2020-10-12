package com.example.notepad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class AddNoteActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextTitle, textInputEditTextContent, textInputEditTextNotePassword;
    Button btnSaveNote;
    int userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        textInputEditTextTitle = findViewById(R.id.editTitle);
        textInputEditTextContent = findViewById(R.id.editContent);
        textInputEditTextNotePassword = findViewById(R.id.notePassword);
        btnSaveNote = findViewById(R.id.btnSaveNote);

        userId = getIntent().getExtras().getInt("userId");

        // 1. zrobic czesc do wysylania danych (id), na podst ktorej beda pobierane notatki (w odp. lista notatek danego usera, wrzucic ja do seta)

        btnSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title, author, content, notePassword;
                title = String.valueOf(textInputEditTextTitle.getText());
                author = String.valueOf(userId);
                content = String.valueOf(textInputEditTextContent.getText());
                notePassword = String.valueOf(textInputEditTextNotePassword.getText());


                if(!title.equals("") && !content.equals("") && !notePassword.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[4];
                            field[0] = "title";
                            field[1] = "author";
                            field[2] = "content";
                            field[3] = "notePassword";
                            String[] data = new String[4];
                            data[0] = title;
                            data[1] = author;
                            data[2] = content;
                            data[3] = notePassword;
                            PutData putData = new PutData("http://192.168.1.64/LoginRegister/addNote.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    if(result.equals("Note has been added")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        Bundle x = new Bundle();
                                        x.putInt("userId", userId);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}