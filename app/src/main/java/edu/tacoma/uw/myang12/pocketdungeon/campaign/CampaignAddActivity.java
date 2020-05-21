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
import java.io.Serializable;
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
    private EditText campaign_code;
    private Button add_button;
    private Button search_button;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCampaignJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_add);

        campaign_name = findViewById(R.id.campaign_name_input);
        campaign_notes = findViewById(R.id.campaign_notes_input);
        campaign_code = findViewById(R.id.campaign_code_input);

        add_button = findViewById(R.id.add_button);
        search_button = findViewById(R.id.search_button);

        /** Set up add button listener.
         * Get campaign name and notes from user entry. */
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campaignName = campaign_name.getText().toString();
                String campaignNotes = campaign_notes.getText().toString();
                mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

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

        /** Set up search button listener.
         * Get campaign code from user entry. */
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String campaignId = campaign_code.getText().toString();
                StringBuilder url = new StringBuilder(getString(R.string.join_campaign));
                url.append(campaignId);
                mCampaignJSON = new JSONObject();
                new CampaignAddActivity.SearchCampaignTask().execute(url.toString());
            }
        });
    }

    /** Send post request to server, adding campaign details into server. */
    private class AddCampaignTask extends AsyncTask<String, Void, String> {

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
                    Log.i("Add_Campaign", mCampaignJSON.toString());

                    wr.write(mCampaignJSON.toString());
                    wr.flush();
                    wr.close();

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

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to add the new campaign")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                // For Debugging
                Log.i("Add_campaign", jsonObject.toString());

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

    /** Search campaign by campaign code. If successful, go to join campaign screen and display result. */
    private class SearchCampaignTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to find the campaign, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {

                    Campaign campaign = Campaign.parseJoinCampaign(
                            jsonObject.getString("names"));

                    Intent intent = new Intent(CampaignAddActivity.this, CampaignJoinActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("CAMPAIGN", (Serializable) campaign);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "Unable to find campaign: Invalid Code",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
