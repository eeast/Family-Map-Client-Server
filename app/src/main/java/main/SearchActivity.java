package main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfamilymap.R;
import com.joanzapata.iconify.IconDrawable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dataManagement.ColorMapping;
import dataManagement.DataCache;
import model.Event;
import model.Person;

import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_female;
import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_male;
import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_map_marker;

public class SearchActivity extends AppCompatActivity {
    private static final int PERSON_TYPE = 0;
    private static final int EVENT_TYPE = 1;


    private static final String EXTRA_EVENT_ID = "EventID";
    private static final String EXTRA_PERSON_ID = "PersonID";

    private DataAdapter adapter;
    private Set<Person> people;
    private Set<Event> events;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterResults(query);
                adapter.notifyDataSetChanged();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterResults(newText);
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

        DataCache dataCache = DataCache.getInstance();
        people = new HashSet<>();
        people.addAll(dataCache.getAllPersons());
        events = new HashSet<>();
        events.addAll(dataCache.getFilteredEvents());

        adapter = new DataAdapter(people, events);
        recyclerView.setAdapter(adapter);
    }

    private void filterResults(String query) {
        people.clear();
        events.clear();
        
        DataCache dataCache = DataCache.getInstance();
        people = dataCache.searchPeople(query);
        events = dataCache.searchEvents(query);

        adapter = new DataAdapter(people, events);
        recyclerView.setAdapter(adapter);
    }

    private class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {
        private final List<Person> people;
        private final List<Event> events;

        public DataAdapter(Set<Person> people, Set<Event> events) {
            this.people = new ArrayList<>(people);
            this.events = new ArrayList<>(events);
        }

        @Override
        public int getItemViewType(int position) {
            return position < people.size() ? PERSON_TYPE : EVENT_TYPE;
        }

        @NonNull
        @Override
        public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;

            if(viewType == PERSON_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }

            return new DataViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
            if(position < people.size()) {
                holder.bind(people.get(position));
            } else {
                holder.bind(events.get(position - people.size()));
            }
        }

        @Override
        public int getItemCount() {
            return people.size() + events.size();
        }
    }

    private class DataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView eventDetails;
        private final TextView personName;
        private final ImageView imageView;

        private final int viewType;
        private Event event;
        private Person person;

        DataViewHolder(View view, int viewType) {
            super(view);
            this.viewType = viewType;

            itemView.setOnClickListener(this);

            if(viewType == EVENT_TYPE) {
                eventDetails = itemView.findViewById(R.id.dataDetails);
            } else {
                eventDetails = null;
            }
            personName = itemView.findViewById(R.id.personName);
            imageView = itemView.findViewById(R.id.imageView);
        }

        private void bind(Person person) {
            this.person = person;
            String name = person.getFirst_name() + " " + person.getLast_name();
            personName.setText(name);

            ImageView imageView = itemView.findViewById(R.id.imageView);
            if (person.getGender().equals("m")) {
                imageView.setImageDrawable(new IconDrawable(itemView.getContext(), fa_male)
                        .colorRes(R.color.light_blue)
                        .sizeDp(50));
            } else {
                imageView.setImageDrawable(new IconDrawable(itemView.getContext(), fa_female)
                        .colorRes(R.color.light_pink)
                        .sizeDp(50));
            }
        }

        private void bind(Event event) {
            this.event = event;
            String details =
                    event.getEvent_type().toUpperCase() + ": " +
                    event.getCity() + ", " +
                    event.getCountry() + " (" +
                    event.getYear() + ")";
            eventDetails.setText(details);
            DataCache dataCache = DataCache.getInstance();
            this.person = dataCache.findPerson(event.getPerson_id());
            String name = person.getFirst_name() + " " + person.getLast_name();
            personName.setText(name);

            ColorMapping colorMapping = ColorMapping.getInstance();
            imageView.setImageDrawable(new IconDrawable(itemView.getContext(), fa_map_marker)
                    .colorRes(colorMapping.getHex(event.getEvent_type()))
                    .sizeDp(50));
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            if(viewType == EVENT_TYPE) {
                intent = new Intent(SearchActivity.this, EventActivity.class);
                intent.putExtra(EXTRA_EVENT_ID, event.getEvent_id());
            } else {
                intent = new Intent(SearchActivity.this, PersonActivity.class);
                intent.putExtra(EXTRA_PERSON_ID, person.getPerson_id());
            }
            startActivity(intent);
        }
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