package edu.tacoma.uw.myang12.pocketdungeon.character;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.ActionBar;

import android.view.MenuItem;
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
import edu.tacoma.uw.myang12.pocketdungeon.model.User;
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;

/**
 * An activity representing a single Characters detail screen.
 */
public class CharacterDetailActivity extends AppCompatActivity
        implements CharacterAddFragment.AddListener {

    public static final String ADD_CHARACTER = "ADD_CHARACTER";
    private JSONObject mCharacterJSON;

    /** Set up display. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.select_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own detail action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        /** Create the detail of character and add it to the activity. */
        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            if (getIntent().getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID) != null) {
                arguments.putSerializable(CharacterDetailFragment.ARG_ITEM_ID,
                        getIntent().getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID));
                CharacterDetailFragment fragment = new CharacterDetailFragment();
                fragment.setArguments(arguments);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.character_detail_container, fragment)
                        .commit();
            } else if (getIntent().getBooleanExtra(CharacterDetailActivity.ADD_CHARACTER, false)) {
                CharacterAddFragment fragment = new CharacterAddFragment();
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.character_detail_container, fragment)
                        .commit();
            }
        }
    }

    /** Set up the go back button to Character list screen. */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            navigateUpTo(new Intent(this, CharacterListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Send post request to server, passing info through a JSONObject. */
    @Override
    public void addCharacter(Character character) {
        SharedPreferences mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
        int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

        StringBuilder url = new StringBuilder(getString(R.string.add_character));
        mCharacterJSON = new JSONObject();
        try {
            mCharacterJSON.put(Character.CHARACTERNAME, character.getCharacterName());
            mCharacterJSON.put(Character.CHARACTERCLASS, character.getCharacterClass());
            mCharacterJSON.put(Character.CHARACTERRACE, character.getCharacterRace());
            mCharacterJSON.put(Character.CHARACTERLEVEL, character.getCharacterLevel());
            mCharacterJSON.put(Character.CHARACTERSTR, character.getCharacterStr());
            mCharacterJSON.put(Character.CHARACTERDEX, character.getCharacterDex());
            mCharacterJSON.put(Character.CHARACTERCONST, character.getCharacterConst());
            mCharacterJSON.put(Character.CHARACTERINT, character.getCharacterInt());
            mCharacterJSON.put(Character.CHARACTERWIS, character.getCharacterWis());
            mCharacterJSON.put(Character.CHARACTERCHA, character.getCharacterCha());
            mCharacterJSON.put(User.ID, userID);
            new AddCharacterTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on adding a character: "
                    + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /** Send post request to server, adding character info into database. */
    private class AddCharacterTask extends AsyncTask<String, Void, String> {

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

                    wr.write(mCharacterJSON.toString());
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
                    response = "Unable to add the new character, Reason: "
                            + e.getMessage();
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /** If character is added successfully, inform user.
         * Otherwise, send error message.
         * @param s response message
         */
        @Override
        protected void onPostExecute(String s) {
            if (s.startsWith("Unable to add the new character")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "Character Added successfully"
                            , Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(CharacterDetailActivity.this, CharacterListActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Character couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e("Add_Character", jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on Adding character"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e("Add_Character", e.getMessage());
            }
        }
    }
}
