package org.techtown.amatta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    public static final int REQUEST_CODE_UPDATE = 200;
    public static final String ID = "_id";

    static ArrayList<Todo> items = new ArrayList<Todo>();

    static long _id_;
    static int tff = 0;

    @NonNull
    @Override
    public TodoAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.todo_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TodoAdapter.ViewHolder holder, int position) {
        final Todo item = items.get(position);
        boolean tf = item.getTf();

        holder.checkBox.setOnCheckedChangeListener(null);
        holder.checkBox.setChecked(tf);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                _id_ = item.getId();
                if(isChecked) {
                    tff = 1;
                    holder.todo_text.setPaintFlags(holder.todo_text.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);  // 취소선 긋기
                    holder.todo_text.setTextColor((Color.parseColor("#9E9E9E")));  // 텍스트 색 회색으로 변경
                    holder.todo_text.setTypeface(null, Typeface.ITALIC);  // 스타일 기울이기

                    holder.todo_time.setPaintFlags(holder.todo_text.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);  // 취소선 긋기
                    holder.todo_time.setTextColor((Color.parseColor("#9E9E9E")));  // 텍스트 색 회색으로 변경
                    holder.todo_time.setTypeface(null, Typeface.ITALIC);  // 스타일 기울이기
                } else {
                    tff = 0;
                    holder.todo_text.setPaintFlags(0);  // 최소선 없애기
                    holder.todo_text.setTextColor((Color.parseColor("#000000")));  // 텍스트 색 검정
                    holder.todo_text.setTypeface(null, Typeface.NORMAL);  // 스타일 기본

                    holder.todo_time.setPaintFlags(0);  // 최소선 없애기
                    holder.todo_time.setTextColor((Color.parseColor("#000000")));  // 텍스트 색 검정
                    holder.todo_time.setTypeface(null, Typeface.NORMAL);  // 스타일 기본
                }
            }
        });

        holder.setItem(item);
        holder.setLayout();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(ArrayList<Todo> items){this.items = items;}

    static class ViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout layoutTodo;
        CheckBox checkBox;
        TextView todo_text, todo_time, emoji;

        public ViewHolder(View itemView){
            super(itemView);

            layoutTodo = itemView.findViewById(R.id.todo_info);
            checkBox = itemView.findViewById(R.id.checkBox);
            todo_text = itemView.findViewById(R.id.todo_text);
            todo_time = itemView.findViewById(R.id.todo_time);
            emoji = itemView.findViewById(R.id.emoji);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getLayoutPosition();
                    Todo item;
                    long id = 0;

                    if(pos != RecyclerView.NO_POSITION) {
                        item = items.get(pos);
                        id = item.getId();
                    }

                    Intent intent = new Intent(v.getContext(), CreateActivity.class);
                    intent.putExtra(ID, id);
                    ((Activity) v.getContext()).startActivityForResult(intent, REQUEST_CODE_UPDATE);
                }
            });
        }

        public void setItem(Todo item){

            int sh = item.getStart_h();
            int sm = item.getStart_m();
            int eh = item.getEnd_h();
            int em = item.getEnd_m();
            boolean tf = item.getTf();
            String startTime;
            String endTime;

            // 시작시간
            if(sm < 10) {
                startTime = sh + ":0" + sm;
            } else {
                startTime = sh + ":" + sm;
            }
            // 종료시간
            if(em < 10) {
                endTime = eh + ":0" + em;
            } else {
                endTime = eh + ":" + em;
            }

            if(tf) {
                todo_text.setPaintFlags(todo_text.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                todo_text.setTextColor((Color.parseColor("#9E9E9E")));
                todo_text.setTypeface(null, Typeface.ITALIC);

                todo_time.setPaintFlags(todo_text.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                todo_time.setTextColor((Color.parseColor("#9E9E9E")));
                todo_time.setTypeface(null, Typeface.ITALIC);
            } else {
                todo_text.setPaintFlags(0);
                todo_text.setTextColor((Color.parseColor("#000000")));
                todo_text.setTypeface(null, Typeface.NORMAL);

                todo_time.setPaintFlags(0);
                todo_time.setTextColor((Color.parseColor("#000000")));
                todo_time.setTypeface(null, Typeface.NORMAL);
            }
            checkBox.setChecked(tf);
            todo_time.setText(startTime + " ~ " + endTime);
            todo_text.setText(item.getTitle());
            emoji.setText(item.getEmoji());
        }

        public void setLayout(){layoutTodo.setVisibility(View.VISIBLE);}

    }

}

