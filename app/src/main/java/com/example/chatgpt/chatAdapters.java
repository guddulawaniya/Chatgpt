package com.example.chatgpt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class chatAdapters extends RecyclerView.Adapter<chatAdapters.viewholder> {

  List<modules> list;

    public chatAdapters(List<modules> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_layout,null);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewholder holder, int position) {

        modules module = list.get(position);
        if (module.getSendby().equals(module.SEND_BY_ME))
        {
            holder.leftview.setVisibility(View.GONE);
            holder.questionsms.setText(module.getMessage());

        }
        else
        {
            holder.rightview.setVisibility(View.GONE);
            holder.responsesms.setText(module.getMessage());

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class viewholder extends RecyclerView.ViewHolder {

        TextView questionsms,responsesms;

        LinearLayout rightview,leftview;

        public viewholder(@NonNull View itemView) {
            super(itemView);
            questionsms = itemView.findViewById(R.id.questionsms);
            responsesms = itemView.findViewById(R.id.idTVResponse);
            rightview = itemView.findViewById(R.id.rightview);
            leftview = itemView.findViewById(R.id.leftview);
        }
    }
}
