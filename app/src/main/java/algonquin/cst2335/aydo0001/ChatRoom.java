package algonquin.cst2335.aydo0001;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

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

    ArrayList<ChatMessage> theMessages = null;

    ActivityChatRoomBinding binding ;
    RecyclerView.Adapter myAdapter = null;

    //Declare the dao here:
    ChatMessageDAO mDao;
    ChatRoomViewModel chatModel;

    @Override
    public void onBackPressed() {
        chatModel.selectedMessage.postValue(null);
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        theMessages = chatModel.theMessages;
        chatModel.selectedMessage.observe(this, selectedMessage -> {

                    if (selectedMessage != null) {
                        MessageDetailsFragment newMessage = new MessageDetailsFragment(selectedMessage);
                        FragmentManager fMgr = getSupportFragmentManager();
                        FragmentTransaction tx = fMgr.beginTransaction();
                        tx.addToBackStack("any string here");
                        tx.add(R.id.fragmentLocation, newMessage);
                        tx.commit();
                    }
                });


        //get the data from the ViewModel:


        //load messages from the database:
        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "fileOnYourPhone").build();

        //intialize the variable
        mDao = db.cmDao(); //get a DAO object to interact with the database

        //load all messages from database:
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {

            List<ChatMessage> fromDatabase = mDao.getAllMessages();//return a List
            theMessages.addAll(fromDatabase);//this adds all messages from the database

        });

        //end of loading from database

        binding.addButton.setOnClickListener(click -> {
            String newMessage = binding.userMessage.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            ChatMessage thisMessage = new ChatMessage(newMessage, currentDateandTime, true);
            theMessages.add(thisMessage);
            binding.userMessage.setText("");//remove what you typed
            //tell the recycle view to update:
            myAdapter.notifyDataSetChanged();//will redraw

            // add to database on another thread:
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                //this is on a background thread
                thisMessage.id = mDao.insertMessage(thisMessage); //get the ID from the database
                Log.d("TAG", "The id created is:" + thisMessage.id);
            }); //the body of run()
        });
        binding.receiveButton.setOnClickListener(click -> {
            String newMessage = binding.userMessage.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());

            // Create a new ChatMessage for received message (use false for the third parameter)
            ChatMessage receivedMessage = new ChatMessage(newMessage, currentDateandTime, false);

            theMessages.add(receivedMessage);
            binding.userMessage.setText(""); // remove what you typed
            myAdapter.notifyDataSetChanged(); // will redraw

            // Add to database on another thread:
            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                // This is on a background thread
                receivedMessage.id = mDao.insertMessage(receivedMessage); // Get the ID from the database
                Log.d("TAG", "The id created is:" + receivedMessage.id);
            });
        });


        binding.myRecycler.setAdapter(
                myAdapter = new RecyclerView.Adapter<MyRowHolder>() {

                    //just inflate the XML
                    @NonNull
                    @Override
                    // implement multiple layouts
                    public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        //viewType will be 0 for the first 3 rows, 1 for everything after

                        if (viewType == 0) {
                            SentRowLayoutBinding rowBinding = SentRowLayoutBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(rowBinding.getRoot()); //call your constructor below
                        } else {  //after row 3
                            ReceiveRowLayoutBinding rowBinding = ReceiveRowLayoutBinding.inflate(getLayoutInflater(), parent, false);
                            return new MyRowHolder(rowBinding.getRoot());
                        }
                    }

                    @Override
                    public int getItemViewType(int position) {
                        //given the row, return an layout id for that row

                        if (theMessages.get(position).SendOrReceive) {
                            return 0;
                        } else
                            return 1;

                    }

                    //number of rows you want
                    @Override
                    public int getItemCount() {
                        return theMessages.size();
                    }

                    @Override
                    public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                        //replace the default text with text at row position

                        String forRow = theMessages.get(position).message;
                        holder.message.setText(forRow);
                        holder.time.setText(theMessages.get(position).timeSent);
                    }


                }
        ); //populate the list

        binding.myRecycler.setLayoutManager(new LinearLayoutManager(this));

    }


    //this represents a single row on the list
    class MyRowHolder extends RecyclerView.ViewHolder {

        public TextView message;
        public TextView time;
        public MyRowHolder(@NonNull View itemView) {
            super(itemView);
            //like onCreate above

            itemView.setOnClickListener( click -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage selected = theMessages.get(position);

                if(chatModel.selectedMessage.getValue() == null)
                    chatModel.selectedMessage.postValue(selected);


//                int rowNum = getAbsoluteAdapterPosition();//which row this is
//                ChatMessage toDelete = theMessages.get(rowNum);
//                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
//
//                builder.setNegativeButton("No" , (btn, obj)->{ /* if no is clicked */  }  );
//                builder.setMessage("Do you want to delete this message?");
//                builder.setTitle("Delete");
//
//                builder.setPositiveButton("Yes", (p1, p2)-> {
//                    /*is yes is clicked*/
//                    Executor thread1 = Executors.newSingleThreadExecutor();
//                    thread1.execute(( ) -> {
//                        //delete from database
//                        mDao.deleteMessage(toDelete);//which chat message to delete?
//
//                    });
//                    theMessages.remove(rowNum);//remove from the array list
//                    myAdapter.notifyDataSetChanged();//redraw the list
//
//
//                    //give feedback:anything on screen
//                    Snackbar.make( itemView , "You deleted the row", Snackbar.LENGTH_LONG)
//                            .setAction("Undo", (btn) -> {
//                                Executor thread2 = Executors.newSingleThreadExecutor();
//                                thread2.execute(( ) -> {
//                                    mDao.insertMessage(toDelete);
//                                });
//
//
//                                theMessages.add(rowNum, toDelete);
//                                myAdapter.notifyDataSetChanged();//redraw the list
//                            })
//                            .show();
//                });
//
//                builder.create().show(); //this has to be last
            });

            message = itemView.findViewById(R.id.message);
            time = itemView.findViewById(R.id.time); //find the ids from XML to java


        }
    }
}