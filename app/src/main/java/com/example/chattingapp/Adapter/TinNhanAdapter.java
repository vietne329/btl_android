package com.example.chattingapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chattingapp.Model.TinNhan;
import com.example.chattingapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class TinNhanAdapter extends RecyclerView.Adapter<TinNhanAdapter.ViewHolder> {

    public static final int TINNHAN_TRAI = 0;
    public static final int TINNHAN_PHAI = 1;
    private Context mContext;
    private List<TinNhan> mTinNhan;
    private String imageurl;

    FirebaseUser fuser;

    public TinNhanAdapter(Context mContext, List<TinNhan> mTinNhan, String imageurl){
        this.mContext = mContext;
        this.mTinNhan = mTinNhan;
        this.imageurl = imageurl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType == TINNHAN_PHAI){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right,parent,false);
            return new TinNhanAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left,parent,false);
            return new TinNhanAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TinNhanAdapter.ViewHolder holder, int position) {
        TinNhan tinNhan = mTinNhan.get(position);
        holder.show_message.setText(tinNhan.getMessage());
        if(imageurl.equals("default")){
            holder.profile_image.setImageResource(R.mipmap.ic_launcher);
        }else {
            Glide.with(mContext).load(imageurl).into(holder.profile_image);
        }
    }

    @Override
    public int getItemCount() {
        return mTinNhan.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profile_image;

        public ViewHolder(View itemView){
            super(itemView);
            show_message = itemView.findViewById(R.id.show_message);
            profile_image = itemView.findViewById(R.id.profile_image);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mTinNhan.get(position).getSender().equals(fuser.getUid())){
            return TINNHAN_PHAI;
        }else {
            return TINNHAN_TRAI;
        }
    }
}
