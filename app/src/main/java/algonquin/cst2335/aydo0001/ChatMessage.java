package algonquin.cst2335.aydo0001;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class ChatMessage {

    @PrimaryKey (autoGenerate = true) //the database manages IDs
    @ColumnInfo(name="id")
    public long id;

    @ColumnInfo (name="Message")
    protected String message;

    @ColumnInfo(name="TimeSent")
    public String timeSent;

    @ColumnInfo(name="SendOrReceive")
    public boolean SendOrReceive;

    public ChatMessage () { }
    public ChatMessage(String m, String t, boolean isS) {
        message = m;
        timeSent = t;
        SendOrReceive = isS;
    }

}
