package edu.tacoma.uw.myang12.pocketdungeon.character;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.appbar.CollapsingToolbarLayout;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;


/**
 * A fragment representing a single Characters detail screen.
 */
public class CharacterDetailFragment extends Fragment {

    public static final String ARG_ITEM_ID = "item_id";
    private Character mCharacter;
    private SharedPreferences mSharedPreferences;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CharacterDetailFragment() {
    }

    /** Set up display page and title. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            mCharacter = (Character) getArguments().getSerializable(ARG_ITEM_ID);

            /** Store character id into SharedPreferences. */
            mSharedPreferences = getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS),
                    Context.MODE_PRIVATE);
            mSharedPreferences.edit()
                    .putInt(getString(R.string.CHARACTERID), mCharacter.getmCharacterID())
                    .commit();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                    activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mCharacter.getCharacterName());
            }
        }
    }

    /**
     * Construct the details for character to display in a TextView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.character_detail, container, false);

        // Show the character detail as text in a TextView.
        if (mCharacter != null) {
            ((TextView) rootView.findViewById(R.id.item_detail_name)).setText(
                    "Name: " + mCharacter.getCharacterName());
            ((TextView) rootView.findViewById(R.id.item_detail_class)).setText(
                    "Class: " + mCharacter.getCharacterClass());
            ((TextView) rootView.findViewById(R.id.item_detail_race)).setText(
                    "Race: " + mCharacter.getCharacterRace());
            ((TextView) rootView.findViewById(R.id.item_detail_level)).setText(
                    "Level: " + mCharacter.getCharacterLevel());
            ((TextView) rootView.findViewById(R.id.item_detail_str)).setText(
                    "Strength: " + mCharacter.getCharacterStr());
            ((TextView) rootView.findViewById(R.id.item_detail_dex)).setText(
                    "Dexterity: " + mCharacter.getCharacterDex());
            ((TextView) rootView.findViewById(R.id.item_detail_const)).setText(
                    "Constitution: " + mCharacter.getCharacterConst());
            ((TextView) rootView.findViewById(R.id.item_detail_int)).setText(
                    "Intelligence: " + mCharacter.getCharacterInt());
            ((TextView) rootView.findViewById(R.id.item_detail_wis)).setText(
                    "Wisdom: " + mCharacter.getCharacterWis());
            ((TextView) rootView.findViewById(R.id.item_detail_cha)).setText(
                    "Charisma: " + mCharacter.getCharacterCha());
        }

        return rootView;
    }
}
