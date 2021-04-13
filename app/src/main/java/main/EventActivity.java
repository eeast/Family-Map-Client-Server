package main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myfamilymap.R;

public class EventActivity extends AppCompatActivity {
    private static final String EXTRA_EVENT_ID = "EventID";
    private static final String MAP_FRAGMENT_TAG = "mapFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra(EXTRA_EVENT_ID);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FamilyMapFragment familyMapFragment = FamilyMapFragment.newInstance();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_EVENT_ID, eventID);
        familyMapFragment.setArguments(bundle);
        fragmentManager
                .beginTransaction()
                .add(R.id.eventFrameLayout, familyMapFragment, MAP_FRAGMENT_TAG)
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }

        return true;
    }
}