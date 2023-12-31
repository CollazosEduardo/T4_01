package com.example.vj20231.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vj20231.R;
import com.example.vj20231.entities.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NameAdapter extends RecyclerView.Adapter {

    private List<User> items;

    public NameAdapter(List<User> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_string, parent, false);
        NameViewHolder viewHolder = new NameViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User item = items.get(position);
        View view = holder.itemView;

        TextView tvName = view.findViewById(R.id.tvName);
        TextView tvUsername = view.findViewById(R.id.tvUserName);
        TextView tvEmail = view.findViewById(R.id.Email);
        ImageView imageView = view.findViewById(R.id.imageView);
        tvName.setText(item.name);
        tvUsername.setText(item.username);
        tvEmail.setText(item.email);



        Picasso.get()
                .load(item.img) //
                .into(imageView);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class NameViewHolder extends RecyclerView.ViewHolder {

        public NameViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
