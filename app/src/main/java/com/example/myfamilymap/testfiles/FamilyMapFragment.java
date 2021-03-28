package com.example.myfamilymap.testfiles;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myfamilymap.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FamilyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FamilyMapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String LOG_TAG = "MapFragment";

    //public static final String ARG_TITLE = "title";

    // TODO: Rename and change types of parameters
    //private String title;

    public FamilyMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment FamilyMap.
     */
    // TODO: Rename and change types and number of parameters
    public static FamilyMapFragment newInstance() {
        FamilyMapFragment fragment = new FamilyMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_family_map, container, false);
    }
}