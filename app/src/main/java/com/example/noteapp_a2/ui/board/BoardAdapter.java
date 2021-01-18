package com.example.noteapp_a2.ui.board;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteapp_a2.OnItemClickListener;
import com.example.noteapp_a2.R;

import java.util.ArrayList;

public class BoardAdapter extends RecyclerView.Adapter<BoardAdapter.ViewHolder> {

    private String[] titles = new String[]{"Fast", "Free", "Powerful"};
    private String[] desc = new String[]{"fast", "free", "powerful"};
    private int[] images = new int[]{R.drawable.speshka, R.drawable.syr_v_myshelovke,
            R.drawable.znanie_sila};
    private OnItemClickListener onItemClickListener;

    public BoardAdapter() {

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pager_board, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textBoardTitle;
        private TextView textDesc;
        private ImageView imageViewBoard;
        private Button btnStart;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textBoardTitle = itemView.findViewById(R.id.textBoardTitle);
            textDesc = itemView.findViewById(R.id.textDesc);
            imageViewBoard = itemView.findViewById(R.id.imageView);
            btnStart = itemView.findViewById(R.id.btnStart);
            btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onClick(getAdapterPosition());
                }
            });
        }

        public void bind(int position) {
            textBoardTitle.setText(titles[position]);
            textDesc.setText(desc[position]);
            imageViewBoard.setImageResource(images[position]);
            btnStartOnLastPage(position);
        }

        private void btnStartOnLastPage(int position) {
            if (position == (titles.length - 1)) btnStart.setVisibility(View.VISIBLE);
            else btnStart.setVisibility(View.GONE);
        }
    }
}
