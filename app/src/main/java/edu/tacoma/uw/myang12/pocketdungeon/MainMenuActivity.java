package edu.tacoma.uw.myang12.pocketdungeon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import edu.tacoma.uw.myang12.pocketdungeon.authenticate.SignInActivity;
import edu.tacoma.uw.myang12.pocketdungeon.campaign.CampaignListActivity;
import edu.tacoma.uw.myang12.pocketdungeon.character.CharacterListActivity;
import edu.tacoma.uw.myang12.pocketdungeon.compendium.CompendiumActivity;
import edu.tacoma.uw.myang12.pocketdungeon.compendium.CompendiumSelectTermActivity;

/**
 * The main screen displaying when user opens the app.
 */
public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    /** Set up the buttons in main screen. */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }

    /** When user clicks on sign out button, go to sign in screen. */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_logout) {
            SharedPreferences sharedPreferences =
                    getSharedPreferences(getString(R.string.LOGIN_PREFS), Context.MODE_PRIVATE);
            sharedPreferences.edit().putBoolean(getString(R.string.LOGGEDIN), false)
                    .commit();
            Intent i = new Intent(this, SignInActivity.class);
            startActivity(i);
            finish();
        }

        if (item.getItemId() == R.id.action_campaign) {
            Intent i = new Intent(this, CampaignListActivity.class);
            startActivity(i);
        }

        if (item.getItemId() == R.id.action_character) {
            Intent i = new Intent(this, CharacterListActivity.class);
            startActivity(i);
        }

        if (item.getItemId() == R.id.action_search) {
            Intent i = new Intent(this, CompendiumSelectTermActivity.class);
            startActivity(i);
        }

        if (item.getItemId() == R.id.action_compendium) {
            Intent i = new Intent(this, CompendiumActivity.class);
            startActivity(i);
        }
        return super.onOptionsItemSelected(item);
    }
}
