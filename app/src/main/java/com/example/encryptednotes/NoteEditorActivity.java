package com.example.encryptednotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NoteEditorActivity extends AppCompatActivity {

    int noteIndex;
    Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        EditText contentText = (EditText) findViewById(R.id.contentText),
                titleText = (EditText) findViewById(R.id.titleText);

        Intent intent = getIntent();
        noteIndex = intent.getIntExtra("noteId", -1);

        if (noteIndex == -1) { // create new note
            note = new Note("", "");
            MainActivity.notes.add(note.getId());
            noteIndex = MainActivity.notes.size() - 1;
            MainActivity.adapter.notifyItemInserted(noteIndex);
        } else {            // get existing note
            String fileId = MainActivity.notes.get(noteIndex);
            note = Note.loadNote(this, fileId);
        }

        // set title and content
        titleText.setText(note.getTitle());
        contentText.setText(note.getContent());

        // deal with changes to title
        titleText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                note.setTitle(String.valueOf(charSequence));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });

        // deal with changes to content
        contentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                note.setContent(String.valueOf(charSequence));
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {}
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        note.saveNote(this);
    }
}