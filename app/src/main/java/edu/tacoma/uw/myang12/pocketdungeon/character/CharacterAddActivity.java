package edu.tacoma.uw.myang12.pocketdungeon.character;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;
import edu.tacoma.uw.myang12.pocketdungeon.model.User;

public class CharacterAddActivity extends AppCompatActivity {

    private EditText character_name;
    private EditText character_class;
    private EditText character_race;
    private EditText character_level;
    private EditText character_strength;
    private EditText character_dexterity;
    private EditText character_constitution;
    private EditText character_intelligence;
    private EditText character_wisdom;
    private EditText character_charisma;

    private Button add_button;
    private SharedPreferences mSharedPreferences;
    private JSONObject mCharacterJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_add);

        character_name = findViewById(R.id.character_name_input);
        character_class = findViewById(R.id.character_class_input);
        character_race = findViewById(R.id.character_race_input);
        character_level = findViewById(R.id.character_level_input);
        character_strength = findViewById(R.id.character_strength_input);
        character_dexterity = findViewById(R.id.character_dexterity_input);
        character_constitution = findViewById(R.id.character_constitution_input);
        character_intelligence = findViewById(R.id.character_intelligence_input);
        character_wisdom = findViewById(R.id.character_wisdom_input);
        character_charisma = findViewById(R.id.character_charisma_input);

        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String characterName = character_name.getText().toString();
                String characterClass = character_class.getText().toString();
                String characterRace = character_race.getText().toString();
                String characterLevel = character_level.getText().toString();
                String characterStrength = character_strength.getText().toString();
                String characterDexterity = character_dexterity.getText().toString();
                String characterConstitution = character_constitution.getText().toString();
                String characterIntelligence = character_intelligence.getText().toString();
                String characterWisdom = character_wisdom.getText().toString();
                String characterCharisma = character_charisma.getText().toString();

                mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
                int userID = mSharedPreferences.getInt(getString(R.string.USERID), 0);

                StringBuilder url = new StringBuilder(getString(R.string.add_character));
                mCharacterJSON = new JSONObject();
                try {
                    mCharacterJSON.put(Character.CHARACTERNAME, characterName);
                    mCharacterJSON.put(Character.CHARACTERCLASS, characterClass);
                    mCharacterJSON.put(Character.CHARACTERRACE, characterRace);
                    mCharacterJSON.put(Character.CHARACTERLEVEL, characterLevel);
                    mCharacterJSON.put(Character.CHARACTERSTR, characterStrength);
                    mCharacterJSON.put(Character.CHARACTERDEX, characterDexterity);
                    mCharacterJSON.put(Character.CHARACTERCONST, characterConstitution);
                    mCharacterJSON.put(Character.CHARACTERINT, characterIntelligence);
                    mCharacterJSON.put(Character.CHARACTERWIS, characterWisdom);
                    mCharacterJSON.put(Character.CHARACTERCHA, characterCharisma);
                    mCharacterJSON.put(User.ID, userID);
                    new CharacterAddActivity.AddCharacterTask().execute(url.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class AddCharacterTask extends AsyncTask<String, Void, String> {

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
