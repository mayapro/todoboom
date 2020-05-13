package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private EditText todoText;
    private ArrayList<ToDo> exampleList;
    private RecyclerView myRecyclerView;
    private ToDoAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;
    private AlertDialog.Builder removeMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        todoText = findViewById(R.id.todoText); //get the id for edit text
        Button buttonAnimate = findViewById(R.id.button_create);
        exampleList = new ArrayList<ToDo>();
        createRecycler();

        loadData();

        buttonAnimate.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (!todoText.getText().toString().equals("")) //check whether the entered text is not null
                {
                    String newToDo = todoText.getText().toString();
                    insertItem(newToDo, ToDo.NOT_DONE);
                    todoText.getText().clear();
                    saveData();
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
            for(int i=0 ; i<mission_temp.size() ; i++)
            {
                exampleList.add(new ToDo(mission_temp.get(i), state_temp.get(i)));
            }

        }

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
        editor.apply();
    }

    /**
     * function for loading SharedPreferences with exampleList
     */
    private void loadData() {
        SharedPreferences sp = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString("task list", null);
        Type type = new TypeToken<ArrayList<ToDo>>() {}.getType();
        ArrayList<ToDo> tmpexampleList = gson.fromJson(json, type);
        if (tmpexampleList != null){
            exampleList.clear();
            for (int i = 0; i < tmpexampleList.size(); i++)
            {
                insertItem(tmpexampleList.get(i).get_one_mission(), tmpexampleList.get(i).get_mission_state());
            }
            Log.d("size of list", String.valueOf(tmpexampleList.size()));
        }
        if (tmpexampleList == null) {
            Log.d("size of list", "0");
            exampleList = new ArrayList<>();
        }
    }


    /**
     * function that create a dialog box for the user
     * @param position - the position of the card that clicked
     */
    public void removeMessageCheck (final int position)
    {
        removeMessage = new AlertDialog.Builder(this);
        removeMessage.setMessage("Are You Sure to delete?");

        removeMessage.setPositiveButton("Yes, please", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //perform any action
                Toast.makeText(getApplicationContext(), "Yes I'm done", Toast.LENGTH_SHORT).show();
                removeItem(position);

                saveData();
            }
        });

        removeMessage.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //perform any action
                Toast.makeText(getApplicationContext(), "Not done", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * function to add an item from the todolist
     * @param newTextToDO - the text of the the todo_item
     */
    public void insertItem (String newTextToDO, int status)
    {
        exampleList.add(new ToDo(newTextToDO, status));
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
                changeItem(position);
            }

            @Override
            public void onLongItemClick(int position)
            {
                removeMessageCheck(position);
                AlertDialog alertDialog = removeMessage.create();
                alertDialog.show();
            }
        });
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
        for(int i=0 ;i<exampleList.size(); i++)
        {
            mission.add(exampleList.get(i).get_one_mission());
            state.add(exampleList.get(i).get_mission_state());
        }
        outState.putStringArrayList("mission",mission);
        outState.putIntegerArrayList("mission_state",state);
    }
}
