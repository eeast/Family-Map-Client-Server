package com.example.myfamilymap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myfamilymap.HTTPConnection.EventsHandler;
import com.example.myfamilymap.HTTPConnection.LoginHandler;
import com.example.myfamilymap.HTTPConnection.PersonHandler;
import com.example.myfamilymap.HTTPConnection.PersonsHandler;
import com.example.myfamilymap.HTTPConnection.RegisterHandler;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import model.AuthToken;
import model.Event;
import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventsResult;
import result.LoginResult;
import result.PersonResult;
import result.PersonsResult;
import result.RegisterResult;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private static final String ARG_SUCCESS = "Success";
    private static final String ARG_MESSAGE = "Message";
    private static final String ARG_AUTH_TOKEN = "AuthToken";
    private static final String ARG_USERNAME = "Username";
    private static final String ARG_PERSON_ID = "PersonID";
    private static final String ARG_FIRST_NAME = "FirstName";
    private static final String ARG_LAST_NAME = "LastName";
    private static final String ARG_GENDER = "Gender";
    private static final String ARG_FATHER_ID = "FatherID";
    private static final String ARG_MOTHER_ID = "MotherID";
    private static final String ARG_SPOUSE_ID = "SpouseID";
    private static final String ARG_DATA = "JsonData";

    private View view;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * default onCreate function
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates inflated view and adds two button listeners:
     * loginButton - initiates login()
     * registerButton - initiates register()
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        //login button
        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }
        });

        //register button
        Button registerButton = (Button) view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                register();
            }
        });
        return view;
    }


    /**
     * login function creates handler and executor for LoginTask. Displays Toast, loads
     * AuthToken, initiates loadData, and switches view upon successful login.
     */
    private void login() {
        String urlString = getURL();
        System.out.println("Connecting to " + urlString);

        String username = getValue(view.findViewById(R.id.username));
        String password = getValue(view.findViewById(R.id.password));
        LoginRequest loginRequest = new LoginRequest(username, password);

        if (loginRequest.isValid()){
            try {
                Handler loginThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        if (bundle.getBoolean(ARG_SUCCESS)) {
                            Toast.makeText(view.getContext(), "Login Successful!", Toast.LENGTH_LONG).show();
                            loadAuthToken(bundle);
                            loadData(bundle.getString(ARG_PERSON_ID));
                            ((MainActivity)getActivity()).switchMapView();
                        } else {
                            Toast.makeText(view.getContext(), "Unable to authenticate. Please check your username and/or password and try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                LoginTask task = new LoginTask(loginThreadHandler, urlString, loginRequest);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(task);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error encountered during login");
            }
        } else {
            Toast.makeText(view.getContext(), "Missing required field. Please ensure that all fields are properly completed.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * register function creates handler and executor for RegisterTask. Displays toast,
     * loads AuthToken, initiates loadData, and switches view upon successful registration.
     */
    private void register() {
        String urlString = getURL();
        System.out.println("Connecting to " + urlString);

        String username = getValue(view.findViewById(R.id.username));
        String password = getValue(view.findViewById(R.id.password));
        String firstName = getValue(view.findViewById(R.id.firstNameField));
        String lastName = getValue(view.findViewById(R.id.lastNameField));
        String email = getValue(view.findViewById(R.id.email));
        RadioGroup genderGroup = view.findViewById(R.id.gender);
        String gender = null;
        if (genderGroup.getCheckedRadioButtonId() == 1) {
            gender = "m";
        } else if (genderGroup.getCheckedRadioButtonId() == 2) {
            gender ="f";
        }
        RegisterRequest registerRequest = new RegisterRequest(username, password, email, firstName,
                lastName, gender);

        if (registerRequest.isValid()) {
            try {
                Handler registerThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        if (bundle.getBoolean(ARG_SUCCESS)) {
                            Toast.makeText(view.getContext(), "Registration Successful!", Toast.LENGTH_LONG).show();
                            loadAuthToken(bundle);
                            loadData(bundle.getString(ARG_PERSON_ID));
                            ((MainActivity)getActivity()).switchMapView();
                        } else {
                            Toast.makeText(view.getContext(), "Username already exists!", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                RegisterTask task = new RegisterTask(registerThreadHandler, urlString, registerRequest);
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(task);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error creating Login Handler");
            }
        } else {
            Toast.makeText(view.getContext(), "Missing required field. Please ensure that all fields are properly completed.", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * loadData function controls loading the Person data for the user, then all family Person
     * objects and corresponding Event objects.
     * @param personID String personID received upon login/registration
     */
    private void loadData(String personID) {
        loadUser(personID);
        loadFamily();
        loadFamilyEvents();
    }

    /**
     * loadUser function creates handler and executor for LoadUserTask
     * @param personID String personID received upon login/registration
     */
    private void loadUser(String personID) {
        String urlString = getURL();
        System.out.println("Connecting to " + urlString);

        try {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    super.handleMessage(msg);
                    DataCache dataCache = DataCache.getInstance();
                    Toast.makeText(view.getContext(), String.format("Welcome, %s %s!",
                            dataCache.getUser().getFirst_name(), dataCache.getUser().getLast_name()),
                            Toast.LENGTH_LONG).show();
                }
            };

            DataCache dataCache = DataCache.getInstance();
            String authToken = dataCache.getAuthToken().getToken();
            LoadUserTask task = new LoadUserTask(handler, urlString, personID, authToken);
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving Person Data for User");
        }
    }


    /**
     * loadFamily function uses the current user's Person data to populate the Person
     * data for extended family members
     */
    private void loadFamily() {
        DataCache dataCache = DataCache.getInstance();
        AuthToken authToken = dataCache.getAuthToken();
        ArrayList<Person> people = new ArrayList<Person>();
        String urlString = getURL();
        System.out.println("Connecting to " + urlString);

        try {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message message) {
                    Bundle bundle = message.getData();
                    if (bundle.getBoolean(ARG_SUCCESS)) {
                        PersonsResult result = new Gson().fromJson(bundle.getString(ARG_DATA), PersonsResult.class);
                        DataCache dataCache = DataCache.getInstance();
                        for (Person person : result.getData()) {
                            dataCache.getAllPersons().add(person);
                        }
                    }
                }
            };

            LoadPersonsTask task = new LoadPersonsTask(handler, urlString, authToken.getToken());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving Person data");
        }
    }

    /**
     * loadFamily function uses the current user's Person data to populate the Person
     * data for extended family members
     */
    private void loadFamilyEvents() {
        DataCache dataCache = DataCache.getInstance();
        AuthToken authToken = dataCache.getAuthToken();
        ArrayList<Event> events = new ArrayList<Event>();
        String urlString = getURL();
        System.out.println("Connecting to " + urlString);

        try {
            Handler handler = new Handler() {
                @Override
                public void handleMessage(@NonNull Message message) {
                    Bundle bundle = message.getData();
                    if (bundle.getBoolean(ARG_SUCCESS)) {
                        EventsResult result = new Gson().fromJson(bundle.getString(ARG_DATA), EventsResult.class);
                        DataCache dataCache = DataCache.getInstance();
                        for (Event event : result.getData()) {
                            dataCache.getAllEvents().add(event);
                        }
                    }
                }
            };

            LoadEventsTask task = new LoadEventsTask(handler, urlString, authToken.getToken());
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.submit(task);

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error retrieving Person data");
        }
    }

    /**
     * method for adding AuthToken object to the DataCache
     * @param bundle results from login or register function
     */
    private void loadAuthToken(Bundle bundle) {
        String authTokenString = bundle.getString(ARG_AUTH_TOKEN);
        String username = bundle.getString(ARG_USERNAME);
        AuthToken authToken = new AuthToken(authTokenString, username);
        DataCache dataCache = DataCache.getInstance();
        dataCache.setAuthToken(authToken);
    }

    /**
     * private Runnable class LoginTask uses LoginHandler class to connect to the server and perform
     * the "login" task.
     */
    private static class LoginTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final LoginRequest loginRequest;

        /**
         * Constructor for LoginTask object
         * @param handler handler created during login method should be passed in
         * @param urlString String url for connection
         * @param loginRequest LoginRequest object containing login details
         */
        public LoginTask(Handler handler, String urlString, LoginRequest loginRequest) {
            this.handler = handler;
            this.urlString = urlString;
            this.loginRequest = loginRequest;
        }

        /**
         * Override of run method: creates LoginHandler object and sends the information from
         * the LoginResult back to the handler in the form of a bundle.
         */
        @Override
        public void run() {
            LoginHandler loginHandler = new LoginHandler();
            LoginResult loginResult = null;
            if (loginRequest != null){
                try {
                    loginResult = loginHandler.login(urlString, loginRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error communicating with server");
                }
            }
            sendMessage(loginResult);
        }

        /**
         * sendMessage method for conversion of LoginResult to bundle to be sent to the handler
         * @param loginResult LoginResult object received from the server
         */
        private void sendMessage(LoginResult loginResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, loginResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, loginResult.getMessage());
            messageBundle.putString(ARG_AUTH_TOKEN, loginResult.getAuthtoken());
            messageBundle.putString(ARG_USERNAME, loginResult.getUsername());
            messageBundle.putString(ARG_PERSON_ID, loginResult.getPerson_id());
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    /**
     * private Runnable class RegisterTask uses registerHandler class to connect to the server and perform
     * the "register" task.
     */
    private static class RegisterTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final RegisterRequest registerRequest;

        /**
         * Constructor for RegisterTask object
         * @param handler handler created during register method should be passed in
         * @param urlString String url for connection
         * @param registerRequest RegisterRequest object containing registration details
         */
        public RegisterTask(Handler handler, String urlString, RegisterRequest registerRequest) {
            this.handler = handler;
            this.urlString = urlString;
            this.registerRequest = registerRequest;
        }

        /**
         * Override of run method: creates RegisterHandler object and sends the information from
         * the RegisterResult back to the handler in the form of a bundle.
         */
        @Override
        public void run() {
            RegisterHandler registerHandler = new RegisterHandler();
            RegisterResult registerResult = null;
            if (registerRequest != null){
                try {
                    registerResult = registerHandler.register(urlString, registerRequest);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error communicating with server");
                }
            }
            sendMessage(registerResult);
        }

        /**
         * sendMessage method for conversion of registerResult to bundle to be sent to the handler
         * @param registerResult RegisterResult object received from the server
         */
        private void sendMessage(RegisterResult registerResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, registerResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, registerResult.getMessage());
            messageBundle.putString(ARG_AUTH_TOKEN, registerResult.getAuth_token());
            messageBundle.putString(ARG_USERNAME, registerResult.getUsername());
            messageBundle.putString(ARG_PERSON_ID, registerResult.getPerson_id());
            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    /**
     * private Runnable class LoadUserTask uses PersonHandler class to connect to the server and
     * retrieve Person data for the current user.
     */
    private static class LoadUserTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final String personID;
        private final String authToken;

        /**
         * Constructor for LoadUserTask object
         * @param handler handler created during loadUser method should be passed in
         * @param urlString String url for connection
         * @param personID String personID received during login/registration
         * @param authToken String authToken received during login/registration
         */
        public LoadUserTask(Handler handler, String urlString, String personID, String authToken) {
            this.handler = handler;
            this.urlString = urlString;
            this.personID = personID;
            this.authToken = authToken;
        }

        /**
         * Override of run method: creates PersonHandler object, receives the PersonResult object
         * from the server, converts the result into a Person object, and adds the Person object
         * data to the DataCache.
         */
        @Override
        public void run() {
            PersonHandler personHandler = new PersonHandler();
            PersonResult personResult = null;
            try {
                personResult = personHandler.getUser(urlString, personID, authToken);
                if (personResult != null) {
                    Person person = new Person(
                            personResult.getPerson_id(),
                            personResult.getAssoc_username(),
                            personResult.getFirst_name(),
                            personResult.getLast_name(),
                            personResult.getGender(),
                            personResult.getFather_id(),
                            personResult.getMother_id(),
                            personResult.getSpouse_id()
                    );
                    DataCache dataCache = DataCache.getInstance();
                    dataCache.setUser(person);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
            }

            sendMessage(personResult);
        }

        /**
         * sendMessage method returns the success boolean and message from the PersonResult object
         * back to the handler in the form of a bundle.
         * @param personResult
         */
        private void sendMessage(PersonResult personResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, personResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, personResult.getMessage());

            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    /**
     * private Runnable class LoadPersonsTask uses PersonHandler class to connect to the server and
     * retrieve Person data for the current user's family.
     */
    private static class LoadPersonsTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final String authToken;

        /**
         * Constructor for LoadUserTask object
         * @param handler handler created during loadPerson method should be passed in
         * @param urlString String url for connection
         * @param authToken String authToken received during login/registration
         */
        public LoadPersonsTask(Handler handler, String urlString, String authToken) {
            this.handler = handler;
            this.urlString = urlString;
            this.authToken = authToken;
        }

        /**
         * Override of run method: creates PersonHandler object, receives the PersonResult object
         * from the server, converts the result into a Person object, and adds the Person object
         * data to the DataCache.
         */
        @Override
        public void run() {
            PersonsHandler personsHandler = new PersonsHandler();
            PersonsResult personsResult = null;
            try {
                personsResult = personsHandler.getPersons(urlString, authToken);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
            }

            sendMessage(personsResult);
        }

        /**
         * sendMessage method returns the success boolean, message, and Person data from the
         * PersonResult object back to the handler in the form of a bundle.
         * @param personsResult PersonsResult object received from the server
         */
        private void sendMessage(PersonsResult personsResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, personsResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, personsResult.getMessage());

            Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gsonSerializer.toJson(personsResult);
            messageBundle.putString(ARG_DATA, jsonString);

            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }

    /**
     * private Runnable class LoadEventsTask uses EventsHandler class to connect to the server and
     * retrieve Event data for the current user's family.
     */
    private static class LoadEventsTask implements Runnable {
        private final Handler handler;
        private final String urlString;
        private final String authToken;

        /**
         * Constructor for LoadUserTask object
         * @param handler handler created during loadPerson method should be passed in
         * @param urlString String url for connection
         * @param authToken String authToken received during login/registration
         */
        public LoadEventsTask(Handler handler, String urlString, String authToken) {
            this.handler = handler;
            this.urlString = urlString;
            this.authToken = authToken;
        }

        /**
         * Override of run method: creates PersonHandler object, receives the PersonResult object
         * from the server, converts the result into a Person object, and adds the Person object
         * data to the DataCache.
         */
        @Override
        public void run() {
            EventsHandler eventsHandler = new EventsHandler();
            EventsResult eventsResult = null;
            try {
                eventsResult = eventsHandler.getEvents(urlString, authToken);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error communicating with server");
            }

            sendMessage(eventsResult);
        }

        /**
         * sendMessage method returns the success boolean, message, and Person data from the
         * PersonResult object back to the handler in the form of a bundle.
         * @param eventsResult PersonsResult object received from the server
         */
        private void sendMessage(EventsResult eventsResult) {
            Message message = Message.obtain();

            Bundle messageBundle = new Bundle();
            messageBundle.putBoolean(ARG_SUCCESS, eventsResult.isSuccess());
            messageBundle.putString(ARG_MESSAGE, eventsResult.getMessage());

            Gson gsonSerializer = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gsonSerializer.toJson(eventsResult);
            messageBundle.putString(ARG_DATA, jsonString);

            message.setData(messageBundle);

            handler.sendMessage(message);
        }
    }


    /**
     * helper function for converting EditText objects from view to String values
     * @param editText EditText object retrieved from LoginFragment view
     * @return String value of EditText object
     */
    private String getValue(EditText editText) {
        String result = editText.getText().toString().trim();
        if (result.length() > 0) {
            return result;
        } else {
            return null;
        }
    }

    /**
     * helper function for retrieving the URL using the hostIP and port fields
     * @return String URL for connecting with the server
     */
    private String getURL() {
        String hostIP = getValue(view.findViewById(R.id.hostIP));
        String port = getValue(view.findViewById(R.id.port));
        String urlString = "";
        if (hostIP != null && port != null) {
            urlString += "http://" + hostIP + ":" + port;
        }
        return urlString;
    }
}