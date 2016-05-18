package com.voxalto.automotovoxalto;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by Hassnae on 11/05/2016.
 */

public class CarModelTabs extends TabActivity implements View.OnClickListener{
    TabHost host;
    Button btn_add_tab;
    private TextView tv_vin, tv_make, tv_model, tv_year, tv_engineOilType, tv_engineCoolantType, tv_brakeType, tv_powerSteeringType;
    String model_tabs;
    String make_tabs;
    String year_tabs;
    String vin_tabs;
    String engineOilType_tabs;
    String engineCoolantType_tabs;
    String brakeType_tabs;
    String powerSteeringType_tabs;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_model_tabs);
        String model_t = getIntent().getStringExtra("Model");
        View m_vForm;
        host = getTabHost();
        host.setup();
        m_vForm = _createTABForm(model_t);
    }

    private ViewGroup _createTABForm(String model) {
        //retreive the values

        vin_tabs = getIntent().getStringExtra("VIN");
        make_tabs = getIntent().getStringExtra("Make");
        model_tabs = getIntent().getStringExtra("Model");
        year_tabs = getIntent().getStringExtra("Year");
        engineOilType_tabs = getIntent().getStringExtra("EngineOilType");
        engineCoolantType_tabs = getIntent().getStringExtra("EngineCoolantType");
        brakeType_tabs = getIntent().getStringExtra("BrakeType");
        powerSteeringType_tabs = getIntent().getStringExtra("PowerSteeringType");

        //send the values to the next activity
        Intent intent = new Intent().setClass(this, CarInfoMnt.class);
        intent.putExtra("Model",model_tabs);
        intent.putExtra("Make", make_tabs);
        intent.putExtra("Year", year_tabs);
        intent.putExtra("EngineOilType", engineOilType_tabs);
        intent.putExtra("EngineCoolantType", engineCoolantType_tabs);
        intent.putExtra("BrakeType", brakeType_tabs);
        intent.putExtra("PowerSteeringType", powerSteeringType_tabs);
        intent.putExtra("VIN", vin_tabs);

        // create the tabs
        host = getTabHost();
        host.setup();
        TabHost.TabSpec spec = host.newTabSpec(model).setIndicator(model).setContent(intent);
        host.addTab(spec);
        return host;
    }

    public void addTab() {
        TabHost.TabSpec spec = host.newTabSpec("Tag2").setIndicator("");
        Intent intent = new Intent().setClass(this, AddCar.class);
        spec.setContent(intent);
        host.addTab(spec);
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent(CarModelTabs.this, AddCar.class);
        startActivity(i);
        addTab();
    }

}

