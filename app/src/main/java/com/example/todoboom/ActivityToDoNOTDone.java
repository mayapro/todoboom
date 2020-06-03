package com.example.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;


public class ActivityToDoNOTDone extends AppCompatActivity {
    private EditText editTodo;
    private TextView createTime;
    private TextView editTime;
    private Integer position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_n_o_t_done);

        editTodo = (EditText) findViewById(R.id.todoText_notDone);
        createTime = (TextView) findViewById(R.id.not_done_create_time);
        editTime = (TextView) findViewById(R.id.not_done_edit_time);

        Button buttApplay = findViewById(R.id.button_apply);
        Button buttDone = findViewById(R.id.button_done);

        Intent intent = getIntent();

//        currentId = intent.getIntExtra("currentId", 0);
        position = intent.getIntExtra("position", 0);

        Log.d("position in item", String.valueOf(position));

        String todoString = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        String createTimeString = "create time: " + "   " +
                                                intent.getStringExtra(MainActivity.CREATE_TIME);
        String editTimeString = "edit time: " + "   " +
                                                intent.getStringExtra(MainActivity.EDIT_TIME);

        editTodo.setText(todoString);
        createTime.setText(createTimeString);
        editTime.setText(editTimeString);

        buttApplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTodoTemp = editTodo.getText().toString();

                Intent resultIntent = new Intent();

                resultIntent.putExtra(MainActivity.EDIT_TODO, editTodoTemp);
                // sends the current position of the mission
                resultIntent.putExtra("position", position);
                // sends if there is a need to update the todo_mission
                resultIntent.putExtra(MainActivity.CLICKED_BUTTON, MainActivity.APPLY);

                setResult(RESULT_OK, resultIntent);

                Toast.makeText(getApplicationContext(), "you just changed the mission", Toast.LENGTH_SHORT).show();
//                finish();
            }
        });

        buttDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                // sends the current position of the mission
                resultIntent.putExtra("position", position);
                // sends if the user marked done on the mission
                resultIntent.putExtra(MainActivity.CLICKED_BUTTON, ToDo.DONE);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

    }
}
