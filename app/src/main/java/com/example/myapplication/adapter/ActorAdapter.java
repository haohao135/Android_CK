package com.example.myapplication.adapter;

import android.annotation.SuppressLint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activity.MovieDetails;
import com.example.myapplication.model.Actor;

import java.util.List;

public class ActorAdapter extends RecyclerView.Adapter<ActorAdapter.ViewHolde> {
    List<Actor> actorList;

    public ActorAdapter(List<Actor> actorList) {
        this.actorList = actorList;
    }

    @NonNull
    @Override
    public ViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.actor_item, parent,false);
        return new ViewHolde(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolde holder, @SuppressLint("RecyclerView") int position) {
        holder.name.setText(actorList.get(position).getActorName());
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                MovieDetails.pos = actorList.get(position).getId();
                MenuInflater inflater = new MenuInflater(v.getContext());
                inflater.inflate(R.menu.menu_context_account, menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(actorList == null || actorList.isEmpty()){
            return 0;
        }
        return actorList.size();
    }

    public class ViewHolde extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolde(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.actorName);
        }
    }
}
