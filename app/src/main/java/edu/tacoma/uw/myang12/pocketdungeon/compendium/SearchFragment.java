package edu.tacoma.uw.myang12.pocketdungeon.compendium;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;

import edu.tacoma.uw.myang12.pocketdungeon.R;
import edu.tacoma.uw.myang12.pocketdungeon.authenticate.LoginFragment;


public class SearchFragment extends Fragment {

    private JSONObject mSearchJSON;

    private static final String ARG_SEARCH_TYPE = "searchType";
    private static final String ARG_SEARCH_TERM = "searchTerm";
    private ScrollView scrollView;
    private LinearLayout linearLayout;
    private View view;

    private String mSearchType;
    private String mSearchTerm;

    public static SearchFragment newInstance(String type, String term) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SEARCH_TYPE, type);
        args.putString(ARG_SEARCH_TERM, term);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSearchType = getArguments().getString(ARG_SEARCH_TYPE);
            mSearchTerm = getArguments().getString(ARG_SEARCH_TERM);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /** Inflate the layout for this fragment.
         *  Get type and term from the user
         */

        StringBuilder url = new StringBuilder(getString(R.string.dnd_api));
        url.append(mSearchType);
        url.append('/');
        String query = mSearchTerm.replace(' ', '-');
        url.append(query);

        System.out.println(mSearchType);
        System.out.println(url.toString());
        if (mSearchType.equals("Spells")) {

            view = inflater.inflate(R.layout.fragment_spell_search, container, false);
            scrollView = (ScrollView) view.findViewById(R.id.search_scroll);
            linearLayout = (LinearLayout) view.findViewById(R.id.search_scroll_linear);

            new ApiTask().execute(url.toString());

        } else {
            view = inflater.inflate(R.layout.fragment_spell_search, container, false);

        }

        return view;
    }

    private class ApiTask extends AsyncTask<String, Void, JSONObject> {


        @Override
        protected JSONObject doInBackground(String... params) {
            String str = params[0];
            URLConnection urlConn = null;
            BufferedReader bufferedReader = null;
            try {
                URL url = new URL(str);
                urlConn = url.openConnection();

                bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));

                StringBuffer stringBuffer = new StringBuffer();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                return new JSONObject(stringBuffer.toString());

            } catch (Exception ex) {
                Log.e("App", "ApiTask", ex);
                return null;
            }
            finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }


        }

        @Override
        protected void onPostExecute(JSONObject result) {
            super.onPostExecute(result);
            if (result != null) {
                try {
                    showAllAttributes(result);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showAllAttributes(JSONObject jsonObject) throws JSONException {
        TextView textView;
        Iterator<String> keys = jsonObject.keys();
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        while (keys.hasNext()) {
            String key = keys.next();
            try {
                Object value = jsonObject.get(key);
                textView = new TextView(getContext());
                textView.setText(key + ": " + value.toString());
                linearLayout.addView(textView, lp);

            } catch (JSONException e) {

            }


        }

    }

}

