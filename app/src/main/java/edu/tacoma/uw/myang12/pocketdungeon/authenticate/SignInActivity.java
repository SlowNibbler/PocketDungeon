package edu.tacoma.uw.myang12.pocketdungeon.authenticate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import edu.tacoma.uw.myang12.pocketdungeon.MainMenuActivity;
import edu.tacoma.uw.myang12.pocketdungeon.R;

/**
 * SignIn activity checks if a user was previously logged in.
 */
public class SignInActivity extends AppCompatActivity
        implements LoginFragment.LoginFragmentListener, RegisterFragment.RegisterFragmentListener {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        /** Use SharedPreferences to check if the user was previously logged in */
        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);

        /** if not logged in, open login screen */
        if (!mSharedPreferences.getBoolean(getString(R.string.LOGGEDIN), false)) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.sign_in_fragment_id, new LoginFragment())
                    .commit();

        /** if previously logged in, open main screen */
        } else {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void login(String email, String pwd) {
        mSharedPreferences
                .edit()
                .putBoolean(getString(R.string.LOGGEDIN), true)
                .commit();
    }

    @Override
    public void register(String email, String pwd) {

    }
}
