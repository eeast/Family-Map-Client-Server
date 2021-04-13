package main;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.myfamilymap.R;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

public class MainActivity extends AppCompatActivity {

    private final String LOGIN_FRAGMENT_TAG = "loginFragment";
    private final String MAP_FRAGMENT_TAG = "mapFragment";

    private DataCache dataCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Iconify.with(new FontAwesomeModule());

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.mainFrameLayout);

        dataCache = DataCache.getInstance();

        if (fragment == null) {
            if (dataCache.getAuthToken() != null) {
                FamilyMapFragment familyMapFragment = FamilyMapFragment.newInstance();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.mainFrameLayout, familyMapFragment, MAP_FRAGMENT_TAG)
                        .commit();
            } else {
                LoginFragment loginFragment = LoginFragment.newInstance();
                fragmentManager
                        .beginTransaction()
                        .add(R.id.mainFrameLayout, loginFragment, LOGIN_FRAGMENT_TAG)
                        .commit();
            }
        }
    }

    public void switchMapView() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment loginFragment = fragmentManager.findFragmentByTag(LOGIN_FRAGMENT_TAG);
        Fragment familyMapFragment = fragmentManager.findFragmentByTag(MAP_FRAGMENT_TAG);
        dataCache = DataCache.getInstance();
        if (loginFragment != null && dataCache.getAuthToken() != null) {
            familyMapFragment = FamilyMapFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.mainFrameLayout, familyMapFragment, MAP_FRAGMENT_TAG)
                    .commit();
        } else if (familyMapFragment != null && dataCache.getAuthToken() == null) {
            loginFragment = LoginFragment.newInstance();
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.mainFrameLayout, loginFragment, LOGIN_FRAGMENT_TAG)
                    .commit();
        }
    }

}