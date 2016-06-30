package com.voxalto.automotovoxalto;

import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.voxalto.automotovoxalto.adapter.SlidingMenuAdapter;
import com.voxalto.automotovoxalto.fragment.Fragment1;
import com.voxalto.automotovoxalto.fragment.Fragment2;
import com.voxalto.automotovoxalto.fragment.Fragment3;
import com.voxalto.automotovoxalto.model.ItemSildeMenu;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Hassnae on 11/05/2016.
 */

public class CarModelTabs extends AppCompatActivity implements MyDialogFragment.EditDialogListener,MyDialogFragment.EditDialogListenerScanByModel {

    Button AddNewTab;
    FragmentParent fragmentParent;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    String model_tabs;
    String make_tabs;
    String year_tabs;
    String vin_tabs;
    String engineOilType_tabs = "10W40";
    String engineCoolantType_tabs = "IAT";
    String brakeType_tabs = "DOT5";
    String powerSteeringType_tabs = "Mineral Oil XXX";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_model_tabs);
        AddNewTab = (Button) findViewById(R.id.add_tab);
        AddNewTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = MyDialogFragment.newInstance();
                newFragment.show(getFragmentManager(), "Dialog");
            }
        });
        getIDs();
        CreateTabForm();
        slidingMenu();
    }


    private void getIDs() {
        AddNewTab = (Button) findViewById(R.id.add_tab);
        fragmentParent = (FragmentParent) this.getSupportFragmentManager().findFragmentById(R.id.fragmentParent);
    }
       

    private void CreateTabForm() {
        //retreive the values
        vin_tabs = getIntent().getStringExtra("VIN");
        make_tabs = getIntent().getStringExtra("Make");
        model_tabs = getIntent().getStringExtra("Model");
        year_tabs = getIntent().getStringExtra("Year");
        engineOilType_tabs = getIntent().getStringExtra("EngineOilType");
        engineCoolantType_tabs = getIntent().getStringExtra("EngineCoolantType");
        brakeType_tabs = getIntent().getStringExtra("BrakeType");
        powerSteeringType_tabs = getIntent().getStringExtra("PowerSteeringType");

        Intent intent = new Intent(this, InfoCar.class);
        intent.putExtra("Model", model_tabs);
        intent.putExtra("Make", make_tabs);
        intent.putExtra("Year", year_tabs);
        intent.putExtra("EngineOilType", engineOilType_tabs);
        intent.putExtra("EngineCoolantType", engineCoolantType_tabs);
        intent.putExtra("BrakeType", brakeType_tabs);
        intent.putExtra("PowerSteeringType", powerSteeringType_tabs);
        intent.putExtra("VIN", vin_tabs);

            if (!model_tabs.equals("")) {
                fragmentParent.addPage(model_tabs);
        }
    }


        @Override
    public void updateResult(boolean ByVin,String vin_tabs,String model_tabs) {
        SharedPreferences sp = getSharedPreferences("KeyByVin", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("VIN", vin_tabs);
        ed.putString("ComingFrom", "Dialog");
        ed.putBoolean("ByVin", ByVin);
        ed.commit();

        if (!model_tabs.equals("")) {
            fragmentParent.addPage(model_tabs);
        }
                }


    public void updateResultByModel(boolean ByVin,String make_tabs,String model_tabs,String year_tabs, String engineOilType_tabs,String engineCoolantType_tabs,String brakeType_tabs,String powerSteeringType_tabs){
        SharedPreferences sp = getSharedPreferences("KeyByModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sp.edit();
        ed.putString("Model", model_tabs);
        ed.putString("Make", make_tabs);
        ed.putString("Year", year_tabs);
        ed.putString("engineOilType", engineOilType_tabs);
        ed.putString("engineCoolantType", engineCoolantType_tabs);
        ed.putString("brakeType", brakeType_tabs);
        ed.putString("powerSteeringType", powerSteeringType_tabs);
        ed.putBoolean("ByVin", ByVin);
        ed.putString("ComingFrom", "Dialog");
        ed.commit();

        if (!model_tabs.equals("")) {
            fragmentParent.addPage(model_tabs);
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
            transaction.replace(R.id.car_model_content, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

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
}

