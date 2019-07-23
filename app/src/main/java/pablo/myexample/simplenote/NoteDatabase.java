package pablo.myexample.simplenote;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Note.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract NoteDao noteDao();

    private static volatile NoteDatabase INSTANCE;

    static NoteDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (NoteDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            NoteDatabase.class, "note_database")
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback =
            new RoomDatabase.Callback(){

                @Override
                public void onOpen (@NonNull SupportSQLiteDatabase db){
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final NoteDao mDao;

        PopulateDbAsync(NoteDatabase db) {
            mDao = db.noteDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAllNotes();
            Note note = new Note("Hello","Description");
            mDao.insert(note);
            note = new Note("Hel","Des");
            mDao.insert(note);
            return null;
        }
    }
}
