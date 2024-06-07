package com.example.phonebookapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonebookapp.databinding.UserCardBinding;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
    Context context;
    ArrayList<User> userArrayList;

    public MyAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UserCardBinding userCardBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.user_card,
                parent,
                false
        );
        return new MyViewHolder(userCardBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User currentUser = userArrayList.get(position);
        holder.userCardBinding.setUser(currentUser);
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private final UserCardBinding userCardBinding;

        public MyViewHolder(UserCardBinding userCardBinding) {
            super(userCardBinding.getRoot());
            this.userCardBinding = userCardBinding;

            userCardBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                }
            });
        }
    }
}
