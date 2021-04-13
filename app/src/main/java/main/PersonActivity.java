package main;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myfamilymap.R;
import com.joanzapata.iconify.IconDrawable;

import java.util.List;

import model.Event;
import model.Person;

import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_female;
import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_male;
import static com.joanzapata.iconify.fonts.FontAwesomeIcons.fa_map_marker;

public class PersonActivity extends AppCompatActivity {
    private static final String EXTRA_PERSON_ID = "PersonID";
    private static final String EXTRA_EVENT_ID = "EventID";

    private Person currentPerson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        Intent intent = getIntent();
        String personID = intent.getStringExtra(EXTRA_PERSON_ID);
        DataCache dataCache = DataCache.getInstance();
        currentPerson = dataCache.findPerson(personID);

        TextView personFirstName = findViewById(R.id.personFirstName);
        TextView personLastName = findViewById(R.id.personLastName);
        TextView personGender = findViewById(R.id.personGender);

        personFirstName.setText(currentPerson.getFirst_name());
        personLastName.setText(currentPerson.getLast_name());
        personGender.setText(currentPerson.getGender().equals("m") ? "Male" : "Female");

        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        List<Person> people = dataCache.getRelatives(currentPerson);
        List<Event> events = dataCache.getPersonalEvents(personID);
        expandableListView.setAdapter(new PersonAdapter(people, events));
        expandableListView.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            setListViewHeight(parent, groupPosition);
            return false;
        });
        setListViewHeight(expandableListView, 2);
    }

    private void setListViewHeight(ExpandableListView listView,
                                   int group) {
        ExpandableListAdapter listAdapter = listView.getExpandableListAdapter();
        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        for (int i = 0; i < listAdapter.getGroupCount(); i++) {
            View groupItem = listAdapter.getGroupView(i, false, null, listView);
            groupItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

            totalHeight += groupItem.getMeasuredHeight();

            if (((listView.isGroupExpanded(i)) && (i != group))
                    || ((!listView.isGroupExpanded(i)) && (i == group))) {
                for (int j = 0; j < listAdapter.getChildrenCount(i); j++) {
                    View listItem = listAdapter.getChildView(i, j, false, null,
                            listView);
                    listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);

                    totalHeight += listItem.getMeasuredHeight();

                }
            }
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        int height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getGroupCount() - 1));
        if (height < 10)
            height = 200;
        params.height = height;
        listView.setLayoutParams(params);
        listView.requestLayout();

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

    private class PersonAdapter extends BaseExpandableListAdapter {

        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PERSON_GROUP_POSITION = 1;

        private final List<Person> people;
        private final List<Event> events;

        public PersonAdapter(List<Person> people, List<Event> events) {
            this.people = people;
            this.events = events;
        }

        @Override
        public int getGroupCount() {
            return 2;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    return people.size();
                case EVENT_GROUP_POSITION:
                    return events.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    return getString(R.string.peopleTitle);
                case EVENT_GROUP_POSITION:
                    return getString(R.string.eventsTitle);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    return people.get(childPosition);
                case EVENT_GROUP_POSITION:
                    return events.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent, false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case PERSON_GROUP_POSITION:
                    titleView.setText(R.string.peopleTitle);
                    break;
                case EVENT_GROUP_POSITION:
                    titleView.setText(R.string.eventsTitle);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            View itemView;

            switch(groupPosition) {
                case PERSON_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePersonView(itemView, childPosition);
                    break;
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return itemView;
        }

        private void initializePersonView(View personView, final int childPosition) {
            TextView personNameView = personView.findViewById(R.id.personName);
            String name = people.get(childPosition).getFirst_name() + " " + people.get(childPosition).getLast_name();
            personNameView.setText(name);

            TextView personRelation = personView.findViewById(R.id.personRelation);
            String relation = getRelation(currentPerson, people.get(childPosition));
            personRelation.setText(relation);

            ImageView imageView = personView.findViewById(R.id.imageView);
            if (people.get(childPosition).getGender().equals("m")) {
                imageView.setImageDrawable(new IconDrawable(personView.getContext(), fa_male)
                        .colorRes(R.color.light_blue)
                        .sizeDp(50));
            } else {
                imageView.setImageDrawable(new IconDrawable(personView.getContext(), fa_female)
                        .colorRes(R.color.light_pink)
                        .sizeDp(50));
            }

            personView.setOnClickListener(v -> {
                Toast.makeText(PersonActivity.this,
                        getString(R.string.personSelected) + people.get(childPosition).getFirst_name() + " " + people.get(childPosition).getLast_name(),
                        Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PersonActivity.this, PersonActivity.class);
                intent.putExtra(EXTRA_PERSON_ID, people.get(childPosition).getPerson_id());
                startActivity(intent);
            });
        }

        public String getRelation(Person currentPerson, Person person) {
            if(currentPerson.getFather_id() != null && currentPerson.getFather_id().equals(person.getPerson_id())) {
                return "Father";
            } else if(currentPerson.getMother_id() != null &&currentPerson.getMother_id().equals(person.getPerson_id())) {
                return "Mother";
            } else if(currentPerson.getSpouse_id() != null && currentPerson.getSpouse_id().equals(person.getPerson_id())) {
                return "Spouse";
            } else {
                if(person.getFather_id() != null && person.getFather_id().equals(currentPerson.getPerson_id())){
                    return "Child";
                } else if (person.getMother_id() != null && person.getMother_id().equals(currentPerson.getPerson_id())) {
                    return "Child";
                } else {
                    return "Unknown";
                }
            }
        }

        private void initializeEventView(View eventView, final int childPosition) {
            TextView dataDetails = eventView.findViewById(R.id.dataDetails);
            String details =
                    events.get(childPosition).getEvent_type().toUpperCase() + ": " +
                    events.get(childPosition).getCity() + ", " +
                    events.get(childPosition).getCountry() + " (" +
                    events.get(childPosition).getYear() + ")";
            dataDetails.setText(details);

            TextView personNameView = eventView.findViewById(R.id.personName);
            DataCache dataCache = DataCache.getInstance();
            Person person = dataCache.findPerson(events.get(childPosition).getPerson_id());
            String name = person.getFirst_name() + " " + person.getLast_name();
            personNameView.setText(name);

            ImageView imageView = eventView.findViewById(R.id.imageView);
            ColorMapping colorMapping = ColorMapping.getInstance();
            imageView.setImageDrawable(new IconDrawable(eventView.getContext(), fa_map_marker)
                    .colorRes(colorMapping.getHex(events.get(childPosition).getEvent_type()))
                    .sizeDp(50));

            eventView.setOnClickListener(v -> {
                Intent intent = new Intent(PersonActivity.this, EventActivity.class);
                intent.putExtra(EXTRA_EVENT_ID, events.get(childPosition).getEvent_id());
                startActivity(intent);
            });
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }
}