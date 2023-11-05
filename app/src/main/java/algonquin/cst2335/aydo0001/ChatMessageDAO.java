package algonquin.cst2335.aydo0001;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatMessageDAO {

    @Insert //compiler knows this is for inserting into database
    public long insertMessage(ChatMessage m);

    @Delete //compiler will generate "Delete from Table where id= ?"
    public void deleteMessage(ChatMessage m);

    @Query("Select * from ChatMessage") //search statement
    public List<ChatMessage> getAllMessages();
}
