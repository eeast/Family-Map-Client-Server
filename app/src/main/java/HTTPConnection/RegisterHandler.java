package HTTPConnection;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import main.DataCache;
import main.MainActivity;
import model.AuthToken;
import request.RegisterRequest;
import result.RegisterResult;

public class RegisterHandler extends ServerProxy {

    /**
     * register function creates handler and executor for RegisterTask. Displays toast,
     * loads AuthToken, initiates loadData, and switches view upon successful registration.
     */
    public void register(Fragment fragment, String urlString, RegisterRequest registerRequest) {
        System.out.println("Registering via " + urlString);

        if (registerRequest.isValid()) {
            try {
                Handler registerThreadHandler = new Handler() {
                    @Override
                    public void handleMessage(Message message) {
                        Bundle bundle = message.getData();
                        if (bundle.getBoolean(ARG_SUCCESS)) {
                            Toast.makeText(fragment.getView().getContext(), "Registration Successful!", Toast.LENGTH_LONG).show();

                            //Save AuthToken
                            DataCache dataCache = DataCache.getInstance();
                            String authTokenString = bundle.getString(ARG_AUTH_TOKEN);
                            String username = bundle.getString(ARG_USERNAME);
                            AuthToken authToken = new AuthToken(authTokenString, username);
                            dataCache.setAuthToken(authToken);

                            //Start other data handlers
                            UserHandler userHandler = new UserHandler();
                            userHandler.loadUser(fragment.getView(), urlString, bundle.getString(ARG_PERSON_ID));
                            PersonsHandler personsHandler = new PersonsHandler();
                            personsHandler.loadFamily(urlString);
                            EventsHandler eventsHandler = new EventsHandler();
                            eventsHandler.loadFamilyEvents(urlString);

                            //Switch view
                            ((MainActivity)fragment.getActivity()).switchMapView();
                        } else {
                            Toast.makeText(fragment.getView().getContext(), "Username already exists!", Toast.LENGTH_LONG).show();
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
            RegisterResult registerResult = null;
            if (registerRequest != null){
                try {
                    registerResult = accessServer(urlString, registerRequest);
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

    private RegisterResult accessServer(String urlString, RegisterRequest request) throws IOException {
        String urlRegister = urlString + "/user/register";
        URL url = new URL(urlRegister);
        connection = (HttpURLConnection) url.openConnection();
        connection.setReadTimeout(5000);
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.connect();

        try (OutputStream requestBody = connection.getOutputStream();) {
            sendRequest(request, requestBody);
        }

        RegisterResult result = null;
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            Gson gsonDeserializer = new Gson();
            result = gsonDeserializer.fromJson(bufferedReader, RegisterResult.class);
        } else {
            result = new RegisterResult("Unable to complete registration");
        }
        return result;
    }
}
