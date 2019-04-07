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

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.io.IOException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.writer.WriterProcessor;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.TarsosDSPAudioFormat;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class SongListAdapter extends PagerAdapter {
    private String TAG = "DISCO_SKELETAL-----" + this.getClass().getSimpleName();
    Context context;
    private List<Song> songList;
    LayoutInflater layoutInflater;
    private String fileName;

    /** DSP */
    public static AudioDispatcher dispatcher;
    private int prevFreqLength = 7;
    int prevFreqIdx = 0;
    int [] prevFreq = new int[prevFreqLength];

    int[] weightFreq = new int[prevFreqLength];
    int weightFreqSum;
    double ratio = 1.1;


//    public void stopRecorder() {
//        Log.d(TAG, "stopRecorder: stop");
//        dispatcher.stop();
//    }

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
        for (int i=0; i<prevFreqLength; i++){
            weightFreq[i] = (int)Math.pow(ratio,(double)i);
            weightFreqSum += weightFreq[i];
        }

        View itemView = layoutInflater.inflate(R.layout.song_item, container, false);

        ImageView imageView = itemView.findViewById(R.id.songImage);
        imageView.setImageResource(songList.get(position).getSongImage());

        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String songName = songList.get(position).getSongName();
                Date today = Calendar.getInstance().getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
//                Toast.makeText(context, "you clicked image " + songName, Toast.LENGTH_SHORT).show();
                dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(44100,4096,0);
                TarsosDSPAudioFormat format = new TarsosDSPAudioFormat( TarsosDSPAudioFormat.Encoding.PCM_SIGNED, 44100, 16, 1, 2*1, 44100, ByteOrder.BIG_ENDIAN.equals(ByteOrder.nativeOrder()));
                //TarsosDSPAudioFormat format = new TarsosDSPAudioFormat( 44100, 16, 1, true, true);
                //TarsosDSPAudioFormat(TarsosDSPAudioFormat.Encoding encoding, float sampleRate, int sampleSizeInBits, int channels, int frameSize, float frameRate, boolean bigEndian)
                //TarsosDSPAudioFormat(float sampleRate, int sampleSizeInBits, int channels, boolean signed, boolean bigEndian)
                //File wavfile = new File(context.getFilesDir(), String.format("Testuser_%s.wav", Calendar.getInstance().getTime()));
                fileName = songName + "_" + dateFormat.format(today);
                File wavfile = new File(context.getFilesDir(), fileName);
                Log.e("file created at",context.getFilesDir().getAbsolutePath());
                try{
                    RandomAccessFile recordFile = new RandomAccessFile(wavfile, "rw");
                    AudioProcessor p1 = new WriterProcessor(format, recordFile);
                    dispatcher.addAudioProcessor(p1);
                }
                catch(Throwable e){
                    e.printStackTrace();
                }
                AudioProcessor p2 = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 44100, 4096, pdh);
                dispatcher.addAudioProcessor(p2);

                new Thread(dispatcher, "Audio Dispatcher").start();
                Intent intent = new Intent(context,  OverrideActivity.class);
                intent.putExtra("fileName", fileName);
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
                    int freq = (int)p;
                    prevFreq[prevFreqIdx] = freq;
                    prevFreqIdx++;
                    prevFreqIdx %= prevFreqLength;
                    if(freq==-1){
                        int sum =0;
                        for(int a = 0; a<prevFreqLength ;a++){
                            sum += prevFreq[(a+prevFreqIdx)%prevFreqLength] * weightFreq[a];

                        }
//                        freq= sum / prevFreqLength;
                        freq= sum / prevFreqLength /weightFreqSum;

                        //int[] frequenciesCopy = previousFreq.clone();
                        //Arrays.sort(frequenciesCopy);
                        //freq = previousFreq[previousFreqIdx/2];
                    }
                    if(freq<=120){
                        freq = 120;
                    }
                    UnityPlayer.UnitySendMessage("BirdForeground","receiveData",""+ freq);
//                    long time= System.currentTimeMillis();
//                    Log.e("Time Class ", " Time value in millisecinds "+time);
//                    text.setText(""+freq);
                }
            });
        }
    };
}
