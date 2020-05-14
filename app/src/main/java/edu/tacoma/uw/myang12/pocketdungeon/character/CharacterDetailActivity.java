package edu.tacoma.uw.myang12.pocketdungeon.character;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;


/**
 * An activity representing a single Item detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link CharacterListActivity}.
 */
public class CharacterDetailActivity extends AppCompatActivity implements CharacterAddFragment.AddListener {

    public static final String ADD_CHARACTER = "ADD_CHARACTER";
    private JSONObject mCharacterJSON;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //

        if (savedInstanceState == null) {
            Bundle arguments = new Bundle();
            if (getIntent().getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID) != null) {
                arguments.putSerializable(CharacterDetailFragment.ARG_ITEM_ID,
                        getIntent().getSerializableExtra(CharacterDetailFragment.ARG_ITEM_ID));
                CharacterDetailFragment fragment = new CharacterDetailFragment();

                fragment.setArguments(arguments);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.item_detail_container, fragment)
                        .commit();
            }
            else if (getIntent().getBooleanExtra(CharacterDetailActivity.ADD_CHARACTER, false)) {
                CharacterAddFragment fragment = new CharacterAddFragment();
                getSupportFragmentManager().beginTransaction().add(R.id.item_detail_container, fragment).commit();
            }



//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putSerializable(CourseDetailFragment.ARG_ITEM_ID,
//                    getIntent().getSerializableExtra(CourseDetailFragment.ARG_ITEM_ID));
//            CourseDetailFragment fragment = new CourseDetailFragment();
//
//            fragment.setArguments(arguments);
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.item_detail_container, fragment)
//                    .commit();
        }


//        if (savedInstanceState == null) {
//            // Create the detail fragment and add it to the activity
//            // using a fragment transaction.
//            Bundle arguments = new Bundle();
//            arguments.putString(CourseDetailFragment.ARG_ITEM_ID,
//                    getIntent().getStringExtra(CourseDetailFragment.ARG_ITEM_ID));
//
//            System.out.println("TESTING "+ CourseDetailFragment.ARG_ITEM_ID);
//
//            CourseDetailFragment fragment = new CourseDetailFragment();
//
//            fragment.setArguments(arguments);
//
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.item_detail_container, fragment)
//                    .commit();
//        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, CharacterListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addCharacter(Character character) {
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

            new AddCharacterAsyncTask().execute(url.toString());
        } catch (JSONException e) {
            Toast.makeText(this, "Error with JSON creation on addiong a character: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private class AddCharacterAsyncTask extends AsyncTask<String, Void, String> {
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
                    Log.i(ADD_CHARACTER, mCharacterJSON.toString());
                    wr.write(mCharacterJSON.toString());
                    wr.flush();
                    wr.close();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to add the new course, Reason: "
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
            if (s.startsWith("Unable to add the new course")) {
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("success")) {
                    Toast.makeText(getApplicationContext(), "Course Added successfully"
                            , Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Course couldn't be added: "
                                    + jsonObject.getString("error")
                            , Toast.LENGTH_LONG).show();
                    Log.e(ADD_CHARACTER, jsonObject.getString("error"));
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(), "JSON Parsing error on Adding course"
                                + e.getMessage()
                        , Toast.LENGTH_LONG).show();
                Log.e(ADD_CHARACTER, e.getMessage());
            }
        }
    }
}
