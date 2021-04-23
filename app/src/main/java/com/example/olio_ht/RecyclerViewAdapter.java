package com.example.olio_ht;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.widget.Toast.makeText;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<calorieEntry> items = new ArrayList<calorieEntry>();
    private OnTextClickListener listener;
    private Context context;

    public RecyclerViewAdapter(ArrayList<calorieEntry> rv_array, OnTextClickListener listener) {
        items = rv_array;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.item.setText(items.get(position).getInfo());

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence text = "Hold to remove item";
                Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
            }
        });

        holder.parentLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String className;
                className = items.get(0).getClass().getSimpleName();
                listener.getClass(className);
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, items.size());
                holder.itemView.setVisibility(View.GONE);
                listener.onTextClick(items);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList getArray() { return items; }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView item;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_data);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }
    }

    interface OnTextClickListener {
        void onTextClick(ArrayList<calorieEntry> array);
        void getClass(String cN);
    }
}

