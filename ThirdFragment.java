package com.Klymene.SCREAM;

import static android.graphics.Color.rgb;

import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.RotateAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.helper.widget.Flow;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.fragment.app.Fragment;

import com.Klymene.SCREAM.databinding.FragmentThirdBinding;
import com.google.android.material.textview.MaterialTextView;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThirdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThirdFragment extends Fragment {

    private FragmentThirdBinding binding;
    private static final String ARG_PARAM1 = "textDisplay";
    private Random r;
    private Typeface font;
    private ViewGroup.LayoutParams cParams;
    private int screenWidth, screenHeight, numOutOfFrame = 0, initalChildCount = 0, childCount;
    private TypedArray idArray;
    private AlphaAnimation animation1;
    private HashMap<View, Letter> outOfFrame = null;
    public MediaRecorder mediaRecorder;
    private Handler mHandler;
    private boolean recordingReady = false;
    private MediaPlayer mediaPlayer;

    private Screamie screamBuddy;

    public ThirdFragment() {
        // Required empty public constructor
    }

    private class Letter {
        public boolean isOutOfFrame;
        public int xvelocity;
        public int yvelocity;

        public Letter(int pos) {
            isOutOfFrame = false;
            xvelocity = r.nextInt(3) - 1;
            yvelocity = r.nextInt(3) - 1;
            if (pos == 1) {
                xvelocity = -1;
            }
            else if (pos == 2) {
                yvelocity = 1;
            }
            else if (pos == 3) {
                yvelocity = -1;
            }
            else if (pos == 4) {
                xvelocity = 1;
            }
        }


    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ThirdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ThirdFragment newInstance(String param1) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        r = new Random();
        font = ResourcesCompat.getFont(this.requireContext(), R.font.fredoka_one);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
        cParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        idArray = getResources().obtainTypedArray(R.array.idArray);
        animation1 = new AlphaAnimation(1f, 0f);
        animation1.setDuration(1000);
        mHandler = new Handler();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAudio();
            }
        });
        binding.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finished();
            }
        });
        binding.textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shake(0.5);
            }
        });
        binding.textView3.setVisibility(View.INVISIBLE);
        screamBuddy = new Screamie(binding.gifImageView);
    }

    private void bounceallTexts(double decibel) {
        int children = binding.constraintLayout.getChildCount();
        for (int i = 0; i < children - 1; i++) {
            View view = binding.constraintLayout.getChildAt(i);
            if (outOfFrame.containsKey(view) &&
                    Boolean.FALSE.equals(outOfFrame.get(view).isOutOfFrame)) {
                bounceText(decibel, view);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
//        return inflater.inflate(R.layout.fragment_third, container, false);
    }

    public void changeViewText(String message) {
        binding.textView3.setText(R.string.begin);
        binding.textView3.setTextColor(rgb(105, 172, 149));
        binding.textView3.setVisibility(View.VISIBLE);
        if (Objects.equals(message, "")) {
            return;
        }
        recordingReady = false;
        numOutOfFrame = 0;
        initalChildCount = message.length();
        childCount = initalChildCount;
        if (outOfFrame != null) {
            outOfFrame.clear();
        }
        screamBuddy.changeView("idle");
        recordAudio();
        outOfFrame = new HashMap<View, Letter>();
        for (int i = 0; i < message.length(); i++) {
            changeText(message, i);
            if (message.charAt(i) == ' ') {
                initalChildCount--;
            }
        }
    }

    private void changeText(String message, int i) {
        TextView character = new MaterialTextView(getContext());
        character.setLayoutParams(cParams);
        character.setText("" + message.charAt(i));
        character.setTextSize(30);
        character.setTypeface(Typeface.DEFAULT_BOLD);
        character.setTypeface(font);
        character.setTextColor(rgb(r.nextInt(30) + 225,
                r.nextInt(30) + 225, r.nextInt(30) + 225));
        character.setId(idArray.getResourceId(i, 0));
        binding.constraintLayout.addView(character, 0);
        if (binding.constraintLayout.getChildAt(0) instanceof TextView) {
            binding.flow.addView(binding.constraintLayout.getChildAt(0));
        }
        int position;
        if (message.charAt(i) != ' ') {
            if (i <= childCount / 4) {
                position = 1;
            }
            else if (i < childCount / 2) {
                position = 2;
            }
            else if( i < (childCount * 3) / 4) {
                position = 3;
            }
            else {
                position = 4;
            }
            outOfFrame.put(character, new Letter(position));
        }
    }

    public void removeLetters() {
        ConstraintLayout clayout = binding.constraintLayout;
        int childCount = clayout.getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            removeLetter(clayout.getChildAt(0));
        }

    }

    private void removeLetter(View view) {
        view.startAnimation(animation1);
        binding.flow.removeView(view);
        binding.constraintLayout.removeView(view);
    }

    private void bounceText(double volume, View letter) {
        if (letter == null || letter instanceof Flow) {
            return;
        }
        if (letter.getAnimation() != null) {
            letter.getAnimation().cancel();
        }
        int xvelocity = 0, yvelocity = 0;
        if (outOfFrame.containsKey(letter)) {
            Letter temp = Objects.requireNonNull(outOfFrame.get(letter));
            xvelocity = temp.xvelocity;
            yvelocity = temp.yvelocity;
        }
        int range;
        if (volume <= 0.4) {
            range = (int) (volume * 250);
            screamBuddy.changeView("angry");
        }
        else if (volume <= 0.60){
            range = (int) (volume * 600);
            screamBuddy.changeView("talk1");
        }
        else {
            range = (int) (volume * 900);
            screamBuddy.changeView("scream1");
        }
        xvelocity = (r.nextInt(range) + range) * xvelocity;
        yvelocity = (r.nextInt(range) + range) * yvelocity;
        FlingAnimation fling = new FlingAnimation(letter, DynamicAnimation.X);
        FlingAnimation fling2 = new FlingAnimation(letter, DynamicAnimation.Y);
        fling.setStartVelocity(xvelocity).setFriction((float) (r.nextFloat() * (1/volume))).start();
        fling2.setStartVelocity(yvelocity).setFriction((float) (r.nextFloat() * (1/volume))).start();
        letter.animate().rotation(r.nextInt(600) - 300).setDuration(r.nextInt(4000)
                + 1000).start();
        outOfFrame((TextView) letter);
    }

    private void outOfFrame(TextView letter) {
        int[] coords = new int[2];
        letter.getLocationOnScreen(coords);
        Layout layout = letter.getLayout();
        if (layout == null) {
            return;
        }
        if (coords[0] < 96 || coords[0] > screenWidth || coords[1] < 0 || coords[1] > screenHeight) {
            if (letter.getText() != " " &&
                    Boolean.FALSE.equals(Objects.requireNonNull(outOfFrame.get(letter)).isOutOfFrame)) {
                numOutOfFrame++;
                Objects.requireNonNull(outOfFrame.get(letter)).isOutOfFrame = true;
                if (Objects.requireNonNull(outOfFrame.get(letter)).isOutOfFrame) {
                    Log.d("changed to true", "true");
                }
            }
        }
        if (numOutOfFrame == initalChildCount) {
            finished();
        }
    }

    private void finished() {
        recordingReady = true;
        binding.textView3.setText(R.string.Done);
        binding.textView3.setTextColor(rgb(105,172,149));
        removeLetters();
        stopRecording();
        screamBuddy.setFinalState();
        outOfFrame.clear();
    }

    private void shake(double decibel) {
        float magnitude = (float) (decibel * 20);
        RotateAnimation rotation = new RotateAnimation(0, magnitude,
                0.5f * binding.textView3.getWidth(), 0.5f *
                binding.textView3.getHeight());
        rotation.setDuration(100);
        rotation.setRepeatCount(10);
        rotation.setRepeatMode(AlphaAnimation.REVERSE);
        binding.textView3.startAnimation(rotation);
    }

    public void recordAudio() {
        try {
            if (mediaRecorder != null) {
                stopRecording();
            }
            stopAudio();
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mediaRecorder.setOutputFile(getFilePath());
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.prepare();
            mediaRecorder.start();
            if (getActivity() != null) {
                getActivity().runOnUiThread(mPollTask);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("recording started", "recording started");
    }

    private void stopRecording() {
        try {
                if (mediaRecorder == null) {
                    return;
                }
                mediaRecorder.stop();
                mediaRecorder.reset();
                mediaRecorder.release();
                mediaRecorder = null;
                Log.d("recording", "stopped");
                mHandler.removeCallbacks(mPollTask);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    private String getFilePath() {
        ContextWrapper contextWrapper = new ContextWrapper(getContext().getApplicationContext());
        File videoDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_DCIM);
        File file = new File(videoDirectory, "Camera");
        return file.getPath();
    }

    private void playAudio() {
        try {
            if (!recordingReady) {
                Toast.makeText(getContext(), "You haven't screamed yet!", Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            stopAudio();
            Log.d("playAudio", "playing");
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(getFilePath());
            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void stopAudio() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    private final Runnable mPollTask = new Runnable() {
        public void run() {
            double decibel = 0;
            if (mediaRecorder != null) {
                decibel = Math.log10(mediaRecorder.getMaxAmplitude() / 2700.0) / 1.5;
            }
            //Log.i("Noise", "runnable mPollTask");
            // Runnable(mPollTask) will again execute after POLL_INTERVAL
            if (mHandler != null) {
                int POLL_INTERVAL = 100;
                mHandler.postDelayed(mPollTask, POLL_INTERVAL);
            }
            if (decibel > 0.30) {
                if (binding.textView3.getText().toString().equals("Start Screaming!")) {
                    binding.textView3.setText(R.string.keepScreaming);
                    binding.textView3.setTextColor(rgb(239, 163, 198));
                }
                bounceallTexts(decibel);
                shake(decibel);
            }
            else {
                screamBuddy.changeView("idle");
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (outOfFrame != null) {
            outOfFrame.clear();
        }
        stopRecording();
        stopAudio();
        binding = null;
        mHandler = null;
        outOfFrame = null;
        r = null;
        cParams = null;
        animation1 = null;
        mediaRecorder = null;
        //remove videos from external storage
    }
}