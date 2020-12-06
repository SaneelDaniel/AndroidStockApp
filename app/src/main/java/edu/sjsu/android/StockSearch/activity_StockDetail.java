package edu.sjsu.android.StockSearch;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONException;
import org.json.JSONObject;

public class activity_StockDetail extends AppCompatActivity {

    static JSONObject json_object = new JSONObject();
    static StockInfo currentStock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_detail);
        Intent intent = getIntent();
        String JSON_String = intent.getStringExtra("JSON_String");
        try {
            json_object = new JSONObject(getIntent().getStringExtra("JSON_String"));
            currentStock = new StockInfo(json_object.getString("ticker"), json_object.getString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        MainActivity.getFavoriteList().add(currentStock);



        Toolbar toolBar = findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        // Set up the ViewPager with the sections adapter.
        ViewPager mViewPager = (ViewPager) findViewById(R.id.container);
        FragmentPageAdapter pa = new FragmentPageAdapter(getSupportFragmentManager());
        pa.addFragment(new stockNews(), "Info");
        pa.addFragment(new stockHistory(),"History");

        mViewPager.setAdapter(pa);
        mViewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(mViewPager);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        String name = "";
        try{
            name = json_object.getString("name");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        getSupportActionBar().setTitle(name);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.settings_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.settings_bar) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



}