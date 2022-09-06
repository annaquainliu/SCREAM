package com.Klymene.SCREAM;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FlingAnimation;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.Klymene.SCREAM.databinding.FragmentFirstBinding;
import com.google.android.material.textview.MaterialTextView;

import java.util.Random;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private String[] problems;
    private Random r;
    private FrameLayout.LayoutParams params;
    private Handler mhandler;
    private Runnable runnable;
    private Typeface font;
    private int duration;
    private Animation animation;
    private int screenHeight;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        runnable = new Runnable(){
            public void run() {
                duration = r.nextInt(2000) + 3000;
                spawnText();
                mhandler.postDelayed(this, duration);
            }
        };
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_firstFragment_to_activityFragment);
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        problems = new String[] {"my life is falling apart", "nobody loves me", "my ex dumped me",
        "i hate myself", "what is the point of life", "i'm worthless", "my boss sucks",
                "my roommate pisses me off daily", "my parents grounded me", "everyone hates me",
        "i am trash", "pollution is killing us", "our earth is slowly dying",
        "everything is terrible", "my parents gave me a curfew", "my crush doesn't like me"};
        r = new Random();
        params = new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        mhandler = new Handler();
        font = ResourcesCompat.getFont(this.requireContext(), R.font.fredoka_one);
        animation = new AlphaAnimation(1f, 0f);
        animation.setDuration(700);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
    }

    private void spawnText() {
        Log.d("running", "running");
        if (binding == null) {
            return;
        }
        if (binding.frameLayout.getChildCount() > 0) {
            removeFrameLayoutChildren();
        }
        for (int i = 0; i < r.nextInt(4); i++) {
            int index = r.nextInt(problems.length);
            TextView text = new MaterialTextView(requireContext());
            text.setText(problems[index]);
            text.setLayoutParams(params);
            text.setTypeface(font);
            text.setTextSize(20);
            text.setTextColor(Color.rgb(r.nextInt(250), r.nextInt(250),
                    r.nextInt(250)));
            binding.frameLayout.addView(text);
            floatAnimation(text);
        }
    }

    private void floatAnimation(TextView text) {
        int yvelocity = (r.nextInt(1000) + 800);
        FlingAnimation fling = new FlingAnimation(text, DynamicAnimation.Y);
        fling.setMinValue(-200).setMaxValue(screenHeight + 200);
        fling.setStartVelocity(yvelocity).setFriction(r.nextFloat() + 0.1f).start();
        text.animate().rotation(r.nextInt(600) + -300).setDuration(duration).start();
    }

    private void removeFrameLayoutChildren() {
        int childCount = binding.frameLayout.getChildCount();
        for (int i = 0; i < childCount; i++) {
            binding.frameLayout.getChildAt(0).startAnimation(animation);
            binding.frameLayout.removeViewAt(0);
        }
    }

    @Override
    public void onDestroyView() {
        binding = null;
        params = null;
        r = null;
        mhandler.removeCallbacks(runnable);
        mhandler = null;
        runnable = null;
        super.onDestroyView();
    }

}