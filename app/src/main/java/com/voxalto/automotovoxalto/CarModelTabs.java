package com.voxalto.automotovoxalto;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TabHost;

/**
 * Created by Hassnae on 11/05/2016.
 */

public class CarModelTabs extends TabActivity implements View.OnClickListener{

    TabHost host;
    private String model;
    Button btn_add_tab;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_model_tabs);

        View m_vForm;
        host = getTabHost();
        host.setup();
        m_vForm = _createTABForm();
    }

    private ViewGroup _createTABForm() {
        // create the tabs
        Intent intent = new Intent().setClass(this, CarInfoMnt.class);
        TabHost.TabSpec spec = host.newTabSpec("camaro").setIndicator("Camaro").setContent(intent);
        host.addTab(spec);
        return host;
    }

    public void addTab() {
        TabHost.TabSpec spec1 = host.newTabSpec("Camaro");
        Intent intent = new Intent().setClass(this, CarInfoMnt.class);
        spec1 = host.newTabSpec("cheverolet").setIndicator("cheverolet").setContent(intent);
        host.addTab(spec1);
    }


    @Override
    public void onClick(View v) {
        Intent i = new Intent(CarModelTabs.this, AddCar.class);
        startActivity(i);
        addTab();
    }

}

