package com.Klymene.SCREAM;

import static com.google.android.material.tabs.TabLayout.Tab;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.Klymene.SCREAM.databinding.FragmentActivityBinding;
import com.google.android.material.tabs.TabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ActivityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ActivityFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentActivityBinding binding;
    private TabLayout tabs;
    FragmentManager fm;

    public ActivityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityFragment newInstance(String param1, String param2) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        fm = getChildFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.framelayout, new SecondFragment());
        ft.replace(R.id.framelayout2, new ThirdFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentActivityBinding.inflate(inflater, container, false);
        FragmentTransaction ft = fm.beginTransaction();
        binding.framelayout2.setVisibility(View.GONE);
//        return inflater.inflate(R.layout.fragment_activity, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabs = binding.tablayout;
        Log.d("CREATED ACTIVITY", "created!Y@!*@&*!&*!");
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(Tab tab) {
                // get the current selected tab's position and replace the fragment accordingly
                SwitchTabs(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void selectTabs(int position) {
        tabs.selectTab(tabs.getTabAt(position));
    }

    public void setMessage(String msg) {
        if (fm.getFragments().get(1) instanceof ThirdFragment) {
            ThirdFragment thirdFrag = (ThirdFragment) fm.getFragments().get(1);
            thirdFrag.removeLetters();
            thirdFrag.changeViewText(msg);
        }

    }

    public void SwitchTabs(int position) {
        switch (position) {
            case 0:
                binding.framelayout.setVisibility(View.VISIBLE);
                binding.framelayout2.setVisibility(View.GONE);
                break;
            case 1:
                binding.framelayout.setVisibility(View.GONE);
                binding.framelayout2.setVisibility(View.VISIBLE);
                break;
        }
    }


}