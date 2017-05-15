package me.tittojose.musicviewcontrol;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;

public class BaselineMusicPlayer {

    private String TAG = BaselineMusicPlayer.class.getSimpleName();

    private String musicUrl;
    private Handler handler = new Handler();
    private Runnable mediaPlayerUpdateRunnable = new Runnable() {
        public void run() {
            if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                if (musicPreviewListener != null) {
                    primarySeekBarProgressUpdater();
                    int seekProgress = (int) (((float) mMediaPlayer.getCurrentPosition() / mediaFileLengthInMilliseconds) * 100);
                    Log.d(TAG, "Seek Percentage : " + seekProgress);
                    musicPreviewListener.onPreviewPlaying(seekProgress);
                }
            }
        }
    };

    interface MusicPreviewListener {

        void onPreviewPlaying(int progressStatus);

        void onPreviewBuffering();

        void onPreviewStopped();

        void onPreviewError();
    }

    private MediaPlayer mMediaPlayer = null;

    private static BaselineMusicPlayer baselineMusicPlayer;
    private MusicPreviewListener musicPreviewListener;

    private BaselineMusicPlayer() {
        mMediaPlayer = new MediaPlayer();
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    public static BaselineMusicPlayer getInstance() {
        if (baselineMusicPlayer != null) {
            return baselineMusicPlayer;
        }
        return new BaselineMusicPlayer();
    }

    public void setPreviewListener(MusicPreviewListener musicPreviewListener) {
        this.musicPreviewListener = musicPreviewListener;
    }

    public void setMusicUrl(String url) {
        this.musicUrl = url;
    }

    public void startMusic(Context context) {
        mediaFileLengthInMilliseconds = 0;
        try {
            mMediaPlayer.setDataSource(context, Uri.parse(musicUrl));
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "setMusicPreviewUrl: error");
        }
        mMediaPlayer.setOnPreparedListener(onPrepareListener);
        mMediaPlayer.setOnCompletionListener(onCompletedListener);
        mMediaPlayer.setOnBufferingUpdateListener(onBufferingUpdateListener);
        mMediaPlayer.setOnErrorListener(onErrorListener);
        mMediaPlayer.prepareAsync();
        if (musicPreviewListener != null) {
            musicPreviewListener.onPreviewBuffering();
        }
    }

    public void stopMusic() {
        handler.removeCallbacks(mediaPlayerUpdateRunnable);
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewStopped();
                musicPreviewListener = null;
            }
        }
    }

    private void primarySeekBarProgressUpdater() {
        // This math construction give a percentage of "was playing"/"song length"
        try {
            if (mediaFileLengthInMilliseconds > 0) {
                if (mMediaPlayer.isPlaying()) {
                    handler.postDelayed(mediaPlayerUpdateRunnable, 700);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int mediaFileLengthInMilliseconds;
    private MediaPlayer.OnPreparedListener onPrepareListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            Log.d(TAG, "onPrepared: ");
            mediaPlayer.start();
            mediaFileLengthInMilliseconds = mediaPlayer.getDuration();
            primarySeekBarProgressUpdater();
        }
    };

    private MediaPlayer.OnCompletionListener onCompletedListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            Log.d(TAG, "onCompletion: ");
            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewStopped();
            }
        }
    };

    private MediaPlayer.OnBufferingUpdateListener onBufferingUpdateListener = new MediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {

        }
    };

    private MediaPlayer.OnErrorListener onErrorListener = new MediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
            Log.e(TAG, "onError: ");
            if (musicPreviewListener != null) {
                musicPreviewListener.onPreviewError();
            }
            return false;
        }
    };

}
