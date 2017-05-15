package me.tittojose.musicviewcontrol;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import me.tittojose.musicviewcontrol.customview.MusicPreviewControl;
import me.tittojose.musicviewcontrol.model.MusicTune;


public class MusicPreviewRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int playingPosition = -1;
    private Context mContext;
    private List<MusicTune> musicTuneList = new ArrayList<>();
    private BaselineMusicPlayer baselineMusicPlayer;


    public MusicPreviewRecyclerAdapter(Context context) {
        mContext = context;
        for (int i = 0; i < 20; i++) {
            musicTuneList.add(new MusicTune());
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.preview_list_item, parent, false);
        return new ItemViewHolder(view);

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final int exatPos = holder.getAdapterPosition();
        MusicTune item = musicTuneList.get(holder.getAdapterPosition());
        ItemViewHolder listViewHolder = (ItemViewHolder) holder;
        listViewHolder.musicPreviewControl.setOnClickListener(new MusicPreviewControl.OnClickListener() {
            @Override
            public void onPlayButtonClicked() {
                if (playingPosition == -1) {
                    // start playing
                    playingPosition = exatPos;
                    baselineMusicPlayer = BaselineMusicPlayer.getInstance();
                    baselineMusicPlayer.setMusicUrl("https://ia802508.us.archive.org/5/items/testmp3testfile/mpthreetest.mp3");
                    baselineMusicPlayer.setPreviewListener(previewListener);
                    baselineMusicPlayer.startMusic(mContext);

                } else {
                    // stop playing
                    baselineMusicPlayer.stopMusic();
                }
            }

            @Override
            public void onStopButtonClicked() {

            }
        });

        // Playing
        if (item.isPreviewBuffering) {
            listViewHolder.musicPreviewControl.setBuffering();
        } else if (item.isPreviewPlaying) {
            listViewHolder.musicPreviewControl.setPlaying(item.previewProgress);

        } else {
            listViewHolder.musicPreviewControl.stopPlaying();
        }
    }

    @Override
    public int getItemCount() {
        return musicTuneList.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {

        private MusicPreviewControl musicPreviewControl;

        public ItemViewHolder(View itemView) {
            super(itemView);
            musicPreviewControl = (MusicPreviewControl) itemView.findViewById(R.id.musicPreviewViewItem);
        }
    }

    private BaselineMusicPlayer.MusicPreviewListener previewListener = new BaselineMusicPlayer.MusicPreviewListener() {
        @Override
        public void onPreviewPlaying(int progressStatus) {
            MusicTune musicTune = musicTuneList.get(playingPosition);
            musicTune.isPreviewPlaying = true;
            musicTune.isPreviewBuffering = false;
            musicTune.previewProgress = (int) (progressStatus * 3.60);
            notifyDataSetChanged();
        }

        @Override
        public void onPreviewBuffering() {
            MusicTune musicTune = musicTuneList.get(playingPosition);
            musicTune.isPreviewPlaying = false;
            musicTune.isPreviewBuffering = true;
            musicTune.previewProgress = 0;
            notifyDataSetChanged();
        }

        @Override
        public void onPreviewStopped() {
            MusicTune musicTune = musicTuneList.get(playingPosition);
            musicTune.isPreviewPlaying = false;
            musicTune.isPreviewBuffering = false;
            musicTune.previewProgress = 0;
            playingPosition = -1;
            notifyDataSetChanged();
        }

        @Override
        public void onPreviewError() {
            MusicTune musicTune = musicTuneList.get(playingPosition);
            musicTune.isPreviewPlaying = false;
            musicTune.isPreviewBuffering = false;
            musicTune.previewProgress = 0;
            playingPosition = -1;
            notifyDataSetChanged();
        }
    };


}
