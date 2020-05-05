package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
//import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
{
    private EditText todoText;
    private ArrayList<ToDo> exampleList;
    private RecyclerView myRecyclerView;
    private ToDoAdapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoText = findViewById(R.id.todoText);//get the id for edit text
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
                    String newToDo = todoText.getText().toString();
                    insertItem(newToDo);
                    todoText.getText().clear();
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

    public void insertItem (String newTextToDO)
    {
        exampleList.add(new ToDo(newTextToDO, ToDo.NOT_DONE));
        myAdapter.notifyItemInserted(exampleList.size() - 1);
    }

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
        });
    }


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
