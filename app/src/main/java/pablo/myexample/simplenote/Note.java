package pablo.myexample.simplenote;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {
    
    @NonNull
    @PrimaryKey
    private String title;

    private String description;

    private String noteTitle;

    public Note(String title, String description, String noteTitle) {
        this.title = title;
        this.description = description;
        this.noteTitle = noteTitle;
    }

    public String getTitle(){return this.title;}

    public String getDescription(){return this.description;}

    public String getNoteTitle() {
        return noteTitle;
    }
}
