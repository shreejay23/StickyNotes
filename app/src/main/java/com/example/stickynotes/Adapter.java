package com.example.stickynotes;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    LayoutInflater inflater;
    List<Note> notes;

    Adapter(Context context, List<Note> notes){
        this.inflater = LayoutInflater.from(context);
        this.notes = notes;
    }


    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = inflater.inflate(R.layout.custom_list_view, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder viewHolder, int i) {

        String title = notes.get(i).getTitle();
        String date = notes.get(i).getDate();
        String time = notes.get(i).getTime();
        long id = notes.get(i).getID();
        String email = notes.get(i).getEmail();
        Log.d("Date on : ", "Date on : "+date);

        viewHolder.nTitle.setText(title);
        viewHolder.nDate.setText(date);
        viewHolder.nTime.setText(time);
        viewHolder.nID.setText(String.valueOf(notes.get(i).getID()));
        viewHolder.nEmail.setText(String.valueOf(notes.get(i).getEmail()));

    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView nTitle, nDate, nTime ,nID, nEmail;


        public ViewHolder(View itemView){
            super(itemView);
            nTitle = itemView.findViewById(R.id.ntitle);
            nDate = itemView.findViewById(R.id.ndate);
            nTime = itemView.findViewById(R.id.ntime);
            nID = itemView.findViewById(R.id.listID);
            nEmail = itemView.findViewById(R.id.nemail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(view.getContext(), Details.class);
                    i.putExtra("ID", notes.get(getAdapterPosition()).getID());
                    view.getContext().startActivity(i);
                }
            });
        }
    }

}
