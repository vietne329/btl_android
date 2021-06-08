package com.example.chattingapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.chattingapp.Adapter.NguoiDungAdapter;
import com.example.chattingapp.Model.NguoiDung;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NguoiDungFragment extends Fragment {

    private RecyclerView recyclerView;
    private NguoiDungAdapter nguoiDungAdapter;
    private List<NguoiDung> mNguoiDungs;

    EditText search_users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_users,container,false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mNguoiDungs = new ArrayList<>();
        readUsers();

        search_users = view.findViewById(R.id.search_user);
        search_users.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchUsers(s.toString().toLowerCase());
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    private void searchUsers(String s) {

        FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mNguoiDungs.clear();
                for(DataSnapshot data: snapshot.getChildren()){
                    NguoiDung nguoiDung = data.getValue(NguoiDung.class);

                    if(!nguoiDung.getId().equals(fuser.getUid())){
                        mNguoiDungs.add(nguoiDung);
                    }
                }

                nguoiDungAdapter = new NguoiDungAdapter(getContext(), mNguoiDungs,false);
                recyclerView.setAdapter(nguoiDungAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUsers() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (search_users.getText().toString().equals("")) {

                    mNguoiDungs.clear();
                    for (DataSnapshot data : snapshot.getChildren()) {
                        NguoiDung nguoiDung = data.getValue(NguoiDung.class);

                        assert nguoiDung != null;
                        assert firebaseUser != null;
                        if (!nguoiDung.getId().equals(firebaseUser.getUid())) {
                            mNguoiDungs.add(nguoiDung);
                        }
                    }
                    nguoiDungAdapter = new NguoiDungAdapter(getContext(), mNguoiDungs, false);
                    recyclerView.setAdapter(nguoiDungAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}