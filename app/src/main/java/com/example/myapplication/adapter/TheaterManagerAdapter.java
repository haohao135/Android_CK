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
import com.example.myapplication.fragment.Movie_Manager_Fragment;
import com.example.myapplication.fragment.Theater_Manager_Fragment;
import com.example.myapplication.model.Theater;

import java.util.List;

public class TheaterManagerAdapter extends RecyclerView.Adapter<TheaterManagerAdapter.ViewHolder> {
    List<Theater> theaterList;

    public TheaterManagerAdapter(List<Theater> theaterList) {
        this.theaterList = theaterList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.theater_item_manager, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Theater theater = theaterList.get(position);
        if(theater == null){
            return;
        }

        holder.theaterName.setText(theater.getName());
        holder.theaterAddress.setText(theater.getAddress());
        holder.itemView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                Theater_Manager_Fragment.theaterPosition = theaterList.get(position).getId();
                MenuInflater inflater = new MenuInflater(v.getContext());
                inflater.inflate(R.menu.menu_context_account, menu);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(!theaterList.isEmpty()){
            return theaterList.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView theaterName, theaterAddress;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            theaterName = itemView.findViewById(R.id.textView7);
            theaterAddress = itemView.findViewById(R.id.textView8);
        }
    }
}
