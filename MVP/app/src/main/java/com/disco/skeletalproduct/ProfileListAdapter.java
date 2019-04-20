package com.disco.skeletalproduct;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
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
    private FirebaseStorage storage = FirebaseStorage.getInstance();
    private StorageReference recordingFolder = storage.getReference().child("recording");

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
//        holder.songDurationView.setText(duration);

        /**Time*/
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
        Date mDate = Calendar.getInstance().getTime();
        try{
            mDate = dateFormat.parse(item.getSongTime());
        }
        catch (ParseException e){
            e.printStackTrace();
        }
        long fileTime = mDate.getTime();
        long now = System.currentTimeMillis();
        Log.d(TAG, "onBindViewHolder: " + String.valueOf(now));
        CharSequence niceDateStr = DateUtils.getRelativeTimeSpanString(fileTime, now,
                0L, DateUtils.FORMAT_ABBREV_ALL);
        Log.d(TAG, "onBindViewHolder: " + niceDateStr);
        holder.songTimeView.setText(niceDateStr);

        holder.realTimeView.setText(item.getSongTime());

        holder.itemView.setTag(item);
    }

    @Override public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView songNameView;
        public TextView songScoreView;
//        public TextView songDurationView;
        public TextView songTimeView;
        public TextView realTimeView;
        public ImageButton songPlayButton;
        public ImageButton songDeleteButton;
        public ImageButton songShareButton;
        private WeakReference<ClickListener> listenerRef;

        public ViewHolder(View itemView, ClickListener listener) {
            super(itemView);
            listenerRef = new WeakReference<>(listener);

            songNameView = itemView.findViewById(R.id.profileSongNametextView);
            songScoreView = itemView.findViewById(R.id.profileScoretextView);
//            songDurationView = itemView.findViewById(R.id.profileDurationtextView);
            songTimeView = itemView.findViewById(R.id.profileTimetextView);
            songPlayButton = itemView.findViewById(R.id.playMusicImageButton);
            songDeleteButton = itemView.findViewById(R.id.deleteButton);
            songShareButton = itemView.findViewById(R.id.shareButton);
            realTimeView = itemView.findViewById(R.id.realtimeTextView);

            songPlayButton.setOnClickListener(this);
            songDeleteButton.setOnClickListener(this);
            songShareButton.setOnClickListener(this);
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
                        String time = realTimeView.getText().toString();
                        String score = songScoreView.getText().toString();
                        String filename = songName + "_" + time + "_" + score;
                        Log.d(TAG, "onClick: playsong " + filename);
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
                        mp.stop();
                        mp.reset();
                        clicked = -1;
//                        mp = null;
//                        mp = new MediaPlayer();
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
                                mp.reset();
//                                mp = null;
//                                mp = new MediaPlayer();
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
                String time = realTimeView.getText().toString();
                String score = songScoreView.getText().toString();
                String filename = songName + "_" + time + "_" + score;
                File fdelete = new File(context.getFilesDir(), filename);
                Log.d(TAG, "onClick: deleted file name " + fdelete);
                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Log.d(TAG, "onClick: file Deleted :" + filename);
                    } else {
                        Log.d(TAG, "onClick: file Not Deleted :" + filename);
                    }
                }
                removeFile(index);
            }
            else if (v.getId() == songShareButton.getId()){
                int index = getAdapterPosition();
                final String songName = songNameView.getText().toString();
                String time = realTimeView.getText().toString();
                String score = songScoreView.getText().toString();
                String filename = songName + "_" + time + "_" + score;
//                Toast.makeText(context, "Share", Toast.LENGTH_SHORT).show();
                try {
                    InputStream stream = new FileInputStream(new File(context.getFilesDir()+"/"+filename));
                    final StorageReference songRef = recordingFolder.child(filename);
                    UploadTask upTask = songRef.putStream(stream);
                    upTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("upload task","failed!");
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //share url
                            songRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    final String url = String.valueOf(uri);
                                    Log.d(TAG, "onSuccess: " + url);
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.putExtra(Intent.EXTRA_TEXT, "Here is my song "
                                            + songName + "\n" + url);
                                    intent.setType("text/plain");
                                    context.startActivity(Intent.createChooser(intent, "Send To"));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Handle any errors
                                }
                            });
                            //Log.d("upload task" ,String.valueOf(songRef.getDownloadUrl()));
                            Log.e("upload task", "success!");

                        }
                    });
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
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
}
