package main;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.myfamilymap.R;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataManagement.DataCache;
import dataManagement.ServerProxy;
import request.LoginRequest;
import request.RegisterRequest;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

import static com.example.myfamilymap.R.string.ServerErrorMsg;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    private View view;
    private EditText hostIPText;
    private EditText portText;
    private EditText usernameText;
    private EditText passwordText;
    private EditText firstNameText;
    private EditText lastNameText;
    private EditText emailText;
    private RadioGroup genderGroup;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment.
     *
     * @return A new instance of fragment main.LoginFragment.
     */
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * default onCreate function
     * @param savedInstanceState default savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Creates inflated view and adds two button listeners:
     * loginButton - initiates login()
     * registerButton - initiates register()
     * @param inflater default inflater
     * @param container default container
     * @param savedInstanceState default savedInstanceState
     * @return view created
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        //register fields
        hostIPText = view.findViewById(R.id.hostIP);
        portText = view.findViewById(R.id.port);
        usernameText = view.findViewById(R.id.username);
        passwordText = view.findViewById(R.id.password);
        firstNameText = view.findViewById(R.id.firstNameField);
        lastNameText = view.findViewById(R.id.lastNameField);
        emailText = view.findViewById(R.id.email);
        genderGroup = view.findViewById(R.id.gender);

        //set listeners
        hostIPText.addTextChangedListener(mTextWatcher);
        portText.addTextChangedListener(mTextWatcher);
        usernameText.addTextChangedListener(mTextWatcher);
        passwordText.addTextChangedListener(mTextWatcher);
        firstNameText.addTextChangedListener(mTextWatcher);
        lastNameText.addTextChangedListener(mTextWatcher);
        emailText.addTextChangedListener(mTextWatcher);
        genderGroup.setOnCheckedChangeListener(mRadioWatcher);

        //login button
        Button loginButton = view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> createLoginHandler());

        //register button
        Button registerButton = view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> createRegisterHandler());

        checkLoginFields();
        checkRegisterFields();
        return view;
    }

    private final TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            checkLoginFields();
            checkRegisterFields();
        }
    };

    private final OnCheckedChangeListener mRadioWatcher = (group, checkedId) -> {
        checkLoginFields();
        checkRegisterFields();
    };

    private void createLoginHandler() {
        String urlString = getURL();
        LoginRequest loginRequest = getLoginRequest();
        if (loginRequest.isValid()) {
            LoginHandler loginHandler = new LoginHandler();
            loginHandler.login(urlString, loginRequest);
        } else {
            Toast.makeText(view.getContext(),
                    R.string.loginErrorMsg,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void createRegisterHandler() {
        String urlString = getURL();
        RegisterRequest registerRequest = getRegisterRequest();
        if (registerRequest.isValid()) {
            RegisterHandler registerHandler = new RegisterHandler();
            registerHandler.register(urlString, registerRequest);
        } else {
            Toast.makeText(view.getContext(),
                    R.string.loginErrorMsg,
                    Toast.LENGTH_LONG).show();
        }
    }

    private void checkLoginFields() {
        Button login = view.findViewById(R.id.loginButton);

        String hostIP = hostIPText.getText().toString();
        String port = portText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        login.setEnabled(!hostIP.equals("") && !port.equals("") && !username.equals("") && !password.equals(""));
    }

    private void checkRegisterFields() {
        Button register = view.findViewById(R.id.registerButton);

        String hostIP = hostIPText.getText().toString();
        String port = portText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String gender = "";
        int selectedGender = genderGroup.getCheckedRadioButtonId();
        if (selectedGender == 1) {
            gender = "m";
        } else if (selectedGender == 2) {
            gender ="f";
        }
        register.setEnabled(!hostIP.equals("") && !port.equals("") && !username.equals("") && !password.equals("") &&
                !firstName.equals("") && !lastName.equals("") && !email.equals("") && !gender.equals(""));
    }

    private RegisterRequest getRegisterRequest() {
        String username = getValue(usernameText);
        String password = getValue(passwordText);
        String firstName = getValue(firstNameText);
        String lastName = getValue(lastNameText);
        String email = getValue(emailText);
        String gender = null;
        int selectedGender = genderGroup.getCheckedRadioButtonId();
        if (selectedGender == 1) {
            gender = "m";
        } else if (selectedGender == 2) {
            gender ="f";
        }
        return new RegisterRequest(username, password, email, firstName, lastName, gender);
    }

    private LoginRequest getLoginRequest() {
        String username = getValue(usernameText);
        String password = getValue(passwordText);
        return new LoginRequest(username, password);
    }

    /**
     * helper function for converting EditText objects from view to String values
     * @param editText EditText object retrieved from main.LoginFragment view
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
        String hostIP = getValue(hostIPText);
        String port = getValue(portText);
        String urlString = "";
        if (hostIP != null && port != null) {
            urlString += "http://" + hostIP + ":" + port;
        }
        return urlString;
    }

    private class LoginHandler {
        protected static final String ARG_SUCCESS = "Success";
        protected static final String ARG_MESSAGE = "Message";
        protected static final String ARG_AUTH_TOKEN = "AuthToken";
        protected static final String ARG_USERNAME = "Username";
        protected static final String ARG_PERSON_ID = "PersonID";

        public void login(String urlString, LoginRequest loginRequest) {
            try {
                Handler loginThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        if (bundle.getBoolean(ARG_SUCCESS)) {
                            Toast.makeText(LoginFragment.this.getContext(), "Login Successful!", Toast.LENGTH_SHORT).show();
                            DataCache dataCache = DataCache.getInstance();
                            String welcome = "Welcome, " + dataCache.getUser().getFirst_name() + " " + dataCache.getUser().getLast_name() + "!";
                            Toast.makeText(LoginFragment.this.getContext(), welcome, Toast.LENGTH_SHORT).show();

                            //Switch view
                            ((MainActivity)LoginFragment.this.getActivity()).switchMapView();
                        } else {
                            Toast.makeText(LoginFragment.this.getView().getContext(), "Unable to authenticate. Please check your username and/or password and try again.", Toast.LENGTH_SHORT).show();
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
        }

        private class LoginTask implements Runnable {
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
                ServerProxy serverProxy = new ServerProxy();
                LoginResult loginResult = serverProxy.login(urlString, loginRequest);
                if(loginResult.isSuccess()) {
                    PersonResult personResult = serverProxy.loadUser(urlString, loginResult.getPerson_id());
                    if(personResult.isSuccess()) {
                        serverProxy.loadPersons(urlString, loginResult.getAuthtoken());
                        serverProxy.loadEvents(urlString, loginResult.getAuthtoken());
                    }
                } else {
                    Toast.makeText(LoginFragment.this.getContext(),
                            ServerErrorMsg,
                            Toast.LENGTH_LONG).show();
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
    }

    private class RegisterHandler {
        protected static final String ARG_SUCCESS = "Success";
        protected static final String ARG_MESSAGE = "Message";
        protected static final String ARG_AUTH_TOKEN = "AuthToken";
        protected static final String ARG_USERNAME = "Username";
        protected static final String ARG_PERSON_ID = "PersonID";

        /**
         * register function creates handler and executor for RegisterTask. Displays toast,
         * loads AuthToken, initiates loadData, and switches view upon successful registration.
         */
        public void register(String urlString, RegisterRequest registerRequest) {
            System.out.println("Registering via " + urlString);

            if (registerRequest.isValid()) {
                try {
                    Handler registerThreadHandler = new Handler() {
                        @Override
                        public void handleMessage(Message message) {
                            Bundle bundle = message.getData();
                            if (bundle.getBoolean(ARG_SUCCESS)) {
                                Toast.makeText(LoginFragment.this.getView().getContext(), "Registration Successful!", Toast.LENGTH_SHORT).show();
                                DataCache dataCache = DataCache.getInstance();
                                String welcome = "Welcome, " + dataCache.getUser().getFirst_name() + " " + dataCache.getUser().getLast_name() + "!";
                                Toast.makeText(LoginFragment.this.getContext(), welcome, Toast.LENGTH_SHORT).show();

                                //Switch view
                                ((MainActivity)LoginFragment.this.getActivity()).switchMapView();
                            } else {
                                Toast.makeText(LoginFragment.this.getView().getContext(), "Username already exists!", Toast.LENGTH_LONG).show();
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
            }
        }

        /**
         * private Runnable class RegisterTask uses registerHandler class to connect to the server and perform
         * the "register" task.
         */
        private class RegisterTask implements Runnable {
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
                ServerProxy serverProxy = new ServerProxy();
                RegisterResult registerResult = serverProxy.register(urlString, registerRequest);
                if(registerResult.isSuccess()) {
                    PersonResult personResult = serverProxy.loadUser(urlString, registerResult.getPerson_id());
                    if(personResult.isSuccess()) {
                        serverProxy.loadPersons(urlString, registerResult.getAuth_token());
                        serverProxy.loadEvents(urlString, registerResult.getAuth_token());
                    }
                } else {
                    Toast.makeText(LoginFragment.this.getContext(),
                            ServerErrorMsg,
                            Toast.LENGTH_LONG).show();
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
    }
}