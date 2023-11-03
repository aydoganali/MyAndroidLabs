package algonquin.cst2335.aydo0001;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import algonquin.cst2335.aydo0001.data.ChatRoomViewModel;
import algonquin.cst2335.aydo0001.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.aydo0001.databinding.ReceiveRowLayoutBinding;
import algonquin.cst2335.aydo0001.databinding.SentRowLayoutBinding;

public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> messages = new ArrayList<>();

    ActivityChatRoomBinding binding;
    private RecyclerView.Adapter myAdapter;
    ChatRoomViewModel chatModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //get the data from the ViewModel:


        binding.addButton.setOnClickListener( click ->{
            String messageText = binding.userMessage.getText().toString();
            ChatMessage sentMessage = new ChatMessage(messageText, true);
            messages.add(sentMessage);
            myAdapter.notifyItemInserted(messages.size()-1);//will redraw
            binding.userMessage.setText("");//remove what you typed
            //tell the recycle view to update:

        });

        binding.receiveButton.setOnClickListener( click ->{
            String messageText = binding.userMessage.getText().toString();
            ChatMessage receivedMessage = new ChatMessage(messageText, false);
            messages.add(receivedMessage);
            myAdapter.notifyItemInserted(messages.size()-1);//will redraw
            binding.userMessage.setText("");//remove what you typed
            //tell the recycle view to update:

        });

        binding.myRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();

        //this represents a single row on the list
        class MyRowHolder extends RecyclerView.ViewHolder {

            public TextView messageText;
            public TextView timeText;

            public MyRowHolder(@NonNull View itemView) {
                super(itemView);
                //like onCreate above
                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time); //find the ids from XML to java
            }
        }
        if(messages == null)
        {
            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
        }

        //creates rows 0 to 50
        binding.myRecycler.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

                    //just inflate the XML
                    @NonNull @Override                                              // implement multiple layouts
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        //viewType will be 0 for the first 3 rows, 1 for everything after

                        if(viewType == 0) {
                            SentRowLayoutBinding binding = SentRowLayoutBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(binding.getRoot()); //call your constructor below
                        }
                        else {  //after row 3
                            ReceiveRowLayoutBinding binding = ReceiveRowLayoutBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(binding.getRoot());
                        }
                    }
                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        //replace the default text with text at row position
                        ChatMessage chatMessage = messages.get(position);
                        holder.messageText.setText(chatMessage.getMessage());

                        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
                        String currentDateandTime = sdf.format(new Date());
                        holder.timeText.setText((CharSequence) currentDateandTime);

                    }
                    @Override
                    public int getItemCount() {

                        return messages.size();
                    }
                    @Override
                    public int getItemViewType(int position) {
                        //given the row, return an layout id for that row

                        if(messages.get(position).isSentButton()) {
                            return 0;
                        } else
                            return 1;
                    }



                    //number of rows you want

                }
        );



    }


    public class ChatMessage {
        String message;
        //        String timeSent;
        boolean isSentButton;

        ChatMessage(String m, boolean sent)
        {
            message = m;
//            timeSent = t;
            isSentButton = sent;
        }
        public String getMessage() {
            return message;
        }
        public boolean isSentButton() {
            return isSentButton;
        }
    }
}



