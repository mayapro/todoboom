package com.example.todoboom;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class ActivityToDoDone extends AppCompatActivity {
    private TextView createTime;
    private TextView editTime;
    private Integer currentId;
    private Integer position;
    private Intent resultIntent;

    private AlertDialog.Builder removeMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        TextView textViewtodo = findViewById(R.id.todoText_done);
        createTime = findViewById(R.id.done_create_time);
        editTime = findViewById(R.id.done_edit_time);

        Button buttdelete = findViewById(R.id.button_delete);
        Button buttnotDone = findViewById(R.id.button_not_done);

        Intent intent = getIntent();

        currentId = intent.getIntExtra("currentId", 0);
        position = intent.getIntExtra("position", 0);

        Log.d("position in item", String.valueOf(position));

        String todoString = intent.getStringExtra(MainActivity.EXTRA_TEXT);
        String createTimeString = "create time: " + "   " +
                intent.getStringExtra(MainActivity.CREATE_TIME);
        String editTimeString = "edit time: " + "   " +
                intent.getStringExtra(MainActivity.EDIT_TIME);

        textViewtodo.setText(todoString);
        createTime.setText(createTimeString);
        editTime.setText(editTimeString);

        buttdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultIntent = new Intent();
                removeMessageCheck(position);

                AlertDialog alertDialog = removeMessage.create();
                alertDialog.show();

            }
        });

        buttnotDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resultIntent = new Intent();
                // sends the current position of the mission
                resultIntent.putExtra("position", position);
                // sends if the user marked done on the mission
                resultIntent.putExtra(MainActivity.CLICKED_BUTTON, ToDo.NOT_DONE);

                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

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
                // sends the current position of the mission
                resultIntent.putExtra("position", position);
                // sends if there is a need to update the todo_mission
                resultIntent.putExtra(MainActivity.CLICKED_BUTTON, MainActivity.DELETE);

                resultIntent.putExtra("currentId", currentId);

                setResult(RESULT_OK, resultIntent);
                finish();
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
}
