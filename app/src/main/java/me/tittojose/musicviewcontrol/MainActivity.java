package me.tittojose.musicviewcontrol;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import me.tittojose.musicviewcontrol.customview.MusicPreviewControl;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private BaselineMusicPlayer baselineMusicPlayer;
    private MusicPreviewControl musicPreviewControl;
    private RecyclerView recyclerView;
    private MusicPreviewRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musicPreviewControl = (MusicPreviewControl) findViewById(R.id.musicPreviewView);
        musicPreviewControl.setOnClickListener(previeControlClickListener);
        recyclerView = (RecyclerView) findViewById(R.id.rvMusicPreviewList);

        GridLayoutManager mGridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        recyclerView.setLayoutManager(mGridLayoutManager);
        adapter = new MusicPreviewRecyclerAdapter(MainActivity.this);
        recyclerView.setAdapter(adapter);

    }

    private MusicPreviewControl.OnClickListener previeControlClickListener = new MusicPreviewControl.OnClickListener() {
        @Override
        public void onPlayButtonClicked() {
            Log.d(TAG, "onPlayButtonClicked: ");
            baselineMusicPlayer = BaselineMusicPlayer.getInstance();
            baselineMusicPlayer.setMusicUrl("https://ia802508.us.archive.org/5/items/testmp3testfile/mpthreetest.mp3");
            baselineMusicPlayer.setPreviewListener(previewListener);
            baselineMusicPlayer.startMusic(MainActivity.this);
        }

        @Override
        public void onStopButtonClicked() {
            Log.d(TAG, "onStopButtonClicked: ");
            baselineMusicPlayer.stopMusic();
        }
    };

    private BaselineMusicPlayer.MusicPreviewListener previewListener = new BaselineMusicPlayer.MusicPreviewListener() {
        @Override
        public void onPreviewPlaying(int progressStatus) {
            int progress = (int) (progressStatus * 3.60);
            musicPreviewControl.setPlaying(progress);
        }

        @Override
        public void onPreviewBuffering() {
            musicPreviewControl.setBuffering();
        }

        @Override
        public void onPreviewStopped() {
            musicPreviewControl.stopPlaying();
        }

        @Override
        public void onPreviewError() {
            musicPreviewControl.stopPlaying();
        }
    };
}
