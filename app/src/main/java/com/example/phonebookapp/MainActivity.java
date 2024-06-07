package com.example.phonebookapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phonebookapp.databinding.ActivityMainBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    // firebase
    FirebaseDatabase database;
    DatabaseReference reference;

    private ActivityMainBinding mainBinding;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private ArrayList<User> userArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        database = FirebaseDatabase.getInstance();
        reference = database.getReference("Users");

        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        userArrayList = new ArrayList<>();

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User newUser = snapshot.getValue(User.class);
                if (newUser != null) {
                    if (previousChildName == null) {
                        userArrayList.add(0, newUser);
                        adapter.notifyItemInserted(0);
                    } else {
                        int prevIndex = findUserIndexById(previousChildName);
                        int newIndex = prevIndex + 1;
                        userArrayList.add(newIndex, newUser);
                        adapter.notifyItemInserted(newIndex);
                    }
                }
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    int index = findUserIndexById(user.getId());
                    if (index != -1) {
                        userArrayList.set(index, user);
                        adapter.notifyItemChanged(index);
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    int index = findUserIndexById(user.getId());
                    if (index != -1) {
                        userArrayList.remove(index);
                        adapter.notifyItemRemoved(index);
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User movedUser = snapshot.getValue(User.class);
                if (movedUser != null) {
                    int index = findUserIndexById(movedUser.getId());
                    if (index != -1) {
                        userArrayList.remove(index);
                        adapter.notifyItemRemoved(index);
                    }

                    if (previousChildName == null) {
                        userArrayList.add(0, movedUser);
                        adapter.notifyItemInserted(0);
                    } else {
                        int prevIndex = findUserIndexById(previousChildName);
                        int newIndex = prevIndex + 1;
                        userArrayList.add(newIndex, movedUser);
                        adapter.notifyItemInserted(newIndex);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error:", error.getMessage());
            }
        });

        adapter = new MyAdapter(this, userArrayList);
        recyclerView = mainBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private int findUserIndexById(String userId) {
        if (userId == null) {
            return -1;
        }
        for (User user : userArrayList) {
            if (userId.equals(user.getId())) {
                return userArrayList.indexOf(user);
            }
        }
        return -1;
    }
}
