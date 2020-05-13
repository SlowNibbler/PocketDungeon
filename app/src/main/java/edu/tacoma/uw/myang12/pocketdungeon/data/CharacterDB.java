package edu.tacoma.uw.myang12.pocketdungeon.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.model.Character;

public class CharacterDB {

    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "members.db";

    private CharacterDBHelper mCharacterDBHelper;
    private SQLiteDatabase mSQLiteDatabase;

    public CharacterDB(Context context) {
        mCharacterDBHelper = new CharacterDBHelper(
                context, DB_NAME, null, DB_VERSION);
        mSQLiteDatabase = mCharacterDBHelper.getWritableDatabase();
    }

    public boolean insertCharacter(String name, String classChar, String race, String level, String str, String dex, String consti, String intl, String wis, String cha) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("CharacterName", name);
        contentValues.put("CharacterClass", classChar);
        contentValues.put("CharacterRace", race);
        contentValues.put("CharacterLevel", level);
        contentValues.put("Strength", str);
        contentValues.put("Dexterity", dex);
        contentValues.put("Constitution", consti);
        contentValues.put("Intelligence", intl);
        contentValues.put("Wisdom", wis);
        contentValues.put("Charisma", cha);


        long rowId = mSQLiteDatabase.insert("Character", null, contentValues);
        return rowId != -1;
    }

    public void deleteCharacter() {
        mSQLiteDatabase.delete("Character", null, null);
    }

    public List<Character> getCharacter() {

        String[] columns = {
                "CharacterName", "CharacterClass", "CharacterRace", "CharacterLevel", "Strength", "Dexterity", "Constitution", "Intelligence", "Wisdom", "Charisma"
        };

        Cursor c = mSQLiteDatabase.query(
                "Character",  // The table to query
                columns,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        c.moveToFirst();
        List<Character> list = new ArrayList<Character>();
        for (int i=0; i<c.getCount(); i++) {
            String name = c.getString(0);
            String classChar = c.getString(1);
            String race = c.getString(2);
            String level = c.getString(3);
            String str = c.getString(4);
            String dex = c.getString(5);
            String consti = c.getString(6);
            String intl = c.getString(7);
            String wis = c.getString(8);
            String cha = c.getString(9);
            Character character = new Character(name, classChar, race, level, str, dex, consti, intl, wis, cha);
            list.add(character);
            c.moveToNext();
        }
        return list;
    }





    class CharacterDBHelper extends SQLiteOpenHelper {

        private final String CREATE_CHARACTERS_SQL;

        private final String DROP_CHARACTERS_SQL;

        public CharacterDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            CREATE_CHARACTERS_SQL = context.getString(R.string.CREATE_CHARACTERS_SQL);
            DROP_CHARACTERS_SQL = context.getString(R.string.DROP_CHARACTERS_SQL);

        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL(CREATE_CHARACTERS_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL(DROP_CHARACTERS_SQL);
            onCreate(sqLiteDatabase);
        }
    }

}
