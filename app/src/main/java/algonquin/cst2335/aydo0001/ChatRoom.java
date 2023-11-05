package algonquin.cst2335.aydo0001;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.aydo0001.data.ChatRoomViewModel;
import algonquin.cst2335.aydo0001.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.aydo0001.databinding.ReceiveRowLayoutBinding;
import algonquin.cst2335.aydo0001.databinding.SentRowLayoutBinding;

public class ChatRoom extends AppCompatActivity {

    ArrayList<ChatMessage> messages = new ArrayList<>();

    ActivityChatRoomBinding binding;
    RecyclerView.Adapter myAdapter;
    ChatRoomViewModel chatModel;

    ChatMessageDAO theDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "database-name").build();
        theDAO = db.cmDao();


        binding.addButton.setOnClickListener( click ->{
            String userMessage = binding.userMessage.getText().toString();
            ChatMessage m = new ChatMessage(userMessage, "Time",true);
            messages.add(m);

            binding.userMessage.setText("");//remove what you typed
            myAdapter.notifyItemInserted(messages.size()-1);//will redraw

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(( ) -> {

                m.id = theDAO.insertMessage(m);
                Log.d("TAG", "The id created is:" + m.id);

            });
        });

//        binding.receiveButton.setOnClickListener( click ->{
//            String messageText = binding.userMessage.getText().toString();
//            ChatMessage receivedMessage = new ChatMessage(messageText, "Time", false);
//            messages.add(receivedMessage);
//            myAdapter.notifyItemInserted(messages.size()-1);//will redraw
//            binding.userMessage.setText("");//remove what you typed
//            //tell the recycle view to update:
//
//        });

        binding.myRecycler.setLayoutManager(new LinearLayoutManager(this));
        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        messages = chatModel.messages.getValue();
        //get all messages from database


        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute((new Runnable() {

            @Override
            public void run() {
                List<ChatMessage> fromDatabase = theDAO.getAllMessages();
                messages.addAll( fromDatabase );
            }
        }));






        //this represents a single row on the list
        class MyRowHolder extends RecyclerView.ViewHolder {

            public TextView messageText;
            public TextView timeText;

            public MyRowHolder(@NonNull View entireRow) {
                super(entireRow);

                entireRow.setOnClickListener( cl -> {
                   int whichRow = getAbsoluteAdapterPosition();
                   ChatMessage thisMessage = messages.get(whichRow);

                    AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                    builder.setTitle("Warning!!");
                    builder.setMessage("Do you want to delete this message?");
                    builder.setPositiveButton("Yes", (a,b) -> {
                        Executor thread = Executors.newSingleThreadExecutor();
                        thread.execute(() -> {
                            theDAO.deleteMessage(thisMessage);

                        });
                        messages.remove(whichRow); //delete from ArrayList
                        myAdapter.notifyDataSetChanged();//redraw the list

                        Snackbar.make(messageText, "Message was deleted", Snackbar.LENGTH_LONG)
                                .setAction("Undo button", (click) -> {
                                Executor thread2 = Executors.newSingleThreadExecutor();
                                thread2.execute(() -> {

                                    theDAO.insertMessage(thisMessage);
                            });

                            messages.add(whichRow, thisMessage); //insert into ArrayList
                            myAdapter.notifyDataSetChanged();//redraw the list
                        })
                                .show();

                    });
                    builder.setNegativeButton("No, don't delete", (a,b) -> {});
                    builder.create(); //this shows the window.

                });
                //like onCreate above
                messageText = itemView.findViewById(R.id.message);
                timeText = itemView.findViewById(R.id.time); //find the ids from XML to java
            }
        }
//        if(messages == null)
//        {
//            chatModel.messages.postValue( messages = new ArrayList<ChatMessage>());
//        }

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
                        String forRow = messages.get(position).message;
                        holder.messageText.setText(forRow);

//                        ChatMessage chatMessage = messages.get(position);
//                        holder.messageText.setText(chatMessage.getMessage());

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

                        if (position < 3) {
                            return 0;
                        } else
                            return 1;
                    }

//                        if(messages.get(position).SendOrReceive()) {
//                            return 0;
//                        } else
//                            return 1;
//                    }



                    //number of rows you want

                }
        );



    }


//    public class ChatMessage {
//        String message;
//        //        String timeSent;
//        boolean isSentButton;
//
//        ChatMessage(String m, boolean sent)
//        {
//            message = m;
////            timeSent = t;
//            isSentButton = sent;
//        }
//        public String getMessage() {
//            return message;
//        }
//        public boolean isSentButton() {
//            return isSentButton;
//        }
//    }
}



