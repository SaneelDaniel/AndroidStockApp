package edu.sjsu.android.StockSearch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private AutoCompleteTextView autoCompleteTextView;
    private RecyclerView favoritesRecyclerView;
    private SharedPreferences sharedPreferences;
    private FavoriteAdapter favadapter;
    private Button clearButton;
    private Button getQuoteButton;
    private Switch autoRefreshSwitch;
    public List<String> autoSuggestedTickerList = new ArrayList<>();
    TextView favoriteChangeRate;
    FavoriteAdapter favoriteAdapter;
    public static ArrayList<StockInfo> favoriteList = new ArrayList<>();

    private ArrayList<String> favList = new ArrayList<>();
    private Context context;
    private ImageButton refreshButton;
    public static String TIINGO_API_TOKEN = "71049819e280bef97c674e9bcf216cb0b9b341ff";
    RequestQueue requestQueue = null;

    public static ArrayList<StockInfo> getFavoriteList() {
        return favoriteList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Stock News");
        favoritesRecyclerView = (RecyclerView)findViewById(R.id.main_recycler_list_view);
        clearButton = (Button) findViewById(R.id.main_clear_button);

        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sharedPreferences = getSharedPreferences("favorite", Context.MODE_PRIVATE);
        favoriteChangeRate = (TextView) findViewById(R.id.favoriteChangeRate);
        refreshButton = (ImageButton) findViewById(R.id.refreshButton);
        context = this;
        autoRefreshSwitch = (Switch) findViewById(R.id.autoRefresh_Switch);
        getQuoteButton = (Button) findViewById(R.id.main_getQuote_button);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        autoCompleteTextView = (AutoCompleteTextView)findViewById(R.id.main_search_autoCompleteTextView);
        autoCompleteTextView.setThreshold(3);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
                String selection = (String)parent.getItemAtPosition(position);
                selection  = selection.split("\n")[0];
                autoCompleteTextView.setText(selection);
            }
        });

        autoCompleteTextView.addTextChangedListener(new TextWatcher(){

            public void afterTextChanged(Editable editable) {
                // TODO Auto-generated method stub

            }



            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String newText = s.toString();
                if(newText.length()>2) {
                    AsyncAutoCompleteTask autoCompleteTask = new AsyncAutoCompleteTask();
                    autoSuggestedTickerList = autoCompleteTask.doInBackground(newText);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (getApplicationContext(), android.R.layout.select_dialog_item, autoSuggestedTickerList);
                autoCompleteTextView.setAdapter(adapter);
                //new getJsonAutoComplete().executethis(newText);
            }

        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoCompleteTextView.setText("");
            }
        });

        getQuoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String validateMe = autoCompleteTextView.getText().toString();
                if(validateMe.length()>0) {
                    AsyncAutoCompleteTextValidationTask validationTask = new AsyncAutoCompleteTextValidationTask();
                    boolean validated = validationTask.doInBackground(validateMe).booleanValue();
                    if(validated){
                        System.out.println("Validation Result: " + validated);
                    }
                }
                else{
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                    builder1.setMessage("Please enter a Valid Name/Ticker");
                    builder1.setCancelable(true);
                    builder1.setNegativeButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        });

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshFavoriteInfo();
            }
        });
        favadapter = new FavoriteAdapter(context, favoriteList);
        favoritesRecyclerView.setAdapter(favoriteAdapter);
    }

    //method to refresh the favorite list stock info
    private void refreshFavoriteInfo() {
    }

    public void removeFavorite(String favTicker) {
    }

    /**
     * inner Async Task Class to handle the AutoCompleteText API Calls
     */
    private  class AsyncAutoCompleteTask extends AsyncTask<String , Void , ArrayList<String>>{
        ArrayList<String> tickerList = new ArrayList<String>();
        @Override
        protected ArrayList<String> doInBackground(String... strings) {
            String JsonURL = "https://api.tiingo.com/tiingo/utilities/search?query="+strings[0]+"&token="+TIINGO_API_TOKEN;
            System.out.println("Auto Complete String Being Passed: "+ strings[0].toString());

            try{
                String url = JsonURL;
                JSONObject jObj = new JSONObject();
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                String companyName = "";
                                String ticker = "";
                                final String[] suggested = new String[]{};
                                try{
                                    for (int i = 0; i<response.length(); i++){
                                        JSONObject obj = response.getJSONObject(i);
                                        companyName = obj.optString("name").toString();
                                        ticker = obj.optString("ticker").toString();
                                        String autoSuggested = companyName + ",\n" + ticker;
                                        tickerList.add(autoSuggested);
                                        //System.out.println("Company name + Ticker: "+ companyName + " + " + ticker);
                                    }
                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                //autoCompleteTextView.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, tickerList));
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //handle the error
                        Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();

                    }
                }) {    //this is the part, that adds the header to the request
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("content-type", "application/json");
                        return params;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            }
            catch (Exception e){
            }

            System.out.println();
            return tickerList;
        }
    }


    /**
     * validation class Async Task
     */
    private class AsyncAutoCompleteTextValidationTask extends AsyncTask<String, Void, Boolean> {
        boolean validated = false;
        @Override
        protected Boolean doInBackground(String... strings) {
            String JsonURL = "https://api.tiingo.com/tiingo/utilities/search?query="+strings[0]+"&token="+TIINGO_API_TOKEN;

            try{
                String url = JsonURL;
                JSONObject jObj = new JSONObject();
                JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                String companyName = "";
                                String ticker = "";
                                final String[] suggested = new String[]{};
                                JSONObject obj = new JSONObject();
                                try{
                                    for (int i = 0; i<response.length(); i++){
                                        obj = response.getJSONObject(i);
                                        companyName = obj.optString("name").toString();
                                        ticker = obj.optString("ticker").toString();
                                        if(companyName.equals(strings[0]) || ticker.equals(strings[0])){
                                            break;
                                        }
                                    }

                                    if(companyName!=""){
                                        String newJsonUrl = "https://api.tiingo.com/tiingo/utilities/search?query="+strings[0]+"&token="+TIINGO_API_TOKEN;

                                        validated = true;
                                        Intent intent = new Intent(getApplicationContext(), activity_StockDetail.class);
                                        String jsonString = obj.toString();
                                        intent.putExtra("JSON_String", jsonString);
                                        System.out.println("JOBj String: "+ jsonString);
                                        startActivity(intent);
                                    }
                                    else{
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                                        builder1.setMessage("Invalid Name/Symbol");
                                        builder1.setCancelable(true);
                                        builder1.setNegativeButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();

                                    }

                                }
                                catch (Exception e){
                                    e.printStackTrace();
                                }
                                //autoCompleteTextView.setAdapter(autoSuggestedTickerList);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //handle the error
                        Toast.makeText(MainActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();

                    }
                }) {    //this is the part, that adds the header to the request
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("content-type", "application/json");
                        return params;
                    }
                };
                requestQueue.add(jsonObjectRequest);
            }
            catch (Exception e){
            }
            return validated;
        }
    }
}