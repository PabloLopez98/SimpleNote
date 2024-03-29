package pablo.myexample.simplenote;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    public static final int NEW_WORD_ACTIVITY_REQUEST_CODE = 1;
    public static final int EDIT_WORD_ACTIVITY_REQUEST_CODE = 2;
    public static final int REQUEST_MICROPHONE = 1;

    private NoteViewModel mNoteViewModel;
    private NoteListAdapter adapter;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Note note = new Note(data.getStringExtra(TypeNote.EXTRA_TITLE), data.getStringExtra(TypeNote.EXTRA_DESCRIPTION),data.getStringExtra(TypeNote.EXTRA_NOTE_TITLE));
            mNoteViewModel.insert(note);
            Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG).show();
        }else if(requestCode == EDIT_WORD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            String title = data.getStringExtra(TypeNote.EXTRA_TITLE);
            String description = data.getStringExtra(TypeNote.EXTRA_DESCRIPTION);
            String noteTitle = data.getStringExtra(TypeNote.EXTRA_NOTE_TITLE);
            Note note = new Note(title, description,noteTitle);
            mNoteViewModel.update(note);
            Toast.makeText(this, "Note updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Not Saved", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkMicPermssion();

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //final NoteListAdapter
        adapter = new NoteListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable final List<Note> notes) {
                adapter.setmNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                mNoteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(MainActivity.this, TypeNote.class);
                intent.putExtra(TypeNote.EXTRA_TITLE, note.getTitle());
                intent.putExtra(TypeNote.EXTRA_NOTE_TITLE, note.getNoteTitle());
                intent.putExtra(TypeNote.EXTRA_DESCRIPTION, note.getDescription());
                intent.putExtra(TypeNote.EXTRA_ID,note.getTitle());
                startActivityForResult(intent, EDIT_WORD_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    private void checkMicPermssion() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    REQUEST_MICROPHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_MICROPHONE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission Granted to record audio", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Permissions Denied to record audio", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }


    public void toTypeNote(View view) {
        Intent intent = new Intent(MainActivity.this, TypeNote.class);
        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
    }
}
