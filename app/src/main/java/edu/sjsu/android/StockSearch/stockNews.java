package edu.sjsu.android.StockSearch;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link stockNews#newInstance} factory method to
 * create an instance of this fragment.
 */
public class stockNews extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RequestQueue requestQueue;
    ImageButton favoriteButton;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public stockNews() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment stockNews.
     */
    // TODO: Rename and change types and number of parameters
    public static stockNews newInstance(String param1, String param2) {
        stockNews fragment = new stockNews();
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
        View view = inflater.inflate(R.layout.fragment_stock_current_info, container, false);
        favoriteButton = view.findViewById(R.id.imageButton);

        JSONObject obj = activity_StockDetail.json_object;
        requestQueue = Volley.newRequestQueue(getContext());
        String name = "";
        String ticker = "";
        Timestamp timeStamp = null;
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                "favorite", Context.MODE_PRIVATE);

        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                if(!activity_StockDetail.currentStock.favorite) {
                    editor.putString(activity_StockDetail.currentStock.ticker, activity_StockDetail.currentStock.price);
                   // favoriteButton.setImageResource(R.drawable.filled);
                } else {
                    editor.remove(activity_StockDetail.currentStock.price);
                    //favorite.setImageResource(R.drawable.star);
                }
                editor.apply();
            }});


        final String[] last = {""};
        final String[] prevClosed = {""};
        final String[] open = {""};
        final String[] high = {""};
        final String[] low = {""};
        final String[] mid = {""};
        final String[] volume = {""};
        final String[] bidSize = {""};
        final String[] bidPrice = {""};
        final String[] askSize = {""};
        final String[] askPrice = {""};

        String jsonURL = "";
        TextView cmpNameTxtView =  view.findViewById(R.id.textView_addCompanyName);
        TextView tickerTxtView =  view.findViewById(R.id.textView_addTicker);
        TextView lastTxtView =  view.findViewById(R.id.textView_addLastPrice);
        TextView prevcloseTxtView =  view.findViewById(R.id.textView_addPrevClose);
        TextView openTxtView = view.findViewById(R.id.textView1_addOpen);
        TextView highTxtView =  view.findViewById(R.id.textView_addHigh);
        TextView lowTxtView =  view.findViewById(R.id.textView_addLow);
        TextView midTxtView =  view.findViewById(R.id.textView_addmid);
        TextView volumeTxtView =  view.findViewById(R.id.textView_addVolume);
        TextView bidSizeTxtView =  view.findViewById(R.id.textView_addBidSize);
        TextView bidPriceTxtView =  view.findViewById(R.id.textView_addBidPrice);
        TextView askSizeTxtView =  view.findViewById(R.id.textView_addAskSize);
        TextView askPriceTxtView =  view.findViewById(R.id.textView_addAskPrice);

        try{
            name = obj.getString("name");
            ticker = obj.getString("ticker");

            if (ticker.length()>0){
                jsonURL = "https://api.tiingo.com/iex/?tickers="+ticker+"&token="+MainActivity.TIINGO_API_TOKEN;

                obj = new JSONObject();
                String textVieBuilderString = "";

                try{
                    String url = jsonURL;
                    final JSONObject[] tmpObj = {new JSONObject()};
                    String finalName = name;
                    String finalTicker = ticker;
                    textVieBuilderString = "Company Name: " + finalName +"\nTicker: " + finalTicker + "\n";

                    String finalTextVieBuilderString = textVieBuilderString;
                    String finalTicker1 = ticker;
                    String finalName1 = name;
                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                            new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    try {
                                        tmpObj[0] = response.getJSONObject(0);


                                        last[0] = tmpObj[0].getString("last");
                                        prevClosed[0] = tmpObj[0].getString("prevClose");
                                        open[0] = tmpObj[0].getString("open");
                                        high[0] =  tmpObj[0].getString("high");
                                        low[0] = tmpObj[0].getString("low");
                                        mid[0] = tmpObj[0].getString("mid");
                                        volume[0] = tmpObj[0].getString("volume");
                                        bidSize[0] = tmpObj[0].getString("bidSize");
                                        bidPrice[0] = tmpObj[0].getString("bidPrice");
                                        askSize[0] = tmpObj[0].getString("askSize");
                                        askPrice[0] = tmpObj[0].getString("askPrice");

                                        cmpNameTxtView.setText(finalName1);
                                        tickerTxtView.setText(finalTicker1);

                                    }
                                    catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    if(last[0]!=null){
                                        lastTxtView.setText(last[0]);
                                    }
                                    else {
                                        lastTxtView.setText(" - ");
                                    }

                                    if(prevClosed[0]!=null){
                                        prevcloseTxtView.setText(prevClosed[0]);
                                    }
                                    else{
                                        prevcloseTxtView.setText(" - ");
                                    }

                                    if(open[0]!= null){
                                        openTxtView.setText(open[0]);
                                    }
                                    else {
                                        openTxtView.setText(" - ");
                                    }

                                    if(high[0]!= null){
                                        highTxtView.setText(high[0]);
                                    }
                                    else {
                                        highTxtView.setText(" - ");
                                    }

                                    if (low[0]!=null){
                                        lowTxtView.setText(low[0]);
                                    }
                                    else {
                                        lowTxtView.setText(" - ");
                                    }

                                    if(mid[0]!= null){
                                        midTxtView.setText(mid[0]);
                                    }
                                    else {
                                        midTxtView.setText(" - ");
                                    }

                                    if (volume[0]!=null){
                                        volumeTxtView.setText(volume[0]);
                                    }
                                    else {
                                        volumeTxtView.setText(" - ");
                                    }

                                    if(bidSize[0]!= null){
                                        bidSizeTxtView.setText(bidSize[0]);
                                    }
                                    else {
                                        bidSizeTxtView.setText(" - ");
                                    }

                                    if(bidPrice[0]!=null){
                                        bidPriceTxtView.setText(bidPrice[0]);
                                    }
                                    else {
                                        bidPriceTxtView.setText(" - ");
                                    }

                                    if (askSize[0]!=null){
                                        askSizeTxtView.setText(askSize[0]);
                                    }
                                    else{
                                        askSizeTxtView.setText(" - ");
                                    }

                                    if (askPrice[0]!=null){
                                        askPriceTxtView.setText(askPrice[0]);
                                    }
                                    else {
                                        askPriceTxtView.setText(" - ");
                                    }

                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {    //this is the part, that adds the header to the request
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