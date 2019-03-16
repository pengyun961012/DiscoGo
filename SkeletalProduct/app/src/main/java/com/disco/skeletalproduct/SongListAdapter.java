package com.disco.skeletalproduct;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.disco.flappybird.UnityPlayerActivity;
import com.unity3d.player.UnityPlayer;

import java.util.List;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class SongListAdapter extends PagerAdapter {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    Context context;
    private List<Song> songList;
    LayoutInflater layoutInflater;
    AudioDispatcher dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100,4096,0);

    public void stopRecorder() {
        dispatcher.stop();
    }

    public SongListAdapter(Context context, List<Song> songList) {
        this.context = context;
        this.songList = songList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return songList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.song_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.songImage);
        imageView.setImageResource(songList.get(position).getSongImage());

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songName = songList.get(position).getSongName();
//                Toast.makeText(context, "you clicked image " + songName, Toast.LENGTH_SHORT).show();
                AudioProcessor p = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, pdh);
                dispatcher.addAudioProcessor(p);

                new Thread(dispatcher, "Audio Dispatcher").start();
                Intent intent = new Intent(context,  UnityPlayerActivity.class);
                context.startActivity(intent);
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

    PitchDetectionHandler pdh = new PitchDetectionHandler(){
        @Override
        public void handlePitch(PitchDetectionResult result, AudioEvent e){
            final float pitchInHz = result.getPitch();
            ((PlayActivity)context).runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    double p = pitchInHz;
                    if (p <= -1.0) {
                        p = 150.0;
                    }
                    UnityPlayer.UnitySendMessage("BirdForeground", "receiveData", "" + (int) p);
//                    Log.d(TAG, "run: threadruning");
                }
            });
        }
    };
}
