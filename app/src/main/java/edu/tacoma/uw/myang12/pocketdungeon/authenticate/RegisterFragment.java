package edu.tacoma.uw.myang12.pocketdungeon.authenticate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.myang12.pocketdungeon.MainMenuActivity;
import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.User;

/**
 * Register fragment to create an account.
 */
public class RegisterFragment extends Fragment {

    private RegisterFragmentListener mRegisterFragmentListener;
    private JSONObject mUserJSON;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public interface RegisterFragmentListener {
        void register(String email, String pwd);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        getActivity().setTitle("Create Account");
        mRegisterFragmentListener = (RegisterFragmentListener) getActivity();
        final EditText edtEmail = view.findViewById(R.id.register_email);
        final EditText edtPassword = view.findViewById(R.id.register_password);
        Button btnRegister = view.findViewById(R.id.button_register);

        /** when user clicks on the register button */
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();
                StringBuilder url = new StringBuilder(getString(R.string.register));

                /** construct a JSONObject to build a formatted message to send */
                mUserJSON = new JSONObject();
                try {
                    mUserJSON.put(User.EMAIL, email);
                    mUserJSON.put(User.PASSWORD, password);
                    new RegisterFragment.RegisterAsyncTask().execute(url.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mRegisterFragmentListener.register(email, password);
            }
        });
        return view;
    }

    /** send post request to server */
    private class RegisterAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setRequestProperty("Content-Type", "application/json");
                    urlConnection.setDoOutput(true);

                    OutputStreamWriter wr =
                            new OutputStreamWriter(urlConnection.getOutputStream());

                    // For Debugging
                    Log.i("REGISTER", mUserJSON.toString());
                    wr.write(mUserJSON.toString());
                    wr.flush();
                    wr.close();

                    /** get response from server*/
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to register, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /** actions after received response from server */
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                /** If register successfully, go to main screen. */
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getActivity(),
                            "Register Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }

                /** If register failed, show error message. */
                else {
                    Toast.makeText(getActivity(),
                            "Register Failed: email already exists.",
                            Toast.LENGTH_SHORT).show();
                    Log.e("REGISTER", jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                e.getMessage();
            }
        }
    }
}
