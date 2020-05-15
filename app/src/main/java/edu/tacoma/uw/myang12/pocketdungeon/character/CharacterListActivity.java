package edu.tacoma.uw.myang12.pocketdungeon.character;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;

public class CharacterListActivity extends AppCompatActivity {

    private List<Character> mCharacterList;
    private RecyclerView mRecyclerView;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCharacterJSON;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        mRecyclerView = findViewById(R.id.recyclerView);
        FloatingActionButton add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CharacterListActivity.this, CharacterAddActivity.class);
                startActivity(intent);
            }
        });

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

        StringBuilder url = new StringBuilder(getString(R.string.get_characters));
        url.append(userID);

        System.out.println("url: " + url);

        mCharacterJSON = new JSONObject();
        new CharacterListActivity.CharacterTask().execute(url.toString());

        assert mRecyclerView != null;
        setupRecyclerView(mRecyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        if (mCharacterList != null) {
            mRecyclerView.setAdapter(new SimpleItemRecyclerViewAdapter
                    (this, mCharacterList));
            mRecyclerView.setLayoutManager(new LinearLayoutManager(CharacterListActivity.this));
        }
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final CharacterListActivity mParent;
        private final List<Character> mValues;

        // constructor
        SimpleItemRecyclerViewAdapter(CharacterListActivity parent,
                                      List<Character> items) {
            mParent = parent;
            mValues = items;
        }

        // Create new views (invoked by the layout manager)
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.character_list, parent, false);
            return new ViewHolder(view);
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mNameView.setText(mValues.get(position).getCharacterName());
            holder.mLevelView.setText(mValues.get(position).getCharacterLevel());
            holder.mClassView.setText(mValues.get(position).getCharacterClass());
        }

        // Return the size of campaign list (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return mValues.size();
        }

        /** Provide a reference to the views for each data item */
        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mNameView;
            final TextView mLevelView;
            final TextView mClassView;
            LinearLayout mainLayout;

            ViewHolder(View view) {
                super(view);
                mNameView = view.findViewById(R.id.character_name_txt);
                mLevelView = view.findViewById(R.id.character_level_txt);
                mClassView = view.findViewById(R.id.character_class_txt);
                mainLayout = view.findViewById(R.id.mainLayout);
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
                    System.out.println("url2: " + url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    // For Debugging
                    Log.i("Get_characters", mCharacterJSON.toString());

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

        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                System.out.println("post");
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
