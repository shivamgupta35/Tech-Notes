package com.example.techandnotez4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTaker extends AppCompatActivity {
    EditText editText_title, editText_notes;
    ImageView imageView_save;
    NotesModel notesModel;
    Boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);
        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        imageView_save = findViewById(R.id.imageView_save);

        notesModel = new NotesModel();
        try {
            notesModel = (NotesModel) getIntent().getSerializableExtra("old_note");
            editText_title.setText(notesModel.getTitle());
            editText_notes.setText(notesModel.getNotes());
            isOldNote = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        imageView_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editText_title.getText().toString();
                String desc = editText_notes.getText().toString();
                if (title.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(NotesTaker.this, "Missing title or description", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("EEE, d MM yyyy HH:mm a");
                    Date date = new Date();
                    if (!isOldNote) {
                        notesModel = new NotesModel();
                    }
                    notesModel.setTitle(title);
                    notesModel.setNotes(desc);
                    notesModel.setDate(formatter.format(date));
                    Intent i = new Intent();
                    i.putExtra("notesModel", notesModel);
                    setResult(Activity.RESULT_OK, i);
                    finish();

                }
            }
        });
    }
}