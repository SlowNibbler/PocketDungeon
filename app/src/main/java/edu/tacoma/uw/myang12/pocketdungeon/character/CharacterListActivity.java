package edu.tacoma.uw.myang12.pocketdungeon.character;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * An activity representing a list of Characters.
 * The activity presents a list of characters, which when touched,
 * lead to a {@link CharacterDetailActivity} representing
 * character details.
 */
public class CharacterListActivity extends AppCompatActivity {
    private List<Character> mCharacterList;
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCharacterJSON;
    private StringBuilder url;

    /** Set up display page and add button listener. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCharacterAddFragment();
            }
        });

        /** Use SharedPreferences to retrieve userID for query. */
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

        /** Set up url and append userID in the url query field. */
        url = new StringBuilder(getString(R.string.get_characters));
        url.append(userID);

        mRecyclerView = findViewById(R.id.character_list);
        assert mRecyclerView != null;
        setupRecyclerView((RecyclerView) mRecyclerView);
    }

    /** Send GET method to server and set up RecyclerView. */
    @Override
    protected void onResume() {
        super.onResume();
        if (mCharacterList == null) {
            mCharacterJSON = new JSONObject();
            new CharacterListActivity.CharacterTask().execute(url.toString());
            setupRecyclerView(mRecyclerView);
        }
    }

    /** Set up add character fragment. */
    private void launchCharacterAddFragment() {
        CharacterAddFragment characterAddFragment = new CharacterAddFragment();
        Intent intent = new Intent(this, CharacterDetailActivity.class);
        intent.putExtra(CharacterDetailActivity.ADD_CHARACTER, true);
        startActivity(intent);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, mCharacterList));
        }
    }

    /** Class to build RecyclerView and View holders. */
    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CharacterListActivity mParentActivity;
        private final List<Character> mValues;

        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Character item = (Character) view.getTag();

                Context context = view.getContext();
                Intent intent = new Intent(context, CharacterDetailActivity.class);
                intent.putExtra(CharacterDetailFragment.ARG_ITEM_ID, item);

                context.startActivity(intent);
            }
        };

        SimpleItemRecyclerViewAdapter(CharacterListActivity parent,
                                      List<Character> items) {
            mValues = items;
            mParentActivity = parent;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mIdView.setText(mValues.get(position).getCharacterName());
            holder.mContentView.setText(mValues.get(position).getCharacterClass());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        /** Return the size of character list (invoked by the layout manager) */
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }

    /** Send get request to server, construct a character list for display. */
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

                    // For Debugging
                    Log.i("characterJson", mCharacterJSON.toString());

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
