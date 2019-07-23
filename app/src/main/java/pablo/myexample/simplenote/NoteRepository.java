package pablo.myexample.simplenote;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {

    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    NoteRepository(Application application) {
        NoteDatabase db = NoteDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    LiveData<List<Note>> getmAllNotes() {
        return mAllNotes;
    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(mNoteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(mNoteDao).execute(note);
    }

    public void insert (Note note) {
        new insertAsyncTask(mNoteDao).execute(note);
    }

    private static class insertAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao mAsyncTaskDao;

        insertAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            return null;
        }
    }
}