package com.programmerbaper.focus.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.programmerbaper.focus.entities.Todo;
import com.programmerbaper.fokus.R;

import java.util.List;


public class TodoRecycleAdapter extends RecyclerView.Adapter<TodoRecycleAdapter.TodoViewHolder> {

    private final String LOG_TAG = TodoRecycleAdapter.class.getName();

    private Context mContext;
    private List<Todo> mTodos;

    public TodoRecycleAdapter(Context mContext, List<Todo> mTodos) {
        this.mContext = mContext;
        this.mTodos = mTodos;
    }

    @Override
    public TodoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.each_todo, parent, false);
        return new TodoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TodoViewHolder holder, int position) {
        Todo todoNow = mTodos.get(position);
        holder.mNama.setText(todoNow.getName());

    }

    @Override
    public int getItemCount() {
        return mTodos.size();
    }

    public static class TodoViewHolder extends RecyclerView.ViewHolder {
        private TextView mNama, mTarif, mTanggal;
        private View mRootView;

        public TodoViewHolder(View itemView) {
            super(itemView);
            mNama = itemView.findViewById(R.id.namaTodo);
            mTarif = itemView.findViewById(R.id.tarif);
            mRootView = itemView;
        }
    }


}
