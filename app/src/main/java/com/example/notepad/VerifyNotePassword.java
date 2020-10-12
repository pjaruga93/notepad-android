package com.example.notepad;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class VerifyNotePassword extends AppCompatActivity {
    TextInputEditText textInputEditTextVerifyNotePassword;
    Button btnConfirmPassword;

    int noteId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_note_password);

        textInputEditTextVerifyNotePassword = findViewById(R.id.verifyNotePassword);
        btnConfirmPassword = findViewById(R.id.btnConfirmNotePassword);

        btnConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String  verifyNotePassword ;
                verifyNotePassword = String.valueOf(textInputEditTextVerifyNotePassword.getText());

                if(!verifyNotePassword.equals("")) {
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[1];
                            field[0] = "notePassword";
                            String[] data = new String[1];
                            data[0] = verifyNotePassword;
                            PutData putData = new PutData("http://192.168.1.64/LoginRegister/verifyNotePassword.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    noteId = Integer.parseInt(result);
                                    if(result.equals(verifyNotePassword)){
                                        Toast.makeText(getApplicationContext(), "Verification successful", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), NoteEditorActivity.class);
                                        intent.putExtra("noteId", noteId);
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
                    Toast.makeText(getApplicationContext(), "Wrong password!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}