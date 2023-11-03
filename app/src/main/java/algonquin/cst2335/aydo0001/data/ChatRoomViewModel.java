package algonquin.cst2335.aydo0001.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.aydo0001.ChatRoom;

public class ChatRoomViewModel extends ViewModel {

    public MutableLiveData<ArrayList<ChatRoom.ChatMessage>> messages = new MutableLiveData< >();

}


