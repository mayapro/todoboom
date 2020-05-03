package com.example.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
//    private TextView my_hello;
    private String newTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        my_hello = findViewById(R.id.Hello_text);
        final EditText todoText = findViewById(R.id.todoText);//get the id for edit text
        Button buttonAnimate = findViewById(R.id.button_create);

        buttonAnimate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (todoText.getText().toString() != null)//check whether the entered text is not null
                {
                    String newToDo = todoText.getText().toString();
//                    my_hello.setText(newToDo);
                    todoText.getText().clear();
                }
            }
        });
        if (savedInstanceState != null) {
            newTxt = savedInstanceState.getString("todoText");
//            my_hello.setText(newTxt);
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putString("todoText",my_hello.getText().toString());
    }
}
