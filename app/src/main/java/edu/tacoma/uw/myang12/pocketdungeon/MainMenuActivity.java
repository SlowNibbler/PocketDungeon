package edu.tacoma.uw.myang12.pocketdungeon;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import edu.tacoma.uw.myang12.pocketdungeon.authenticate.SignInActivity;
import edu.tacoma.uw.myang12.pocketdungeon.campaign.CampaignListActivity;
import edu.tacoma.uw.myang12.pocketdungeon.character.CharacterListActivity;

/**
 * The main screen displays when user open the app.
 */
public class MainMenuActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

    }

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
            finish();
        }

        if (item.getItemId() == R.id.action_character) {
            Intent i = new Intent(this, CharacterListActivity.class);
            startActivity(i);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
