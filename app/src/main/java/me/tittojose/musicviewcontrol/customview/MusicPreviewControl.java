package me.tittojose.musicviewcontrol.customview;


import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import me.tittojose.musicviewcontrol.R;

public class MusicPreviewControl extends RelativeLayout {

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onPlayButtonClicked();

        void onStopButtonClicked();
    }

    private boolean isBuffering;
    private boolean isPlaying;
    private int previewProgress;

    ProgressWheelIndicator progressWheelIndicator;
    ImageView playStatusImageView;


    public MusicPreviewControl(Context context) {
        super(context);
        initializeViews(context);
    }


    public MusicPreviewControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context);
    }

    public MusicPreviewControl(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeViews(context);
    }

    private void initializeViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.music_preview_control_layout, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        progressWheelIndicator = (ProgressWheelIndicator) findViewById(R.id.progressWheel);
        playStatusImageView = (ImageView) findViewById(R.id.grid_item_image_play_center);
        playStatusImageView.setImageResource(R.drawable.ic_play_big);

        progressWheelIndicator.setBarColor(ContextCompat.getColor(getContext(), R.color.progress_wheel_indicator));
        progressWheelIndicator.setRimColor(ContextCompat.getColor(getContext(), R.color.progress_wheel_ideal));
        progressWheelIndicator.setVisibility(View.INVISIBLE);

        playStatusImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((onClickListener != null) && (isPlaying || isBuffering)) {
                    onClickListener.onStopButtonClicked();
                } else if (onClickListener != null) {
                    onClickListener.onPlayButtonClicked();
                }
            }
        });

    }

    public void setBuffering() {

        isBuffering = true;
        isPlaying = false;

        playStatusImageView.setImageResource(R.drawable.ic_stop_big);
        progressWheelIndicator.setVisibility(View.VISIBLE);
        progressWheelIndicator.setSpinSpeed(5);
        progressWheelIndicator.spin();
    }

    public void stopPlaying() {

        isBuffering = false;
        isPlaying = false;

        playStatusImageView.setImageResource(R.drawable.ic_play_big);
        progressWheelIndicator.setVisibility(View.INVISIBLE);
        progressWheelIndicator.stopSpinning();
        progressWheelIndicator.incrementProgress(0);
    }

    public void setPlaying(int previewProgress) {

        isBuffering = false;
        isPlaying = true;

        playStatusImageView.setImageResource(R.drawable.ic_stop_big);
        progressWheelIndicator.setVisibility(View.VISIBLE);
        progressWheelIndicator.stopSpinning();
        progressWheelIndicator.incrementProgress(previewProgress);

    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}

