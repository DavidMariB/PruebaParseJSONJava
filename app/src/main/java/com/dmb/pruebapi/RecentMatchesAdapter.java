package com.dmb.pruebapi;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by davidmari on 30/11/17.
 */

public class RecentMatchesAdapter extends RecyclerView.Adapter<RecentMatchesAdapter.AnimeViewHolder> {
    private List<Match> items;

    public static class AnimeViewHolder extends RecyclerView.ViewHolder {
        // Campos respectivos de un item
        public ImageView champImg;
        public TextView score,result,champName,lane;

        public AnimeViewHolder(View v) {
            super(v);
            champImg = v.findViewById(R.id.champIcon);
            score = v.findViewById(R.id.matchScore);
            result = v.findViewById(R.id.matchResult);
            champName = v.findViewById(R.id.champName);
            lane = v.findViewById(R.id.matchLane);
        }
    }

    public RecentMatchesAdapter(List<Match> items) {
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public AnimeViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.activity_recent_matches, viewGroup, false);
        return new AnimeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AnimeViewHolder viewHolder, int i) {
        viewHolder.champImg.setImageResource(items.get(i).getChampImg());
        viewHolder.champName.setText(items.get(i).getChampName());
        viewHolder.score.setText("Resultado: "+String.valueOf(items.get(i).getScore()));
        viewHolder.result.setText(String.valueOf(items.get(i).getResult()));
        viewHolder.lane.setText("Linea: "+String.valueOf(items.get(i).getLane()));
    }
}
