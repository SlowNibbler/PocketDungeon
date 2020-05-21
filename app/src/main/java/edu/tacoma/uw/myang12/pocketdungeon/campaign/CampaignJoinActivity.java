/**
 * This class handles joining an existing campaign.
 * User can search for a campaign by entering campaign code
 * and join with a specific character.
 *
 * @author: Meng Yang
 */
package edu.tacoma.uw.myang12.pocketdungeon.campaign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import edu.tacoma.uw.myang12.pocketdungeon.character.CharacterListActivity;
import edu.tacoma.uw.myang12.pocketdungeon.model.Campaign;
import edu.tacoma.uw.myang12.pocketdungeon.R;

public class CampaignJoinActivity extends AppCompatActivity {

    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campaign_join);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        Campaign campaign = (Campaign) bundle.getSerializable("CAMPAIGN");

        mSharedPreferences = getSharedPreferences(getString(R.string.LOGIN_PREFS),
                Context.MODE_PRIVATE);
        mSharedPreferences.edit()
                .putInt(getString(R.string.CAMPAIGNID), campaign.getCampaignID())
                .commit();

        String campaignId = String.valueOf(campaign.getCampaignID());
        String campaignName = campaign.getCampaignName();
        String campaignNotes = campaign.getGetCampaignNotes();

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Code: " + campaignId + "\n");
        stringBuilder.append("Name: " + campaignName + "\n");
        stringBuilder.append("Description: " + campaignNotes);

        TextView textView = findViewById(R.id.search_result);
        textView.setText(stringBuilder);

        Button charButton = findViewById(R.id.select_char_btn);
        charButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CampaignJoinActivity.this, CharacterListActivity.class);
                startActivity(i);
            }
        });
    }
}
