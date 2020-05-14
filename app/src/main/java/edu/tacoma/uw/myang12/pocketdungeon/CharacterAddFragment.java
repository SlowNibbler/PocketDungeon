package edu.tacoma.uw.myang12.pocketdungeon;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.tacoma.uw.myang12.pocketdungeon.model.Character;




/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CharacterAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CharacterAddFragment extends Fragment {

    private AddListener mAddListener;

    public interface AddListener {
        public void addCharacter(Character character);
    }
    public CharacterAddFragment(){

    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CourseAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CharacterAddFragment newInstance(String param1, String param2) {
        CharacterAddFragment fragment = new CharacterAddFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddListener = (AddListener) getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_character_add, container
                , false);
        getActivity().setTitle("Add a New Character");
        final EditText characterNameEditText = v.findViewById(R.id.add_character_name);
        final EditText characterClassEditText = v.findViewById(R.id.add_character_class);
        final EditText characterRaceEditText = v.findViewById(R.id.add_character_race);
        final EditText characterLevelEditText = v.findViewById(R.id.add_character_level);
        Button addButton = v.findViewById(R.id.btn_add_character);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String charName = characterNameEditText.getText().toString();
                String charClass = characterClassEditText.getText().toString();
                String charRace = characterRaceEditText.getText().toString();
                String charLevel = characterLevelEditText.getText().toString();
                //Character character = new Character(charName, charClass, charRace, charLevel);
                Character character = new Character(charName, charClass, charRace, charLevel, "0", "0", "0", "0","0", "0");
                if (mAddListener != null) {
                    mAddListener.addCharacter(character);
                }
            }
        });

        return v;

    }
}
