package main;

import android.os.Bundle;
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

import HTTPConnection.LoginHandler;
import HTTPConnection.RegisterHandler;

import request.LoginRequest;
import request.RegisterRequest;

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
        Button loginButton = (Button) view.findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> createLoginHandler());



        //register button
        Button registerButton = (Button) view.findViewById(R.id.registerButton);
        registerButton.setOnClickListener(v -> createRegisterHandler());

        checkLoginFields();
        checkRegisterFields();
        return view;
    }

    private void createLoginHandler() {
        String urlString = getURL();
        LoginRequest loginRequest = getLoginRequest();
        if (loginRequest.isValid()) {
            LoginHandler loginHandler = new LoginHandler();
            loginHandler.login(this, urlString, loginRequest);
        } else {
            Toast.makeText(view.getContext(), "Missing required field. Please ensure that all fields are properly completed.", Toast.LENGTH_LONG).show();
        }
    }

    private void createRegisterHandler() {
        String urlString = getURL();
        RegisterRequest registerRequest = getRegisterRequest();
        if (registerRequest.isValid()) {
            RegisterHandler registerHandler = new RegisterHandler();
            registerHandler.register(this, urlString, registerRequest);
        } else {
            Toast.makeText(view.getContext(), "Missing required field. Please ensure that all fields are properly completed.", Toast.LENGTH_LONG).show();
        }
    }

    private void checkLoginFields() {
        Button login = (Button) view.findViewById(R.id.loginButton);

        String hostIP = hostIPText.getText().toString();
        String port = portText.getText().toString();
        String username = usernameText.getText().toString();
        String password = passwordText.getText().toString();

        login.setEnabled(!hostIP.equals("") && !port.equals("") && !username.equals("") && !password.equals(""));
    }

    private void checkRegisterFields() {
        Button register = (Button) view.findViewById(R.id.registerButton);

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
}