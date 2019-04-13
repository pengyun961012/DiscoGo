package com.disco.skeletalproduct;

import android.app.Application;
import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProfileListAdapter extends RecyclerView.Adapter<ProfileListAdapter.ViewHolder> {
    private static String TAG = "DISCO_SKELETAL----- profile";

    private static List<Profile> items;
    private static Context context;
    private final ClickListener listener;
    private static MediaPlayer mp = new MediaPlayer();
    private static int clicked = -1;

    public ProfileListAdapter(List<Profile> items, Context context, ClickListener listener) {
        this.items = items;
        this.context = context;
        this.listener = listener;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        return new ViewHolder(v, listener);
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        Profile item = items.get(position);
        holder.songNameView.setText(item.getSongName());
        holder.songScoreView.setText(Integer.toString(item.getSongScore()));
        String duration = Integer.toString(item.getSongDurationMinute()) + " : " + Integer.toString(item.getSongDurationSecond());
        holder.songDurationView.setText(duration);

        /**Time*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm");
        Date mDate = Calendar.getInstance().getTime();
        try{
            mDate = dateFormat.parse(item.getSongTime());
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        long fileTime = mDate.getTime();
        long now = System.currentTimeMillis();
        CharSequence niceDateStr = DateUtils.getRelativeTimeSpanString(fileTime, now,
                0L, DateUtils.FORMAT_ABBREV_ALL);
        holder.songTimeView.setText(niceDateStr);


        holder.itemView.setTag(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView songNameView;
        public TextView songScoreView;
        public TextView songDurationView;
        public TextView songTimeView;
        public ImageButton songPlayButton;
        public ImageButton songDeleteButton;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);

            songNameView = itemView.findViewById(R.id.profileSongNametextView);
            songScoreView = itemView.findViewById(R.id.profileScoretextView);
            songDurationView = itemView.findViewById(R.id.profileDurationtextView);
            songTimeView = itemView.findViewById(R.id.profileTimetextView);
            songPlayButton = itemView.findViewById(R.id.playMusicImageButton);
            songDeleteButton = itemView.findViewById(R.id.deleteButton);

            songPlayButton.setOnClickListener(this);
            songDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == songPlayButton.getId()) {
                int index = getAdapterPosition();
                if (clicked == index || clicked == -1) {
                    clicked = index;
                    try {
                        String songName = songNameView.getText().toString();
////                        Log.d(TAG, "onClick: songname " + songName);
                        String time = songTimeView.getText().toString();
                        String filename = songName + "_" + time;
//                        Log.d(TAG, "onClick: " + filename);
                        mp.setDataSource(context.getFilesDir() + "/" + filename);
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        mp.prepare();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                if (items.get(index).isPlayed()) {
                    if (mp.isPlaying()) {
//                    Toast.makeText(v.getContext(), "Pause ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        songPlayButton.setImageResource(R.drawable.resume);
                        mp.pause();
                    } else {
//                    Toast.makeText(v.getContext(), "Play ITEM PRESSED = " + String.valueOf(getAdapterPosition()), Toast.LENGTH_SHORT).show();
                        songPlayButton.setImageResource(R.drawable.pause_black);
                        mp.start();
                    }
//                items.get(index).setPlayed(!items.get(index).isPlayed());

                    // set up files here
                    mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mp) {
                            try {
                                mp.stop();
                                songPlayButton.setImageResource(R.drawable.resume);
                                clicked = -1;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            }
            else if (v.getId() == songDeleteButton.getId()){
                int index = getAdapterPosition();
                String songName = songNameView.getText().toString();
                String time = songTimeView.getText().toString();
                String filename = songName + "_" + time;
                File fdelete = new File(context.getFilesDir(), filename);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Log.d(TAG, "onClick: file Deleted :" + filename);
                    } else {
                        Log.d(TAG, "onClick: file Not Deleted :" + filename);
                    }
                }
                removeFile(index);
            }

//            listenerRef.get().onPositionClicked(getAdapterPosition());
//            File rawdata = new File(v.getContext().getFilesDir(), "lrz");
//            File wavdata = new File(v.getContext().getFilesDir(), "lrz.wav");
//            try {
//                rawToWave(rawdata, wavdata);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }

    }

    public void removeFile(int position){
        items.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, items.size());
    }

    private static void rawToWave(final File rawFile, final File waveFile) throws IOException {

        byte[] rawData = new byte[(int) rawFile.length()];
        DataInputStream input = null;
        try {
            input = new DataInputStream(new FileInputStream(rawFile));
            input.read(rawData);
        } finally {
            if (input != null) {
                input.close();
            }
        }

        DataOutputStream output = null;
        try {
            output = new DataOutputStream(new FileOutputStream(waveFile));
            // WAVE header
            // see http://ccrma.stanford.edu/courses/422/projects/WaveFormat/
            writeString(output, "RIFF"); // chunk id
            writeInt(output, 36 + rawData.length); // chunk size
            writeString(output, "WAVE"); // format
            writeString(output, "fmt "); // subchunk 1 id
            writeInt(output, 16); // subchunk 1 size
            writeShort(output, (short) 1); // audio format (1 = PCM)
            writeShort(output, (short) 1); // number of channels
            writeInt(output, 44100); // sample rate
            writeInt(output, 44100 * 2); // byte rate
            writeShort(output, (short) 2); // block align
            writeShort(output, (short) 16); // bits per sample
            writeString(output, "data"); // subchunk 2 id
            writeInt(output, rawData.length); // subchunk 2 size
            // Audio data (conversion big endian -> little endian)
            short[] shorts = new short[rawData.length / 2];
            ByteBuffer.wrap(rawData).order(ByteOrder.LITTLE_ENDIAN).asShortBuffer().get(shorts);
            ByteBuffer bytes = ByteBuffer.allocate(shorts.length * 2);
            for (short s : shorts) {
                bytes.putShort(s);
            }

            output.write(fullyReadFileToBytes(rawFile));
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }
    static byte[] fullyReadFileToBytes(File f) throws IOException {
        int size = (int) f.length();
        byte bytes[] = new byte[size];
        byte tmpBuff[] = new byte[size];
        FileInputStream fis= new FileInputStream(f);
        try {

            int read = fis.read(bytes, 0, size);
            if (read < size) {
                int remain = size - read;
                while (remain > 0) {
                    read = fis.read(tmpBuff, 0, remain);
                    System.arraycopy(tmpBuff, 0, bytes, size - remain, read);
                    remain -= read;
                }
            }
        }  catch (IOException e){
            throw e;
        } finally {
            fis.close();
        }

        return bytes;
    }
    private static void writeInt(final DataOutputStream output, final int value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
        output.write(value >> 16);
        output.write(value >> 24);
    }

    private static void writeShort(final DataOutputStream output, final short value) throws IOException {
        output.write(value >> 0);
        output.write(value >> 8);
    }

    private static void writeString(final DataOutputStream output, final String value) throws IOException {
        for (int i = 0; i < value.length(); i++) {
            output.write(value.charAt(i));
        }
    }
}
