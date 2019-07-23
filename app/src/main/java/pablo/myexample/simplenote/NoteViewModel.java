package pablo.myexample.simplenote;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mRepository;

    private LiveData<List<Note>> mAllNotes;

    public NoteViewModel (Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getmAllNotes();
    }

    LiveData<List<Note>> getAllNotes() { return mAllNotes; }

    public void insert(Note note) { mRepository.insert(note); }

    public void update(Note note) {
        mRepository.update(note);
    }

    public void delete(Note note) {
        mRepository.delete(note);
    }

}