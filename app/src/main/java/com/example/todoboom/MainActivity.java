package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity
{
    private EditText todoText;
    public ArrayList<ToDo> exampleList;
    private RecyclerView myRecyclerView;
    private ToDoAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private AlertDialog.Builder removeMessage;

    private Calendar cc;

    public static final String EXTRA_TEXT = "EXTRA_TEXT";
    public static final String CREATE_TIME = "createTime";
    public static final String EDIT_TIME = "editTime";

    public static final String EDIT_TODO = "apply";
    public static final String CLICKED_BUTTON = "clicked";

    public static final Integer APPLY = 3;
    public static final Integer DELETE = 2;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference noteRef = db.document("Notebook/My First Note"); // i need to change it!!!
    private TextView textViewData;


    private Integer counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoText = findViewById(R.id.todoText); //get the id for edit text
        Button buttonAnimate = findViewById(R.id.button_create);
        exampleList = new ArrayList<ToDo>();
        createRecycler();
        deleteNote();
        loadNote();

        buttonAnimate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!todoText.getText().toString().equals("")) //check whether the entered text is not null
                {
                    cc = Calendar.getInstance();
                    String tempTime = get_time(cc);

                    counter += 1;
                    String newToDo = todoText.getText().toString();
                    insertItem(newToDo, tempTime , tempTime, ToDo.NOT_DONE, counter);
                    todoText.getText().clear();

                    saveNote();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"you can't create an empty TODO item"
                            ,Toast.LENGTH_SHORT).show();
                }
            }

        });

        if (savedInstanceState != null)
        {
            ArrayList<String> mission_temp = savedInstanceState.getStringArrayList("mission");
            ArrayList<Integer> state_temp = savedInstanceState.getIntegerArrayList("mission_state");

            ArrayList<String> timestamp_temp = savedInstanceState.getStringArrayList("timestamp");
            ArrayList<String> edit_timestamp_temp = savedInstanceState.getStringArrayList("edit_timestamp");
            ArrayList<Integer> mission_id_temp = savedInstanceState.getIntegerArrayList("mission_id");

            exampleList.clear();  // why would i need that? i need to check it
            for(int i=0 ; i<mission_temp.size() ; i++)
            {
                Log.d("is this a problem", String.valueOf(i));

                exampleList.add(new ToDo(mission_temp.get(i), timestamp_temp.get(i),
                        edit_timestamp_temp.get(i), mission_id_temp.get(i), state_temp.get(i)));
            }
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @Override
//            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
//                if (e != null) {
//                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
////                    Log.d(TAG, e.toString());
//                    return;
//                }
//                if (documentSnapshot.exists()) {
//                    loadData(documentSnapshot);
//                    Log.d("is this a problem", "made it");
//                }
//            }
//        });
//    }

    public void saveNote() {
        Gson gson = new Gson();
        String json = gson.toJson(exampleList);

        Map<String, Object> note = new HashMap<>();
        note.put("KEY_TITLE", json);
        note.put("counterForId", String.valueOf(counter));

        db.collection("Notebook").document("My First Note").set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadNote() {
        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            loadData(documentSnapshot);
                        } else {
                            Log.d("maya", "problem");
                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d("TAG", e.toString());
                    }
                });
    }

    public void deleteNote() {
        noteRef.delete();
    }

    /**
     * function for updating SharedPreferences with the changing in exampleList
     */
    private void saveData() {
        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        Gson gson = new Gson();
        String json = gson.toJson(exampleList);
        editor.putString("task list", json);

        editor.putInt("counterForId", counter);
        editor.apply();
    }

    /**
     * function for loading SharedPreferences with exampleList
     */
    private void loadData(DocumentSnapshot documentSnapshot) {
        String allData = documentSnapshot.getString("KEY_TITLE");

        Gson gson = new Gson();
        String json = allData;

        counter = Integer.valueOf(documentSnapshot.getString("counterForId"));
        Type type = new TypeToken<ArrayList<ToDo>>() {}.getType();
        ArrayList<ToDo> tmpexampleList = gson.fromJson(json, type);

        if (tmpexampleList != null){
            exampleList.clear();
            for (int i = 0; i < tmpexampleList.size(); i++)
            {
                exampleList.add(new ToDo(tmpexampleList.get(i).get_one_mission(),
                        tmpexampleList.get(i).get_creation_timestamp(),
                        tmpexampleList.get(i).get_edit_timestamp(),
                        tmpexampleList.get(i).get_mission_state(),
                        tmpexampleList.get(i).get_mission_id()));
                myAdapter.notifyDataSetChanged();

            }
        }
        if (tmpexampleList == null) {
            Log.d("size of list", "0");
            exampleList = new ArrayList<>();
        }
    }


    public String get_time(Calendar cc)
    {
        int year = cc.get(Calendar.YEAR);
        int month = cc.get(Calendar.MONTH);
        int mDay = cc.get(Calendar.DAY_OF_MONTH);
        int mHour = cc.get(Calendar.HOUR_OF_DAY);
        int mMinute = cc.get(Calendar.MINUTE);

        String newTime = mDay + "/" + month + "/" + year +"   " + mHour + ":" + mMinute;
        return newTime;
    }

    /**
     * function to add an item from the todolist
     * @param newTextToDO - the text of the the todo_item
     */
    public void insertItem (String newTextToDO, String tempTime, String tempTime1, int status, int id)
    {
        exampleList.add(new ToDo(newTextToDO, tempTime, tempTime1, status, id));
        myAdapter.notifyItemInserted(exampleList.size() - 1);
    }

    /**
     * function to remove an item from the todo_list
     * @param position - the position of the item in the list
     */
    public void removeItem (int position)
    {
        exampleList.remove(position);
        myAdapter.notifyItemRemoved(position);
    }

    /**
     * function to change the status the item of the todo_list to done
     * @param position - the position of the item in the list
     */
    public void changeItem(int position) {
        if (exampleList.get(position).get_mission_state() != ToDo.DONE)
        {
            exampleList.get(position).mark_mission_done();
            Toast.makeText(getApplicationContext(),"TODO " +
                            exampleList.get(position).get_one_mission() + " is now DONE. BOOM!"
                    ,Toast.LENGTH_SHORT).show();
            myAdapter.notifyItemChanged(position);
        }
    }

    /**
     * function that create our recycler in the app and mange the clicks on the cards
     */
    public void createRecycler ()
    {
        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new ToDoAdapter(exampleList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);

        myAdapter.setOnItemClickListener(new ToDoAdapter.OnItemClickListener()
        {
            @Override
            public void onItemClick(int position)
            {
                if (exampleList.get(position).get_mission_state() == ToDo.DONE)
                {
                    openActivityDone(position);
//                    changeItem(position);
                }
                else
                {
                    openActivityNotDone(position);
                }
            }
        });
    }

    /**
     * function that opens new activity for the mission.
     */
    public void openActivityDone(int position)
    {
        String todoMission = exampleList.get(position).get_one_mission();
        String createTime = exampleList.get(position).get_creation_timestamp();
        String editTime = exampleList.get(position).get_edit_timestamp();
//        Integer currentId = (Integer) exampleList.get(position).get_mission_id();

        Intent intent = new Intent(this, ActivityToDoDone.class);
        intent.putExtra(EXTRA_TEXT, todoMission);
        intent.putExtra(CREATE_TIME, createTime);
        intent.putExtra(EDIT_TIME, editTime);

        intent.putExtra("position", position);

//        intent.putExtra("currentId", currentId);

        startActivityForResult(intent, 1);
    }

    /**
     * function that opens new activity for the mission.
     */
    public void openActivityNotDone(int position)
    {
        String todoMission = exampleList.get(position).get_one_mission();
        String createTime = exampleList.get(position).get_creation_timestamp();
        String editTime = exampleList.get(position).get_edit_timestamp();
//        Integer currentId = (Integer) exampleList.get(position).get_mission_id();

        Intent intent = new Intent(this, ActivityToDoNOTDone.class);
        intent.putExtra(EXTRA_TEXT, todoMission);
        intent.putExtra(CREATE_TIME, createTime);
        intent.putExtra(EDIT_TIME, editTime);

        intent.putExtra("position", position);

//        intent.putExtra("currentId", currentId);

        startActivityForResult(intent, 1);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int clicked_button = data.getIntExtra(CLICKED_BUTTON, 0);

                if (clicked_button == APPLY)
                {
                    String editedTodo =  data.getStringExtra(EDIT_TODO);
                    Integer tempPosition = data.getIntExtra("position", 0);

                    cc = Calendar.getInstance();
                    String tempTime = get_time(cc);

                    exampleList.get(tempPosition).edit_mission(editedTodo, tempTime);
                    myAdapter.notifyItemChanged(tempPosition);
                    saveNote();
                }

                if (clicked_button == ToDo.DONE)
                {
                    Integer tempPosition = data.getIntExtra("position", 0);
                    cc = Calendar.getInstance();
                    String tempTime = get_time(cc);

                    exampleList.get(tempPosition).mark_mission_done();
                    myAdapter.notifyItemChanged(tempPosition);
                    saveNote();
                }

                if (clicked_button == ToDo.NOT_DONE)
                {
                    int tempPosition = data.getIntExtra("position", 0);
                    cc = Calendar.getInstance();
                    String tempTime = get_time(cc);

                    exampleList.get(tempPosition).mark_mission_not_done(tempTime);
                    myAdapter.notifyItemChanged(tempPosition);
                    saveNote();
                }

                if (clicked_button == DELETE)
                {
                    int tempPosition = data.getIntExtra("position", 0);
                    removeItem(tempPosition);

                    saveNote();
                }
            }
//            if (resultCode == RESULT_CANCELED) {
//                mTextViewResult.setText("Nothing selected");
//            }
        }
    }


    /**
     * function for saving all the todoitems when shifting from landscape mode to portrait
     * and in reverse
     * @param outState - bundle
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);

        ArrayList<String> mission=new ArrayList<>();
        ArrayList<Integer> state=new ArrayList<>();

        ArrayList<String> timeStemp =new ArrayList<>();
        ArrayList<String> editTimeStemp =new ArrayList<>();
        ArrayList<Integer> ids=new ArrayList<>();

        Log.d("why would you make", String.valueOf(exampleList.size()));

        for(int i=0 ;i<exampleList.size(); i++)
        {
            mission.add(exampleList.get(i).get_one_mission());
            state.add(exampleList.get(i).get_mission_state());
            timeStemp.add(exampleList.get(i).get_creation_timestamp());
            editTimeStemp.add(exampleList.get(i).get_edit_timestamp());
            ids.add(exampleList.get(i).get_mission_id());
        }
        outState.putStringArrayList("mission",mission);
        outState.putIntegerArrayList("mission_state",state);

        outState.putStringArrayList("timestamp",timeStemp);
        outState.putStringArrayList("edit_timestamp",editTimeStemp);
        outState.putIntegerArrayList("mission_id",ids);

        Log.d("why would you", String.valueOf(exampleList.size()));

    }

}
