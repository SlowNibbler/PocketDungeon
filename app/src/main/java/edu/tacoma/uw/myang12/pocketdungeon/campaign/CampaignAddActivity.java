package edu.tacoma.uw.myang12.pocketdungeon.campaign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.Campaign;
import edu.tacoma.uw.myang12.pocketdungeon.model.User;

/**
 * This class handles storing a new campaign into user's account.
 */
public class CampaignAddActivity extends AppCompatActivity {

    private EditText campaign_name;
    private EditText campaign_notes;
    private Button add_button;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCampaignJSON;

    /**
     * standard onCreate function to setup the view
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_add);

        campaign_name = findViewById(R.id.campaign_name_input);
        campaign_notes = findViewById(R.id.campaign_notes_input);

        /** Set up add button listener.
         * Get campaign name and notes from user entry. */
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            /**
             * Sends the users input to a JSON and then the database
             * @param v
             */
            @Override
            public void onClick(View v) {
                String campaignName = campaign_name.getText().toString();
                String campaignNotes = campaign_notes.getText().toString();

                /** Use SharedPreferences to retrieve userID for query. */
                mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

                /** Set up url and construct a JSONObject to build a formatted message to send. */
                StringBuilder url = new StringBuilder(getString(R.string.add_campaign));
                mCampaignJSON = new JSONObject();
                try {
                    mCampaignJSON.put(Campaign.NAME, campaignName);
                    mCampaignJSON.put(Campaign.NOTES, campaignNotes);
                    mCampaignJSON.put(User.ID, userID);
                    new CampaignAddActivity.AddCampaignTask().execute(url.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /** Send post request to server, adding campaign info into database. */
    private class AddCampaignTask extends AsyncTask<String, Void, String> {
        /**
         * Connects to the database to add the users data
         * @param urls
         * @return
         */
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
                    wr.write(mCampaignJSON.toString());
                    wr.flush();
                    wr.close();

                    /** Get response from server. */
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add the new campaign, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /** If campaign is added successfully, inform user and display campaign list.
         * Otherwise, send error message.
         * @param s response message
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to add the new campaign")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "Campaign Added successfully"
                            , Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CampaignAddActivity.this, CampaignListActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Campaign couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e("Add_Campaign", jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on Adding campaign"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e("Add_Campaign", e.getMessage());
            }
        }
    }
}
