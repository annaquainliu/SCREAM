package com.Klymene.SCREAM;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.Klymene.SCREAM.databinding.FragmentSecondBinding;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    ActivityFragment parentFrag;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFrag = ((ActivityFragment)SecondFragment.this.getParentFragment());
        binding.testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (parentFrag != null) {
                    parentFrag.setMessage(getInputText());
                    parentFrag.selectTabs(1);
                }
            }
        });
    }

    public String getInputText() {
        return binding.TextInput.getText().toString();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}