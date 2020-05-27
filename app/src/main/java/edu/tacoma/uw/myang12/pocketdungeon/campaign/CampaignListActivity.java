/**
 * This class handles displaying the list of campaign under user's account.
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.Campaign;

/** This class retrieves and displays user's campaign list. */
public class CampaignListActivity extends AppCompatActivity {

    private List<Campaign> mCampaignList;
    private RecyclerView mRecyclerView;

    /** Get userID from SharedPreferences and query user's campaign list. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_list);

        FloatingActionButton add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CampaignListActivity.this, CampaignAddActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);
        StringBuilder url = new StringBuilder(getString(R.string.get_campaigns));
        url.append(userID);
        new CampaignListActivity.CampaignTask().execute(url.toString());

        mRecyclerView = findViewById(R.id.campaign_list);
        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCampaignList != null) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter
                    (this, mCampaignList));
            recyclerView.setLayoutManager(new LinearLayoutManager(CampaignListActivity.this));
        }
    }

    /** Class to build RecyclerView and View holders. */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CampaignListActivity mParent;
        private final List<Campaign> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int campaignId = view.getId();
                Context context = view.getContext();
                Intent intent = new Intent(context, CampaignCharacterActivity.class);
                intent.putExtra("campaignID", campaignId);
                context.startActivity(intent);
            }
        };

        // constructor
        SimpleItemRecyclerViewAdapter(CampaignListActivity parent,
                                      List<Campaign> items) {
            mParent = parent;
            mValues = items;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.campaign_list, parent, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(String.valueOf(mValues.get(position).getCampaignID()));
            holder.mNameView.setText(mValues.get(position).getCampaignName());
            holder.mNotesView.setText(mValues.get(position).getGetCampaignNotes());
            holder.mRoleView.setText(mValues.get(position).getCampaignRole());

            holder.itemView.setId(mValues.get(position).getCampaignID());
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        // Return the size of campaign list (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /** Provide a reference to the views for each data item */
        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mNameView;
            final TextView mNotesView;
            final TextView mRoleView;

            ViewHolder(View view) {
                super(view);
                mIdView = view.findViewById(R.id.campaign_id_txt);
                mNameView = view.findViewById(R.id.campaign_name_txt);
                mNotesView = view.findViewById(R.id.campaign_notes_txt);
                mRoleView = view.findViewById(R.id.campaign_role_txt);
            }
        }
    }

    /** Send get request to server, construct a campaign list for display. */
    private class CampaignTask extends AsyncTask<String, Void, String> {
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
                    response = "Unable to download the list of campaigns, Reason: "
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

                    mCampaignList = Campaign.parseCampaignJson(
                            jsonObject.getString("names"));

                    if (!mCampaignList.isEmpty()) {
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
