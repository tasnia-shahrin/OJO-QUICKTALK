package com.example.ojoquicktalk;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class NotesActivity extends AppCompatActivity {

    FloatingActionButton addNoteBtn;

    RecyclerView recyclerView;

    NoteAdapter noteAdapter;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        addNoteBtn = findViewById(R.id.add_note_btn);
        recyclerView = findViewById(R.id.recyler_view);



        addNoteBtn.setOnClickListener((v)-> startActivity(new Intent(NotesActivity.this,NoteDetailsActivity.class)) );

        setupRecyclerView();
    }





    void setupRecyclerView(){
        Query query  = Utility.getCollectionReferenceForNotes().orderBy("timestamp",Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query,Note.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter(options,this);
        recyclerView.setAdapter(noteAdapter);


        }
       protected void onStart() {

           super.onStart();
           recyclerView.setAdapter(noteAdapter);
           noteAdapter.startListening();
       }

    @Override
    protected void onStop() {
        super.onStop();
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        recyclerView.setAdapter(noteAdapter);
        noteAdapter.notifyDataSetChanged();

    }
}



