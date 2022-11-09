package com.example.encryptednotes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.ViewHolder> {

    // define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
        boolean onItemLongClick(View itemView, int position);
    }

    //define the listener member variable
    private OnItemClickListener listener;

    // define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleView;
        public ViewHolder(View itemView) {
            super(itemView);

            titleView = itemView.findViewById(R.id.textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // trigger click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemLongClick(itemView, position);
                        }
                    }

                    return true;
                }
            });
        }
    }

    private List<String> notes;
    public NotesAdapter(ArrayList<String> notes) {
        this.notes = notes;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @NonNull
    @Override
    public NotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View noteView = inflater.inflate(R.layout.list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(noteView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(NotesAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        String note = notes.get(position);

        // Set item views based on your views and data model
        TextView titleView = holder.titleView;
        titleView.setText(note);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return notes.size();
    }
}
