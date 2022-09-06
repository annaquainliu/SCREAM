package com.Klymene.SCREAM;

import android.os.Handler;

import java.util.HashMap;
import java.util.Objects;

import pl.droidsonroids.gif.GifImageView;

public class Screamie {

    private static class Info {
        private final int id;
        private final float time;
        public Info(int i, float t) {
            id = i;
            time = t;
        }
        public int getId() {
            return id;
        }
        public float getTime() {
            return time;
        }
    }

    private final GifImageView imageView;
    private HashMap<String, Info> hashMap;
    private String previousAnimation = "idle";
    private boolean animationOccurring = false;
    private final Handler handler;

    public Screamie(GifImageView v) {
        handler = new Handler();
        imageView = v;
        initializeHashmap();
    }

    private void initializeHashmap() {
        Integer[] ids = new Integer[] {R.drawable.angry, R.drawable.idle, R.drawable.scream1,
                R.drawable.scream2, R.drawable.scream3, R.drawable.talk1, R.drawable.talk2,
                R.drawable.talk3};
        String[] names = {"angry", "idle", "scream1", "scream2", "scream3", "talk1",
                "talk2", "talk3"};
        Float[] times = {1.92f, 3.33f, 0.33f, 0.2f, 0.38f, 0.25f, 0f, 0.25f};
        hashMap = new HashMap<>();
        for (int i = 0; i < ids.length; i++) {
            hashMap.put(names[i], new Info(ids[i], times[i]));
        }
    }

    private void waitWhileOpeningMouth(float seconds) {
        animationOccurring = true;
        handler.postDelayed(() -> {
            if (!Objects.equals(previousAnimation, "angry") &&
                    !Objects.equals(previousAnimation, "idle")) {
                imageView.setImageResource(Objects.requireNonNull(hashMap.get
                        (replaceLastChar(previousAnimation, '2'))).getId());
            }
            else {
                imageView.setImageResource(Objects.requireNonNull(hashMap.get
                        (previousAnimation)).getId());
            }
            animationOccurring = false;
        }, (long) seconds * 1000);
    }

    private void waitClosingMouth(float seconds) {
        animationOccurring = true;
        handler.postDelayed(() -> {
            animationOccurring = false;
            imageView.setImageResource(Objects.requireNonNull(hashMap.get("idle")).getId());
        }, (long) seconds * 1000);
    }

    public void setFinalState() {
        handler.postDelayed(() -> {
            changeView("idle");
        }, 500);
    }

    public void changeView(String state) {
        if (hashMap.get(state) == null || animationOccurring ||
                Objects.equals(state, previousAnimation)) {
            return;
        }
        //closing mouth
        if (Objects.equals(state, "idle")) {
            String updatedState = previousAnimation;
            if (!Objects.equals(previousAnimation, "angry")) {
                updatedState = replaceLastChar(previousAnimation, '3');
            }
            imageView.setImageResource(Objects.requireNonNull(hashMap.get(updatedState)).getId());
            waitClosingMouth(Objects.requireNonNull(hashMap.get(updatedState)).getTime());
        }

        previousAnimation = state;

        if (!animationOccurring) {
            //opening mouth
            imageView.setImageResource(Objects.requireNonNull(hashMap.get(state)).getId());
            waitWhileOpeningMouth(Objects.requireNonNull(hashMap.get(state)).getTime());
        }

    }

    private String replaceLastChar(String s, Character c) {
        return s.replace(s.charAt(s.length() - 1), c);
    }
}
