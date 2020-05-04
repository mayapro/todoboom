package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
//    private TextView my_hello;
    private String newTxt;
    private RecyclerView myRecyclerView;
    private RecyclerView.Adapter myAdapter;
    private RecyclerView.LayoutManager myLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        my_hello = findViewById(R.id.Hello_text);
        final EditText todoText = findViewById(R.id.todoText);//get the id for edit text
        Button buttonAnimate = findViewById(R.id.button_create);

        setContentView(R.layout.activity_main);

        final ArrayList<ToDo> exampleList = new ArrayList<>();
        exampleList.add(new ToDo("my first task"));

        exampleList.add(new ToDo("my first task111"));
        exampleList.add(new ToDo("my first task22"));
        exampleList.add(new ToDo("my first task3"));
        exampleList.add(new ToDo("my first task4"));
        exampleList.add(new ToDo("my first task5"));
        exampleList.add(new ToDo("my first task6"));
        exampleList.add(new ToDo("my first task7"));
        exampleList.add(new ToDo("my first task8"));
        exampleList.add(new ToDo("my first task9"));


        myRecyclerView = findViewById(R.id.recyclerView);
        myRecyclerView.setHasFixedSize(true);
        myLayoutManager = new LinearLayoutManager(this);
        myAdapter = new ToDoAdapter(exampleList);

        myRecyclerView.setLayoutManager(myLayoutManager);
        myRecyclerView.setAdapter(myAdapter);

        buttonAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todoText.getText().toString() != null)//check whether the entered text is not null
                {
                    String newToDo = todoText.getText().toString();
                    exampleList.add(new ToDo(newToDo));
//                    my_hello.setText(newToDo);
                    todoText.getText().clear();
                }
            }
        });
//        if (savedInstanceState != null) {
//            newTxt = savedInstanceState.getString("todoText");
//            my_hello.setText(newTxt);
        }
    }

//    @Override
//    protected void onSaveInstanceState(@NonNull Bundle outState) {
//        super.onSaveInstanceState(outState);
////        outState.putString("todoText",my_hello.getText().toString());
//    }
//}
