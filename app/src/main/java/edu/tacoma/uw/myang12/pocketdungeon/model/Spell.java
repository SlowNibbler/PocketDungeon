package edu.tacoma.uw.myang12.pocketdungeon.model;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.Serializable;

public class Spell implements Serializable {

    private String mName;
    private String mDesc;
    private String mHigher_level;
    private String mRange;

    private static final String SPELL_NAME = "name";
    private static final String SPELL_DESC = "desc";
    private static final String SPELL_HIGHER_LEVEL = "higher_level";
    private static final String SPELL_RANGE = "range";

    public String getSpellName() {
        return this.mName;
    }

    public String getSpellDesc() {
        return this.mDesc;
    }

    public String getSpellHigherLevel() {
        return this.mHigher_level;
    }

    public String getSpellRange() {
        return this.mRange;
    }

    public void setSpellName(String newValue) {
        this.mName = newValue;
    }

    public void setSpellDesc(String newValue) {
        this.mDesc = newValue;
    }

    public void setSpellHigherLevel(String newValue) {
        this.mHigher_level = newValue;
    }

    public void setSpellRange(String newValue) {
        this.mRange = newValue;
    }



}
