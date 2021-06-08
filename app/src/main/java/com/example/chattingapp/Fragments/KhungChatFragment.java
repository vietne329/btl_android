package com.example.chattingapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chattingapp.Adapter.NguoiDungAdapter;
import com.example.chattingapp.Model.TinNhan;
import com.example.chattingapp.Model.NguoiDung;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class KhungChatFragment extends Fragment {

    private RecyclerView recyclerView;
    private NguoiDungAdapter nguoiDungAdapter;
    private List<NguoiDung> mNguoiDung;

    FirebaseUser fuser;
    DatabaseReference reference;

    private List<String> usersList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chats, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        usersList = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull  DataSnapshot snapshot) {
                usersList.clear();
                for (DataSnapshot data: snapshot.getChildren()){
                    TinNhan tinNhan = data.getValue(TinNhan.class);
                    if(tinNhan.getSender().equals(fuser.getUid())){
                        usersList.add(tinNhan.getReceiver());
                    }
                    if(tinNhan.getReceiver().equals(fuser.getUid())){
                        usersList.add(tinNhan.getSender());
                    }
                }

                readChats();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }


    private void readChats() {
        mNguoiDung = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mNguoiDung.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    NguoiDung nguoiDung = data.getValue(NguoiDung.class);

                        for (String id : usersList){
                            if(nguoiDung.getId().equals(id)){
                                if(mNguoiDung.size() != 0){
                                    for(NguoiDung nguoiDung1 : mNguoiDung){
                                        if(!nguoiDung.getId().equals(nguoiDung1.getId())){
                                            mNguoiDung.add(nguoiDung);
                                        }
                                    }
                                }else {
                                    mNguoiDung.add(nguoiDung);
                                }
                            }
                        }
                    }
                nguoiDungAdapter = new NguoiDungAdapter(getContext(), mNguoiDung,true);
                recyclerView.setAdapter(nguoiDungAdapter);
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}