/**
 * This class handles joining an existing campaign.
 * User can search for a campaign by entering campaign code
 * and join with a specific character.
 *
 * @author: Meng Yang
 */
package edu.tacoma.uw.myang12.pocketdungeon.campaign;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.myang12.pocketdungeon.model.Character;
import edu.tacoma.uw.myang12.pocketdungeon.character.CharacterListActivity;
import edu.tacoma.uw.myang12.pocketdungeon.model.Campaign;
import edu.tacoma.uw.myang12.pocketdungeon.R;

public class CampaignJoinActivity extends AppCompatActivity {

    private List<Character> mCharacterList;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_join);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Campaign campaign = (Campaign) bundle.getSerializable("CAMPAIGN");

        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mSharedPreferences.edit()
                .putInt(getString(R.string.CAMPAIGNID), campaign.getCampaignID())
                .commit();

        String campaignId = "Code: " + campaign.getCampaignID();
        String campaignName = "Name: " + campaign.getCampaignName();
        String campaignDesc = "Description: " + campaign.getGetCampaignNotes();

        TextView mIdView = findViewById(R.id.campaignId_txt);
        TextView mNameView = findViewById(R.id.campaignName_txt);
        TextView mDescView = findViewById(R.id.campaignDesc_txt);

        mIdView.setText(campaignId);
        mNameView.setText(campaignName);
        mDescView.setText(campaignDesc);

        Button charButton = findViewById(R.id.select_char_btn);
        charButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CampaignJoinActivity.this, CharacterListActivity.class);
                startActivity(i);
            }
        });

        StringBuilder url = new StringBuilder(getString(R.string.search_characters));
        url.append(campaign.getCampaignID());
        new CampaignJoinActivity.CharacterTask().execute(url.toString());

        mRecyclerView = findViewById(R.id.player_list);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            recyclerView.setAdapter(new CampaignJoinActivity.SimpleItemRecyclerViewAdapter(this, mCharacterList));
        }
        recyclerView.setLayoutManager(new LinearLayoutManager(CampaignJoinActivity.this));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<CampaignJoinActivity.SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CampaignJoinActivity mParentActivity;
        private final List<Character> mValues;

        SimpleItemRecyclerViewAdapter(CampaignJoinActivity parent,
                                      List<Character> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.campaign_character_list, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final CampaignJoinActivity.SimpleItemRecyclerViewAdapter.ViewHolder holder, int position) {
            holder.mNameView.setText(mValues.get(position).getCharacterName());
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameView;

            ViewHolder(View view) {
                super(view);
                mNameView = view.findViewById(R.id.character_name);
            }
        }
    }

    private class CharacterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    /** Get response from server. */
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }
                } catch (Exception e) {
                    response = "Unable to download the list of characters, Reason: "
                            + e.getMessage();
                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /** If character is retrieved successfully, inform user.
         * Otherwise, send error message.
         * @param s response message
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {
                    mCharacterList = Character.parseCharacterJSON(
                            jsonObject.getString("names"));

                    if (!mCharacterList.isEmpty()) {
                        setupRecyclerView(mRecyclerView);
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Error: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
}
