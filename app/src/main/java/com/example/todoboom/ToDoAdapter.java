package com.example.todoboom;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
//import android.widget.AdapterView;
import android.widget.AdapterView;
import android.widget.TextView;
import android.view.LayoutInflater;
import java.util.ArrayList;


public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>
{
    private ArrayList<ToDo> myTodoList;
    private ToDoAdapter.OnItemClickListener myListener;

    public interface OnItemClickListener
    {
        void onItemClick(int position);
        void onLongItemClick(int position); // called when there is a long click
    }
    public void setOnItemClickListener(ToDoAdapter.OnItemClickListener listener)
    {
        myListener = listener;
    }

    public static class ToDoViewHolder extends RecyclerView.ViewHolder
    {
        public TextView oneTaskText;

        public ToDoViewHolder(@NonNull View itemView, final ToDoAdapter.OnItemClickListener listener)
        {
            super(itemView);
            oneTaskText = itemView.findViewById(R.id.one_task);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if (listener != null)
                    {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION)
                        {
                            listener.onLongItemClick(position);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    public ToDoAdapter(ArrayList<ToDo> exampleList)
    {
        myTodoList = exampleList;
    }

    @NonNull
    @Override
    public ToDoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_item, parent,
                false);
        ToDoViewHolder evh = new ToDoViewHolder(v, myListener);
        return evh;
    }

    /**
     * function for changing the state of mission to done
     * @param holder - ToDoViewHolder item
     * @param position - the position of the wanted item to change
     */
    @Override
    public void onBindViewHolder(@NonNull ToDoViewHolder holder, int position)
    {
        ToDo currentItem = myTodoList.get(position);
        if (currentItem.get_mission_state() == ToDo.DONE)
        {
            holder.oneTaskText.setTextColor(Color.GRAY);
            holder.oneTaskText.setText(currentItem.get_one_mission());
        }
        else
        {
            holder.oneTaskText.setText(currentItem.get_one_mission());
        }
    }

    @Override
    public int getItemCount()
    {
        return myTodoList.size();
    }

}
