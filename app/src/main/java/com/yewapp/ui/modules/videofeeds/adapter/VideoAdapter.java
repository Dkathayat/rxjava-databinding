package com.yewapp.ui.modules.videofeeds.adapter;

import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.yewapp.R;

import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.myviewholder> {

    ArrayList objList;

    public VideoAdapter(@NonNull ArrayList<String> options) {
        objList = options;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_feeds, parent, false);
        return new myviewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull myviewholder holder, int position) {
        holder.setData((String) objList.get(position));
    }

    @Override
    public int getItemCount() {
        return objList.size();
    }

    public void updateList(ArrayList list) {
        objList.addAll(list);
        notifyDataSetChanged();
    }


    class myviewholder extends RecyclerView.ViewHolder {
        VideoView videoView;
        TextView title, desc;
        ProgressBar pbar;

        public myviewholder(@NonNull View itemView) {
            super(itemView);

            videoView = itemView.findViewById(R.id.idExoPlayerVIew);
            /*title = itemView.findViewById(R.id.textVideoTitle);
            desc = itemView.findViewById(R.id.textVideoDescription);
            pbar = itemView.findViewById(R.id.videoProgressBar);*/
        }

        void setData(String obj) {
            videoView.setVideoPath(obj);
            /*title.setText(obj.getTitle());
            desc.setText(obj.getDesc());*/

            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    pbar.setVisibility(View.GONE);
                    mediaPlayer.start();
                }
            });

            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.start();
                }
            });
        }
    }


}
