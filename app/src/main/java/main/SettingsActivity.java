package main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfamilymap.R;

public class SettingsActivity extends AppCompatActivity {
    private DataCache dataCache;

    private static final int LIFE_LINES_TOGGLE = 0;
    private static final int FAMILY_LINES_TOGGLE = 1;
    private static final int SPOUSE_LINES_TOGGLE = 2;
    private static final int FATHER_SIDE_TOGGLE = 3;
    private static final int MOTHER_SIDE_TOGGLE = 4;
    private static final int MALE_EVENTS_TOGGLE = 5;
    private static final int FEMALE_EVENTS_TOGGLE = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        dataCache = DataCache.getInstance();

        Switch switchLifeStoryLines = findViewById(R.id.switchLifeStoryLines);
        switchLifeStoryLines.setChecked(dataCache.getOptionToggle(LIFE_LINES_TOGGLE));
        switchLifeStoryLines.setOnCheckedChangeListener((buttonView, isChecked) ->
                dataCache.setOptionToggle(LIFE_LINES_TOGGLE, isChecked));

        Switch switchFamilyLines = findViewById(R.id.switchFamilyTreeLines);
        switchFamilyLines.setChecked(dataCache.getOptionToggle(FAMILY_LINES_TOGGLE));
        switchFamilyLines.setOnCheckedChangeListener((buttonView, isChecked) ->
                dataCache.setOptionToggle(FAMILY_LINES_TOGGLE, isChecked));

        Switch switchSpouseLines = findViewById(R.id.switchSpouseLines);
        switchSpouseLines.setChecked(dataCache.getOptionToggle(SPOUSE_LINES_TOGGLE));
        switchSpouseLines.setOnCheckedChangeListener((buttonView, isChecked) ->
                dataCache.setOptionToggle(SPOUSE_LINES_TOGGLE, isChecked));

        Switch switchFatherSide = findViewById(R.id.switchFatherSide);
        switchFatherSide.setChecked(dataCache.getOptionToggle(FATHER_SIDE_TOGGLE));
        switchFatherSide.setOnCheckedChangeListener((buttonView, isChecked) ->
                dataCache.setOptionToggle(FATHER_SIDE_TOGGLE, isChecked));

        Switch switchMotherSide = findViewById(R.id.switchMotherSide);
        switchMotherSide.setChecked(dataCache.getOptionToggle(MOTHER_SIDE_TOGGLE));
        switchMotherSide.setOnCheckedChangeListener((buttonView, isChecked) ->
                dataCache.setOptionToggle(MOTHER_SIDE_TOGGLE, isChecked));

        Switch switchMaleEvents = findViewById(R.id.switchMaleEvents);
        switchMaleEvents.setChecked(dataCache.getOptionToggle(MALE_EVENTS_TOGGLE));
        switchMaleEvents.setOnCheckedChangeListener((buttonView, isChecked) ->
                dataCache.setOptionToggle(MALE_EVENTS_TOGGLE, isChecked));

        Switch switchFemaleEvents = findViewById(R.id.switchFemaleEvents);
        switchFemaleEvents.setChecked(dataCache.getOptionToggle(FEMALE_EVENTS_TOGGLE));
        switchFemaleEvents.setOnCheckedChangeListener((buttonView, isChecked) ->
                dataCache.setOptionToggle(FEMALE_EVENTS_TOGGLE, isChecked));

        RelativeLayout logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logout());
    }

    private void logout() {
        Toast.makeText(this, "logout initiated", Toast.LENGTH_SHORT).show();
        DataCache dataCache = DataCache.getInstance();
        dataCache.clear();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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