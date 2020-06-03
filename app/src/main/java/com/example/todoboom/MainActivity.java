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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
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
    private CollectionReference notebookRef = db.collection("Notebook");
    private DocumentReference noteRef = db.document("Notebook/My First Note"); // i need to change it!!!
    private DocumentReference noteRef1 = db.document("Notebook/note5"); // i need to change it!!!

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

                    addNote(exampleList.size()-1);
                    updatecounter();

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
            ArrayList<String> mission_old = savedInstanceState.getStringArrayList("old_mission");

            ArrayList<Integer> state_temp = savedInstanceState.getIntegerArrayList("mission_state");

            ArrayList<String> timestamp_temp = savedInstanceState.getStringArrayList("timestamp");
            ArrayList<String> edit_timestamp_temp = savedInstanceState.getStringArrayList("edit_timestamp");
            ArrayList<Integer> mission_id_temp = savedInstanceState.getIntegerArrayList("mission_id");

            exampleList.clear();
            for(int i=0 ; i<mission_temp.size() ; i++)
            {

                exampleList.add(new ToDo(mission_temp.get(i), mission_old.get(i), timestamp_temp.get(i),
                        edit_timestamp_temp.get(i), mission_id_temp.get(i), state_temp.get(i)));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                exampleList.clear();

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    String allData = documentSnapshot.getString("KEY_TITLE");
                    if (allData.equals("0"))
                    {
                        counter = Integer.valueOf(documentSnapshot.getString("noteCounter"));
                    }
                    else {

                        Gson gson = new Gson();
                        Type type = new TypeToken<ToDo>() {
                        }.getType();
                        ToDo tmpexampleList = gson.fromJson(allData, type);

                        exampleList.add(new ToDo(tmpexampleList.get_one_mission(),
                                tmpexampleList.get_old_mission(),
                                tmpexampleList.get_creation_timestamp(),
                                tmpexampleList.get_edit_timestamp(),
                                tmpexampleList.get_mission_state(),
                                tmpexampleList.get_mission_id()));

                        myAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }


    public void addNotecounter() {
        String noteName = "MainNoteCounter";
        Map<String, Object> note = new HashMap<>();
        note.put("noteCounter", String.valueOf(counter));
        note.put("KEY_TITLE", "0");

        notebookRef.document(noteName).set(note);
    }

    public void updatecounter() {
        String noteName = "MainNoteCounter";
        Map<String, Object> note = new HashMap<>();
        note.put("noteCounter", String.valueOf(counter));
        note.put("KEY_TITLE", "0");

        notebookRef.document(noteName).set(note);
    }

    public void addNote(int position) {
        Gson gson = new Gson();
        String json = gson.toJson(exampleList.get(position));

        String noteName = "note" + exampleList.get(position).get_mission_id();
        Map<String, Object> note = new HashMap<>();
        note.put("KEY_TITLE", json);
        notebookRef.document(noteName).set(note);
    }

    public void loadNotes() {
        notebookRef.whereGreaterThanOrEqualTo("priority", 2)
                .orderBy("priority", Query.Direction.DESCENDING)
                .limit(3)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        exampleList.clear();

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            String allData = documentSnapshot.getString("KEY_TITLE");
                            if (allData.equals("0"))
                            {
                                counter = Integer.valueOf(documentSnapshot.getString("noteCounter"));
                            }
                            else {
                                Gson gson = new Gson();
                                Type type = new TypeToken<ToDo>() {
                                }.getType();
                                ToDo tmpexampleList = gson.fromJson(allData, type);
                                exampleList.add(new ToDo(tmpexampleList.get_one_mission(),
                                        tmpexampleList.get_old_mission(),
                                        tmpexampleList.get_creation_timestamp(),
                                        tmpexampleList.get_edit_timestamp(),
                                        tmpexampleList.get_mission_state(),
                                        tmpexampleList.get_mission_id()));
                                myAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }



    public void deleteNote(int id) {
        String toDelete = "Notebook/note" + id;
        db.document(toDelete).delete();
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
        exampleList.add(new ToDo(newTextToDO, newTextToDO, tempTime, tempTime1, status, id));
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

//    /**
//     * function to change the status the item of the todo_list to done
//     * @param position - the position of the item in the list
//     */
//    public void changeItem(int position) {
//        exampleList.get(position).mark_mission_done();
//        Toast.makeText(getApplicationContext(),"(TODO1) " +
//                        exampleList.get(position).get_one_mission() + " is now DONE. BOOM!"
//                ,Toast.LENGTH_SHORT).show();
////        myAdapter.notifyItemChanged(position);
//    }

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
        Integer currentId = (Integer) exampleList.get(position).get_mission_id();

        Intent intent = new Intent(this, ActivityToDoDone.class);
        intent.putExtra(EXTRA_TEXT, todoMission);
        intent.putExtra(CREATE_TIME, createTime);
        intent.putExtra(EDIT_TIME, editTime);

        intent.putExtra("position", position);

        intent.putExtra("currentId", currentId);

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
                    addNote(tempPosition);
                }

                if (clicked_button == ToDo.DONE)
                {
                    Integer tempPosition = data.getIntExtra("position", 0);
                    cc = Calendar.getInstance();
                    String tempTime = get_time(cc);

                    exampleList.get(tempPosition).mark_mission_done();
                    Toast.makeText(getApplicationContext(),"TODO " +
                                    exampleList.get(tempPosition).get_one_mission() + " is now DONE. BOOM!"
                            ,Toast.LENGTH_SHORT).show();

                    myAdapter.notifyItemChanged(tempPosition);
                    addNote(tempPosition);
                }

                if (clicked_button == ToDo.NOT_DONE)
                {
                    int tempPosition = data.getIntExtra("position", 0);
                    cc = Calendar.getInstance();
                    String tempTime = get_time(cc);

                    exampleList.get(tempPosition).mark_mission_not_done(tempTime);

                    myAdapter.notifyItemChanged(tempPosition);
                    addNote(tempPosition);
                }

                if (clicked_button == DELETE)
                {
                    int tempPosition = data.getIntExtra("position", 0);
                    int tempId = data.getIntExtra("currentId", 0);

                    removeItem(tempPosition);

                    deleteNote(tempId);
                }
            }
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
        ArrayList<String> oldMission = new ArrayList<>();

        ArrayList<Integer> state=new ArrayList<>();


        ArrayList<String> timeStemp =new ArrayList<>();
        ArrayList<String> editTimeStemp =new ArrayList<>();
        ArrayList<Integer> ids=new ArrayList<>();


        for(int i=0 ;i<exampleList.size(); i++)
        {
            mission.add(exampleList.get(i).get_one_mission());
            oldMission.add(exampleList.get(i).get_old_mission());
            state.add(exampleList.get(i).get_mission_state());
            timeStemp.add(exampleList.get(i).get_creation_timestamp());
            editTimeStemp.add(exampleList.get(i).get_edit_timestamp());
            ids.add(exampleList.get(i).get_mission_id());
        }
        outState.putStringArrayList("mission",mission);
        outState.putStringArrayList("old_mission",oldMission);

        outState.putIntegerArrayList("mission_state",state);

        outState.putStringArrayList("timestamp",timeStemp);
        outState.putStringArrayList("edit_timestamp",editTimeStemp);
        outState.putIntegerArrayList("mission_id",ids);

    }

}
