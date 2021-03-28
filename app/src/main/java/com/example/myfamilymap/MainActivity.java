package com.example.myfamilymap;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myfamilymap.testfiles.FamilyMapFragment;

public class MainActivity extends AppCompatActivity {

    private FamilyMapFragment familyMapFragment;
    private LoginFragment loginFragment;

    private DataCache dataCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataCache = DataCache.getInstance();

        FragmentManager fm = getSupportFragmentManager();
        loginFragment = (LoginFragment) fm.findFragmentById(R.id.mainFrameLayout);
        if (loginFragment == null) {
            loginFragment = LoginFragment.newInstance();
            fm.beginTransaction()
                    .add(R.id.mainFrameLayout, loginFragment)
                    .commit();
        }
        if (dataCache.getAuthToken() != null) {
            familyMapFragment = familyMapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.mainFrameLayout, familyMapFragment)
                    .commit();
        }
    }

    public void switchMapView() {
        dataCache = DataCache.getInstance();
        System.out.println(dataCache.getAuthToken().getToken());
        if (dataCache.getAuthToken() != null) {
            FragmentManager fm = getSupportFragmentManager();
            familyMapFragment = familyMapFragment.newInstance();
            fm.beginTransaction()
                    .replace(R.id.mainFrameLayout, familyMapFragment)
                    .commit();
        }
    }








}