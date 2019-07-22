package pablo.myexample.simplenote;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "note_table")
public class Note {

    @PrimaryKey
    @NonNull
    private String title;

    private String description;

    public Note(String title, String description) {this.title = title;this.description = description;}

    public String getTitle(){return this.title;}

    public String getDescription(){return this.description;}
}
