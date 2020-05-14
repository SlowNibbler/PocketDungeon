package edu.tacoma.uw.myang12.pocketdungeon.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Campaign implements Serializable {
    private int campaignID;
    private String campaignName;
    private String getCampaignNotes;

    public static final String ID = "campaignid";
    public static final String NAME = "campaignname";
    public static final String NOTES = "campaignnotes";

    public Campaign(int campaignID, String campaignName, String getCampaignNotes) {
        this.campaignID = campaignID;
        this.campaignName = campaignName;
        this.getCampaignNotes = getCampaignNotes;
    }

    public int getCampaignID() { return campaignID; }

    public void setCampaignID(int campaignID) { this.campaignID = campaignID; }

    public String getCampaignName() {
        return campaignName;
    }

    public void setCampaignName(String campaignName) {
        this.campaignName = campaignName;
    }

    public String getGetCampaignNotes() {
        return getCampaignNotes;
    }

    public void setGetCampaignNotes(String getCampaignNotes) {
        this.getCampaignNotes = getCampaignNotes;
    }

    public static List<Campaign> parseCampaignJson(String campaignJson) throws JSONException {
        List<Campaign> campaignList = new ArrayList<>();
        if (campaignJson != null) {
            JSONArray arr = new JSONArray(campaignJson);
            for(int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                Campaign campaign = new Campaign(obj.getInt(Campaign.ID),
                        obj.getString(Campaign.NAME),
                        obj.getString(Campaign.NOTES));
                campaignList.add(campaign);
            }
        }
        return campaignList;
    }
}