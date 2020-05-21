package edu.tacoma.uw.myang12.pocketdungeon.model;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** A class for Character object, a character has name, class, race, level, strength, dexterity,
 * constitution, intelligence, wisdom and charisma. */
public class Character implements Serializable {

    private int mCharacterID;
    private String mCharacterName;
    private String mCharacterClass;
    private String mCharacterRace;
    private String mCharacterLevel;
    private String mCharacterStr;
    private String mCharacterDex;
    private String mCharacterConst;
    private String mCharacterInt;
    private String mCharacterWis;
    private String mCharacterCha;

    // fields for query database
    public static final String CHARACTERID = "characterid";
    public static final String CHARACTERNAME = "charactername";
    public static final String CHARACTERCLASS = "characterclass";
    public static final String CHARACTERRACE = "characterrace";
    public static final String CHARACTERLEVEL = "characterlevel";
    public static final String CHARACTERSTR = "strength";
    public static final String CHARACTERDEX = "dexterity";
    public static final String CHARACTERCONST = "constitution";
    public static final String CHARACTERINT = "intelligence";
    public static final String CHARACTERWIS = "wisdom";
    public static final String CHARACTERCHA = "charisma";

    public Character(int id, String name, String charClass, String race, String level, String str,
                     String dex, String constI, String intl, String wis, String cha) {
        mCharacterID = id;
        mCharacterName = name;
        mCharacterClass = charClass;
        mCharacterRace = race;
        mCharacterLevel = level;
        mCharacterStr = str;
        mCharacterDex = dex;
        mCharacterConst = constI;
        mCharacterInt = intl;
        mCharacterWis = wis;
        mCharacterCha = cha;
    }

    public int getmCharacterID() {
        return mCharacterID;
    }

    public void setmCharacterID(int mCharacterID) {
        this.mCharacterID = mCharacterID;
    }

    public String getCharacterName() {return this.mCharacterName;}

    public String getCharacterClass() {
        return this.mCharacterClass;
    }

    public String getCharacterRace() {
        return this.mCharacterRace;
    }

    public String getCharacterLevel() {
        return this.mCharacterLevel;
    }

    public String getCharacterStr() {
        return this.mCharacterStr;
    }

    public String getCharacterDex() {
        return this.mCharacterDex;
    }

    public String getCharacterConst() {
        return this.mCharacterConst;
    }

    public String getCharacterInt() {
        return this.mCharacterInt;
    }

    public String getCharacterWis() {
        return this.mCharacterWis;
    }

    public String getCharacterCha() {
        return this.mCharacterCha;
    }



    public void setCharacterName(String newVar) {
        this.mCharacterName = newVar;
    }
    public void setmCharacterClass(String newVar) {
        this.mCharacterClass = newVar;
    }
    public void setmCharacterRace(String newVar) {
        this.mCharacterRace = newVar;
    }
    public void setmCharacterLevel(String newVar) {
        this.mCharacterLevel = newVar;
    }
    public void setmCharacterStr(String newVar) {
        this.mCharacterStr = newVar;
    }
    public void setmCharacterDex(String newVar) {
        this.mCharacterDex = newVar;
    }
    public void setmCharacterConst(String newVar) {
        this.mCharacterConst = newVar;
    }
    public void setmCharacterInt(String newVar) {
        this.mCharacterInt = newVar;
    }
    public void setmCharacterWis(String newVar) {
        this.mCharacterWis = newVar;
    }
    public void setmCharacterCha(String newVar) {
        this.mCharacterCha = newVar;
    }


/** Should this be removed? */
//    public String toString() {
//        String temp = "Name: " + this.mCharacterName + "Level: " + this.mCharacterLevel;
//        return temp;
//    }

    /** method to construct a character list by parsing JsonObject. */
    public static List<Character> parseCharacterJSON(String characterJson) throws JSONException {
        List<Character> characterList = new ArrayList<>();
        if (characterJson != null) {

            JSONArray arr = new JSONArray(characterJson);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Character character = new Character(obj.getInt(Character.CHARACTERID),
                        obj.getString(Character.CHARACTERNAME),
                        obj.getString(Character.CHARACTERCLASS),
                        obj.getString(Character.CHARACTERRACE),
                        String.valueOf(obj.getInt(Character.CHARACTERLEVEL)),
                        String.valueOf(obj.getInt(Character.CHARACTERSTR)),
                        String.valueOf(obj.getInt(Character.CHARACTERDEX)),
                        String.valueOf(obj.getInt(Character.CHARACTERCONST)),
                        String.valueOf(obj.getInt(Character.CHARACTERINT)),
                        String.valueOf(obj.getInt(Character.CHARACTERWIS)),
                        String.valueOf(obj.getInt(Character.CHARACTERCHA)));
                characterList.add(character);
            }
        }
        return characterList;
    }


}






























