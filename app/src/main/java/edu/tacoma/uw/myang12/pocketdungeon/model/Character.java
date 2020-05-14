package edu.tacoma.uw.myang12.pocketdungeon.model;



        import org.json.JSONArray;
        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.Serializable;
        import java.util.ArrayList;
        import java.util.List;

public class Character implements Serializable {

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


    public static final String CHARACTERNAME = "charactername";
    public static final String CHARACTERCLASS = "characterclass";
    public static final String CHARACTERRACE = "characterrace";
    public static final String CHARACTERLEVEL = "0";
    public static final String CHARACTERSTR = "0";
    public static final String CHARACTERDEX = "0";
    public static final String CHARACTERCONST = "0";
    public static final String CHARACTERINT = "0";
    public static final String CHARACTERWIS = "0";
    public static final String CHARACTERCHA = "0";

    public Character(String name, String charClass, String race, String level, String str, String dex, String constI, String intl, String wis, String cha) {
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



    public String toString() {
        String temp = "Name: " + this.mCharacterName + "Level: " + this.mCharacterLevel;
        return temp;
    }

    public static List<Character> parseCourseJSON(String courseJson) throws JSONException {
        List<Character> characterList = new ArrayList<>();
        if (courseJson != null) {
            JSONArray arr = new JSONArray(courseJson);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Character character = new Character(obj.getString(Character.CHARACTERNAME), obj.getString(Character.CHARACTERCLASS), obj.getString(Character.CHARACTERRACE),
                        obj.getString(Character.CHARACTERLEVEL), obj.getString(Character.CHARACTERSTR), obj.getString(Character.CHARACTERDEX), obj.getString(Character.CHARACTERCONST),
                        obj.getString(Character.CHARACTERINT), obj.getString(Character.CHARACTERWIS), obj.getString(Character.CHARACTERCHA));
                //System.out.println("PARSE: " + course.toString());
                characterList.add(character);
            }
        }
        return characterList;
    }


}






























