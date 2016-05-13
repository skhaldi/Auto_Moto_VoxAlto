package com.voxalto.automotovoxalto;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.voxalto.automotovoxalto.adapter.SlidingMenuAdapter;
import com.voxalto.automotovoxalto.fragment.Fragment1;
import com.voxalto.automotovoxalto.fragment.Fragment2;
import com.voxalto.automotovoxalto.fragment.Fragment3;
import com.voxalto.automotovoxalto.model.ItemSildeMenu;

import java.util.ArrayList;
import java.util.List;

public class AddCar extends AppCompatActivity {


    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car);
        slidingMenu();
    }

    public void OnClickButtonScan(View v){
        if(v.getId() == R.id.Bscan){

            EditText vin = (EditText)findViewById(R.id.TFvin);
            String str_vin = vin.getText().toString();

            Intent i = new Intent(AddCar.this, CarModelTabs.class);
            i.putExtra("VIN",str_vin);
            startActivity(i);
        }
    }

    public void OnClickButtonCancel(View v){
        if(v.getId() == R.id.cancel){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    public void OnClickButtonAddCar(View v){
        if(v.getId() == R.id.Bscan2){

            EditText model = (EditText)findViewById(R.id.TFmodel);
            EditText make = (EditText)findViewById(R.id.TFmake);
            EditText year = (EditText)findViewById(R.id.TFyear);

            String str_model = model.getText().toString();
            String str_make = make.getText().toString();
            String str_year = year.getText().toString();

            Intent i = new Intent(this, InfoCar.class);
            i.putExtra("Model",str_model);
            i.putExtra("Make",str_make);
            i.putExtra("Year",str_year);
            startActivity(i);
        }
    }

    public void slidingMenu() {

        final String appName = getString(R.string.app_name);
        List<ItemSildeMenu> listSliding = new ArrayList<>();
        SlidingMenuAdapter adapter = new SlidingMenuAdapter(this, listSliding);
        final ListView listViewSliding = (ListView) findViewById(R.id.lv_sliding_menu);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //Add item for sliding list
        listSliding.add(new ItemSildeMenu(R.drawable.ic_action_settings, "Settings"));
        listSliding.add(new ItemSildeMenu(R.drawable.ic_action_about, "About"));
        listSliding.add(new ItemSildeMenu(R.mipmap.ic_launcher, "Android"));

        listViewSliding.setAdapter(adapter);

        //Set title
        setTitle(appName);
        //Item selected
        listViewSliding.setItemChecked(0, true);
        //Display icon to open/close sliding list
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //Close menu
        drawerLayout.closeDrawer(listViewSliding);
        //Display fragment 1 when start
        replaceFragment(0);

        //Handle on item click
        listViewSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                   @Override
                                                   public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                       //Set title
                                                       setTitle(appName);
                                                       //Item selected
                                                       listViewSliding.setItemChecked(position, true);
                                                       //Display fragment 1 when start
                                                       replaceFragment(position);
                                                       //Close menu
                                                       drawerLayout.closeDrawer(listViewSliding);
                                                   }
                                               }
        );

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    private void replaceFragment(int pos) {
        Fragment fragment = null;
        switch (pos) {
            case 0:
                fragment = new Fragment1();
                break;
            case 1:
                fragment = new Fragment2();
                break;
            case 2:
                fragment = new Fragment3();
                break;
            default:
                fragment = new Fragment1();
                break;
        }

        if (fragment != null ) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.add_car_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }
}
