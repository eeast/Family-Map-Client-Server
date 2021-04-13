package main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.myfamilymap.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.Iconify;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;
import com.joanzapata.iconify.fonts.FontAwesomeModule;

import java.util.ArrayList;
import java.util.List;

import model.Event;
import model.Person;

import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_android;
import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_female;
import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_male;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FamilyMapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FamilyMapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static final String EXTRA_PERSON_ID = "PersonID";
    private static final String EXTRA_EVENT_ID = "EventID";

    private static final int RESULT_CODE_SETTINGS = 0;

    private static final int LIFE_LINES_TOGGLE = 0;
    private static final int FAMILY_LINES_TOGGLE = 1;
    private static final int SPOUSE_LINES_TOGGLE = 2;

    private List<Polyline> lines;
    private List<Marker> markers;

    private GoogleMap map;
    private Marker currentMarker;

    private Event currentEvent;

    LinearLayout eventDataBox;
    TextView dataDetails;
    TextView personName;
    ImageView imageView;

    public FamilyMapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BlankFragment.
     */
    public static FamilyMapFragment newInstance() {
        FamilyMapFragment fragment = new FamilyMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getActivity().getClass() == MainActivity.class) {
            setHasOptionsMenu(true);
        }
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            String eventID = bundle.getString(EXTRA_EVENT_ID, null);
            if (eventID != null) {
                DataCache dataCache = DataCache.getInstance();
                currentEvent = dataCache.findEvent(eventID);
            }
        }
        Iconify.with(new FontAwesomeModule());
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.menu_main, menu);

        MenuItem search = menu.findItem(R.id.searchMenuItem);
        search.setIcon(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_search)
                .colorRes(R.color.white)
                .actionBarSize());

        MenuItem options = menu.findItem(R.id.optionsMenuItem);
        options.setIcon(new IconDrawable(this.getContext(), FontAwesomeIcons.fa_gear)
                .colorRes(R.color.white)
                .actionBarSize());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.searchMenuItem:
                Toast.makeText(this.getContext(), "Search selected", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this.getActivity(), SearchActivity.class);
                startActivity(intent);
                return super.onOptionsItemSelected(item);
            case R.id.optionsMenuItem:
                Intent intent1 = new Intent(this.getActivity(), SettingsActivity.class);
                startActivityForResult(intent1, RESULT_CODE_SETTINGS);
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_CODE_SETTINGS) {
            DataCache dataCache = DataCache.getInstance();
            if(dataCache.getAuthToken() == null && this.getActivity().getClass() == MainActivity.class) {
                ((MainActivity)this.getActivity()).switchMapView();
            } else {
                loadMarkers(map);
                if (currentMarker != null) {
                    for (Marker m : markers) {
                        if (m.getTitle().equals(currentMarker.getTitle())) {
                            onMarkerClick(m);
                            break;
                        }
                    }
                    if (!markers.contains(currentMarker)) {
                        personName.setText(R.string.markerHidden1);
                        dataDetails.setText(R.string.markerHidden2);
                        imageView.setImageDrawable(new IconDrawable(this.getContext(), fa_android)
                                .colorRes(R.color.grey)
                                .sizeDp(50));
                        for (Polyline polyline : lines) {
                            polyline.remove();
                        }
                        lines.clear();
                    }
                }
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        View view = layoutInflater.inflate(R.layout.fragment_family_map, container, false);

        lines = new ArrayList<>();
        markers = new ArrayList<>();

        dataDetails = view.findViewById(R.id.dataDetails);
        personName = view.findViewById(R.id.personName);
        imageView = view.findViewById(R.id.imageView);

        personName.setText(R.string.defaultMapWelcome);
        dataDetails.setText(R.string.defaultMapSubtext);
        imageView.setImageDrawable(new IconDrawable(this.getContext(), fa_android)
                .colorRes(R.color.grey)
                .sizeDp(50));

        eventDataBox = view.findViewById(R.id.eventDataBox);
        eventDataBox.setClickable(false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.familyMap);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        return view;
    }

    private void navigateToActivity() {
        String personID = currentEvent.getPerson_id();
        Intent intent = new Intent(this.getActivity(), PersonActivity.class);
        intent.putExtra(EXTRA_PERSON_ID, personID);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(this);
        loadMarkers(map);
        if (currentEvent != null) {
            for (Marker m : markers) {
                if(currentEvent.getEvent_id().equals(m.getTitle())) {
                    onMarkerClick(m);
                    break;
                }
            }
        }
    }

    private void loadMarkers(GoogleMap map) {
        DataCache dataCache = DataCache.getInstance();
        List<Event> events = new ArrayList<>(dataCache.getFilteredEvents());
        clearMarkers();
        for (Event event : events) {
            LatLng location = new LatLng(event.getLatitude(), event.getLongitude());

            Marker marker = map.addMarker(new MarkerOptions()
                    .position(location)
                    .title(event.getEvent_id()).icon(getColoredMarker(event.getEvent_type())));
            markers.add(marker);
        }
    }

    private void clearMarkers() {
        int size = markers.size();
        for(int i = 0; i < size; i++) {
            markers.get(i).remove();
        }
        markers.clear();
    }

    private BitmapDescriptor getColoredMarker(String type) {
        String key = type.toLowerCase();
        ColorMapping colorMapping = ColorMapping.getInstance();
        return BitmapDescriptorFactory.defaultMarker(colorMapping.getHue(key));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        currentMarker = marker;
        eventDataBox.setClickable(true);
        eventDataBox.setOnClickListener(v -> navigateToActivity());
        DataCache dataCache = DataCache.getInstance();
        currentEvent = dataCache.findEvent(marker.getTitle());

        String details =
                currentEvent.getEvent_type().toUpperCase() + ": " +
                currentEvent.getCity() + ", " +
                currentEvent.getCountry() + " (" +
                currentEvent.getYear() + ")";

        dataDetails.setText(details);

        Person person = dataCache.findPerson(currentEvent.getPerson_id());
        String name = person.getFirst_name() + " " + person.getLast_name();
        personName.setText(name);

        if (person.getGender().equals("m")) {
            imageView.setImageDrawable(new IconDrawable(this.getContext(), fa_male)
                    .colorRes(R.color.light_blue)
                    .sizeDp(50));
        } else {
            imageView.setImageDrawable(new IconDrawable(this.getContext(), fa_female)
                    .colorRes(R.color.light_pink)
                    .sizeDp(50));
        }

        clearLines();
        loadMapLines(map, currentEvent, person);

        map.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        return true;
    }

    private void loadMapLines(GoogleMap map, Event currentEvent, Person person) {
        DataCache dataCache = DataCache.getInstance();
        if(dataCache.getOptionToggle(LIFE_LINES_TOGGLE)) {
            loadLifeStoryLines(map, person);
        }
        if(dataCache.getOptionToggle(FAMILY_LINES_TOGGLE)) {
            loadFamilyTreeLines(map, currentEvent, person, 0);
        }
        if(dataCache.getOptionToggle(SPOUSE_LINES_TOGGLE)) {
            loadSpouseLines(map, currentEvent, person);
        }
    }

    private void loadLifeStoryLines(GoogleMap map, Person person) {
        DataCache dataCache = DataCache.getInstance();
        List<Event> events = dataCache.getPersonalEvents(person.getPerson_id());
        for(int i = 1; i < events.size(); i++) {
            drawLine(map, events.get(i - 1), events.get(i), "life", 10);
        }
    }

    private void loadFamilyTreeLines(GoogleMap map, Event currentEvent, Person person, int generation) {
        DataCache dataCache = DataCache.getInstance();
        if(person.getFather_id() != null) {
            Person father = dataCache.findPerson(person.getFather_id());
            if(father != null) {
                List<Event> fatherEvents = dataCache.getPersonalEvents(father.getPerson_id());
                if (fatherEvents.size() > 0) {
                    drawLine(map, currentEvent, fatherEvents.get(0), "family", (10 - (generation * 2)));
                    loadFamilyTreeLines(map, fatherEvents.get(0), father, generation + 1);
                }
            }
        }
        if(person.getMother_id() != null) {
            Person mother = dataCache.findPerson(person.getMother_id());
            if (mother != null) {
                List<Event> motherEvents = dataCache.getPersonalEvents(mother.getPerson_id());
                if (motherEvents.size() > 0) {
                    drawLine(map, currentEvent, motherEvents.get(0), "family", (10 - (generation * 2)));
                    loadFamilyTreeLines(map, motherEvents.get(0), mother, generation + 1);
                }
            }
        }

    }

    private void loadSpouseLines(GoogleMap map, Event currentEvent, Person person) {
        DataCache dataCache = DataCache.getInstance();
        if(person.getSpouse_id() != null) {
            List<Event> events = dataCache.getPersonalEvents(person.getSpouse_id());
            if(events.size() > 0) {
                drawLine(map, currentEvent, events.get(0), "spouse", 10);
            }
        }

    }

    private void drawLine(GoogleMap map, Event event1, Event event2, String type, int width) {
        ColorMapping colorMapping = ColorMapping.getInstance();
        Polyline line = map.addPolyline(new PolylineOptions()
                .add(new LatLng(event1.getLatitude(), event1.getLongitude()), new LatLng(event2.getLatitude(), event2.getLongitude()))
                .width(width)
                .color(ContextCompat.getColor(this.getContext(), colorMapping.getHex(type))));
        lines.add(line);
    }

    private void clearLines() {
        int size = lines.size();
        for(int i = 0; i < size; i++) {
            lines.get(i).remove();
        }
        lines.clear();
    }
}