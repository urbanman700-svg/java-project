package com.kd.note;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NoteAdapter.OnNoteClickListener {

    public static final String EXTRA_TITLE = "extra_title";
    public static final String EXTRA_CONTENT = "extra_content";

    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;

    private final ActivityResultLauncher<Intent> addNoteLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String title = result.getData().getStringExtra(EXTRA_TITLE);
                    String content = result.getData().getStringExtra(EXTRA_CONTENT);
                    if (title != null && !title.trim().isEmpty()) {
                        noteList.add(0, new Note(title, content));
                        noteAdapter.notifyItemInserted(0);
                        recyclerView.scrollToPosition(0);
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteList = new ArrayList<>();
        noteAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(noteAdapter);

        FloatingActionButton fab = findViewById(R.id.fab_add_note);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
            addNoteLauncher.launch(intent);
        });
    }

    @Override
    public void onDeleteClick(int position) {
        if (position < 0 || position >= noteList.size()) return;
        noteList.remove(position);
        noteAdapter.notifyItemRemoved(position);
        noteAdapter.notifyItemRangeChanged(position, noteList.size());
        Toast.makeText(this, "Note deleted", Toast.LENGTH_SHORT).show();
    }
}
