package com.example.todoboom;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.TextView;
import android.view.LayoutInflater;

import java.util.ArrayList;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder> {
    private ArrayList<ToDo> myTodoList;

    public static class ToDoViewHolder extends RecyclerView.ViewHolder{
        public TextView oneTaskText;

        public ToDoViewHolder(@NonNull View itemView) {
            super(itemView);
            oneTaskText = itemView.findViewById(R.id.one_task);
        }
    }

    public ToDoAdapter(ArrayList<ToDo> exampleList) {
        myTodoList = exampleList;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent, false);
        ToDoViewHolder evh = new ToDoViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position) {
        ToDo currentItem = myTodoList.get(position);

        holder.oneTaskText.setText(currentItem.get_one_mission());
    }

    @Override
    public int getItemCount() {
        return myTodoList.size();
    }

}
