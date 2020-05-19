package edu.tacoma.uw.myang12.pocketdungeon.character;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;


/**
 * A simple fragment helps to add a character.
 */
public class CharacterAddFragment extends Fragment {
    private AddListener mAddListener;

    public interface AddListener {
        void addCharacter(Character character);
    }

    public CharacterAddFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddListener = (AddListener) getActivity();
    }

    /** Get user's input from text field, pass it to construct a Character object. */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_character_add, container, false);
        getActivity().setTitle("Add a New Character");

        final EditText character_name = view.findViewById(R.id.character_name_input);
        final EditText character_class = view.findViewById(R.id.character_class_input);
        final EditText character_race = view.findViewById(R.id.character_race_input);
        final EditText character_level = view.findViewById(R.id.character_level_input);
        final EditText character_strength = view.findViewById(R.id.character_strength_input);
        final EditText character_dexterity = view.findViewById(R.id.character_dexterity_input);
        final EditText character_constitution = view.findViewById(R.id.character_constitution_input);
        final EditText character_intelligence = view.findViewById(R.id.character_intelligence_input);
        final EditText character_wisdom = view.findViewById(R.id.character_wisdom_input);
        final EditText character_charisma = view.findViewById(R.id.character_charisma_input);

        Button addButton = view.findViewById(R.id.btn_add_character);
        addButton.setOnClickListener(new View.OnClickListener() {
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

                Character character = new Character(characterName, characterClass, characterRace,
                        characterLevel, characterStrength, characterDexterity,
                        characterConstitution, characterIntelligence, characterWisdom,
                        characterCharisma);
                if (mAddListener != null) {
                    mAddListener.addCharacter(character);
                }
            }
        });
        return view;
    }
}
