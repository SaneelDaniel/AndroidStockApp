package edu.sjsu.android.StockSearch;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link stockHistory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class stockHistory extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RequestQueue requestQueue;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public stockHistory() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment stockHistory.
     */
    // TODO: Rename and change types and number of parameters
    public static stockHistory newInstance(String param1, String param2) {
        stockHistory fragment = new stockHistory();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stock_history, container, false);
        JSONObject obj = activity_StockDetail.json_object;
        requestQueue = Volley.newRequestQueue(getContext());
        System.out.println("On Create: Desc Fragment: ");
        final String[] name = {""};
        final String[] ticker = {""};
        final String[] description = {""};
        final String[] startDate = {""};
        final String[] exhangeCode = {""};
        final String[] date = {""};
        final String[] high = {""};
        final String[] open = {""};
        final String[] low = {""};
        final String[] close = {""};
        final String[] volume = {""};


        final String[] jsonURL = {""};
        TextView cmpNameTxtView =  view.findViewById(R.id.textView_HistoryFragment_addName);
        TextView tickerTxtView =  view.findViewById(R.id.textView_HistoryFragment_addticker);
        TextView descriptionTextView =  view.findViewById(R.id.textView_HistoryFragment_addDescription);
        TextView startDateTxtView =  view.findViewById(R.id.textView_HistoryFragment_addStartDate);
        TextView exchangeCodeTxtView = view.findViewById(R.id.textView_HistoryFragment_addExchangeCode);

        try{
            name[0] = obj.getString("name");
            ticker[0] = obj.getString("ticker");

            if (ticker[0].length()>0){
                jsonURL[0] = "https://api.tiingo.com/tiingo/daily/"+ticker[0]+"?token="+MainActivity.TIINGO_API_TOKEN;

                obj = new JSONObject();
                System.out.println("JSONURL DESC: "+ jsonURL[0]);
                try{
                    String url = jsonURL[0];
                    JSONObject tmpObj = new JSONObject();

                    JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    System.out.println("Ticker Sent: "+ ticker[0] + "New URL Json Response: "+ response.toString());

                                    try {
                                        System.out.println("hello" + response.toString());
                                        //tmpObj = response;


                                        name[0] = response.getString("name");
                                        ticker[0] = response.getString("ticker");
                                        description[0] = response.getString("description");
                                        startDate[0] =  response.getString("startDate");
                                        exhangeCode[0] = response.getString("exchangeCode");
                                        cmpNameTxtView.setText(name[0]);
                                        tickerTxtView.setText(ticker[0]);

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }
                                    if(description[0]!=null){
                                        descriptionTextView.setText(description[0]);
                                    }
                                    else {
                                        descriptionTextView.setText(" - ");
                                    }


                                    if(startDate[0]!= null){
                                        startDateTxtView.setText(startDate[0]);

                                    }
                                    else {
                                        startDateTxtView.setText(" - ");
                                    }

                                    if (exhangeCode[0]!=null){
                                        exchangeCodeTxtView.setText(exhangeCode[0]);
                                    }
                                    else {
                                        exchangeCodeTxtView.setText(" - ");
                                    }


                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();

                        }
                    })
                    {    //this is the part, that adds the header to the request
                        @Override
                        public Map<String, String> getHeaders() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("content-type", "application/json");
                            return params;
                        }
                    };
                    requestQueue.add(jsonArrayRequest);
                }
                catch (Exception e){
                    e.printStackTrace();
                }


            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }
}