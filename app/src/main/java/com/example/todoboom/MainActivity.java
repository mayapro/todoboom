package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private EditText todoText;

    private ArrayList<ToDo> exampleList;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoText = findViewById(R.id.todoText);//get the id for edit text
        Button buttonAnimate = findViewById(R.id.button_create);

        exampleList = new ArrayList<ToDo>();
//        exampleList.add(new ToDo1("my first task", ToDo1.NOT_DONE));
//        exampleList.add(new ToDo2("my first task111", ToDo2.NOT_DONE));
//        exampleList.add(new ToDo3("my first task22", ToDo3.NOT_DONE));

        createRecycler();

        buttonAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (!todoText.getText().toString().equals("")) //check whether the entered text is not null
                {
                    String newToDo = todoText.getText().toString();
                    insertItem(newToDo);
                    todoText.getText().clear();
                }
            }

        });
        if (savedInstanceState != null)
        {
            ArrayList<String> mission_temp = savedInstanceState.getStringArrayList("mission");
            ArrayList<Integer> state_temp = savedInstanceState.getIntegerArrayList("mission_state");
            for(int i=0 ; i<mission_temp.size() ; i++){
                exampleList.add(new ToDo(mission_temp.get(i), state_temp.get(i)));
            }
        }
    }

    public void insertItem (String newTextToDO)
    {
        exampleList.add(new ToDo(newTextToDO, ToDo.NOT_DONE));
        myAdapter.notifyItemInserted(exampleList.size() - 1);   // ???? plus1 ????
    }

    public void createRecycler ()
    {
        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new ToDoAdapter(exampleList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        ArrayList<String> mission=new ArrayList<>();
        ArrayList<Integer> state=new ArrayList<>();
        for(int i=0;i<exampleList.size();i++){
            mission.add(exampleList.get(i).get_one_mission());
            state.add(exampleList.get(i).get_mission_state());
        }
        outState.putStringArrayList("mission",mission);
        outState.putIntegerArrayList("mission_state",state);
    }
}
