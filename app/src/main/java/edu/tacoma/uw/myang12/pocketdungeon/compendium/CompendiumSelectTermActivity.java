package edu.tacoma.uw.myang12.pocketdungeon.compendium;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import edu.tacoma.uw.myang12.pocketdungeon.R;

public class CompendiumSelectTermActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compendium_select_term);

        Button searchButton = findViewById(R.id.button_search);
        final EditText userText = findViewById(R.id.search_term);
        final Spinner userSearchType = findViewById(R.id.term_spinner);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchText = userText.getText().toString();

                if (searchText.length() > 0) {
                    String searchType = userSearchType.getSelectedItem().toString();
                    SearchFragment fragment = new SearchFragment().newInstance(searchType, searchText);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.spell_container, fragment)
                            .commit();
                }
            }
        });


    }



    /* https://stackoverflow.com/questions/54004976/in-android-how-to-create-an-outlined-dropdown-menu-spinner-as-specified-by-th */
}
