package com.voxalto.automotovoxalto;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

/**
 * Created by Hassnae on 11/05/2016.
 */

public class CarModelTabs extends AppCompatActivity {

    Button AddNewTab;
    FragmentParent fragmentParent;

    String model_tabs;
    String make_tabs;
    String year_tabs;
    String vin_tabs;
    String engineOilType_tabs = "10W40";
    String engineCoolantType_tabs = "IAT";
    String brakeType_tabs = "DOT5";
    String powerSteeringType_tabs = "Mineral Oil XXX";
    String output;


    private JsonTask task;
    private JsonTask task2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_model_tabs);
        AddNewTab = (Button) findViewById(R.id.add_tab);
        AddNewTab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showPopup();
            }
        });
        getIDs();
        CreateTabForm();
    }

    private PopupWindow pw;

    private void showPopup() {
        try {
            LayoutInflater inflater = (LayoutInflater) getBaseContext().
            getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.pop_up, (ViewGroup)findViewById(R.id.popup_1));
            pw = new PopupWindow(layout, 750, 750, true);
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);
            pw.setFocusable(true);
            pw.update();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
       
    public void OnClickButtonScanVinPopUp(View v) throws ExecutionException, InterruptedException, JSONException {
        if(v.getId() == R.id.ScanVinPopUp){
            View contentView = pw.getContentView();
            EditText vin = (EditText) contentView.findViewById(R.id.TFvinPopUp);
            vin_tabs = vin.getText().toString();

            task = new JsonTask();
            output = task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vin_tabs + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy").get();

            JSONObject obj = new JSONObject(output);
            model_tabs = obj.getJSONObject("model").getString("name");
            make_tabs = obj.getJSONObject("make").getString("name");
            year_tabs = obj.getJSONArray("years").getJSONObject(0).getString("year");
            engineOilType_tabs = "10W40";
            engineCoolantType_tabs = "IAT";
            brakeType_tabs = "DOT5";
            powerSteeringType_tabs = "Mineral Oil XXX";
            SendData(vin_tabs, make_tabs, model_tabs, year_tabs, engineOilType_tabs, engineCoolantType_tabs, brakeType_tabs, powerSteeringType_tabs);

            if (!model_tabs.equals("")) {
                fragmentParent.addPage(model_tabs);
                pw.dismiss();
            }
        }
    }


    public class JsonTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            try {
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                return finalJson;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            JSONObject parentObject = null;
            try {
                String style_id = "";
                String vin = getIntent().getStringExtra("VIN");
                if (result != null) {
                    parentObject = new JSONObject(result);
                    if (parentObject != null) {
                        if (parentObject.optJSONObject("make") != null) {
                            JSONObject makeObject = parentObject.getJSONObject("make");
                            if (makeObject != null) {
                                make_tabs = makeObject.getString("name");
                            }
                            JSONObject modelObject = parentObject.getJSONObject("model");
                            if (modelObject != null) {
                                model_tabs = modelObject.getString("name");
                            }
                            vin = parentObject.getString("vin");
                            JSONArray yearArray = parentObject.getJSONArray("years");
                            JSONObject yearObject = null;
                            if (yearArray != null) {
                                yearObject = yearArray.getJSONObject(0);
                                if (yearObject != null) {
                                    year_tabs = yearObject.getString("year");
                                    JSONArray stylesArray = yearObject.getJSONArray("styles");
                                    JSONObject stylesObject = stylesArray.getJSONObject(0);
                                    style_id = stylesObject.getString("id");
                                }
                            }
                        }
                        if (parentObject != null) {
                            if (parentObject.optJSONArray("engines") != null) {
                                JSONArray enginesObject = parentObject.getJSONArray("engines");
                                JSONObject engines  = enginesObject.getJSONObject(0);
                                String type = engines.getString("type");
                            }
                        }
                    }
                }
                if (style_id != "") {
                    if (task.getStatus().toString() == "RUNNING") {
                        task.cancel(true);
                    }
                    task2 = new JsonTask();
                    task2.execute("https://api.edmunds.com/api/vehicle/v2/styles/" + style_id + "/engines?availability=standard&fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void OnClickButtonCancelVinPopUp(View v){
        if(v.getId() == R.id.CancelVinPopUp){
            pw.dismiss();
        }
        }

    public void OnClickButtonCancelModelPopUp(View v){
        if(v.getId() == R.id.CancelModelPopUp){
            pw.dismiss();
        }
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

        if (!model_tabs.equals("")) {
            fragmentParent.addPage(model_tabs);
        }
    }


    private void SendData(String vin_tabs,String make_tabs,String model_tabs,String year_tabs, String engineOilType_tabs,String engineCoolantType_tabs,String brakeType_tabs,String powerSteeringType_tabs ) {
        //send the values to the next activity
        Intent intent = new Intent().setClass(this, CarInfoMnt.class);
        intent.putExtra("Model", model_tabs);
        intent.putExtra("Make", make_tabs);
        intent.putExtra("Year", year_tabs);
        intent.putExtra("EngineOilType", engineOilType_tabs);
        intent.putExtra("EngineCoolantType", engineCoolantType_tabs);
        intent.putExtra("BrakeType", brakeType_tabs);
        intent.putExtra("PowerSteeringType", powerSteeringType_tabs);
        intent.putExtra("VIN", vin_tabs);
    }
}

