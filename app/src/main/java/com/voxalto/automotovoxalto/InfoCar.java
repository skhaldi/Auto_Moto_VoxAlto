package com.voxalto.automotovoxalto;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.Menu;
import android.view.View;

import android.widget.TabHost;


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


public class InfoCar extends Fragment{
    private String model;
    private String make;
    private String year;

    TabHost host;
    private TextView tv_vin, tv_make, tv_model, tv_year, tv_engineOilType, tv_engineCoolantType, tv_brakeType, tv_powerSteeringType;
    private String vin;

    private JsonTask task = new JsonTask();
    private JsonTask task2 = new JsonTask();

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.info_car);
//        model = getIntent().getStringExtra("Model");
//        make = getIntent().getStringExtra("Make");
//        year = getIntent().getStringExtra("Year");
//        vin = getIntent().getStringExtra("VIN");
        vin = getActivity().getIntent().getStringExtra("VIN");

        if(vin != null)
            task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vin + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy");

       // else
        //TO DO : need an API that returns the car information by model/make/year
        //task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vin + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy");


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.info_car, container, false);
    }

/*    public void addTab(){
        host = (TabHost)getView().findViewById(R.id.tabhost);
        host.setup();
        TabHost.TabSpec spec = host.newTabSpec(model);
        spec.setContent(R.id.InfoLayout);
        spec.setIndicator(model);
        host.addTab(spec);*/


//        spec.setContent(new TabHost.TabContentFactory() {
//        @Override
//            public View createTabContent(String tag) {
//                return new AnalogClock(InfoCar.this);
//            }
//        });

    //}


//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu, menu);
//        return true;
//            }


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
            tv_vin = (TextView) getView().findViewById(R.id.V_vin);
            tv_make = (TextView) getView().findViewById(R.id.V_make);
            tv_model = (TextView) getView().findViewById(R.id.V_model);
            tv_year = (TextView)getView().findViewById(R.id.V_year);

            tv_engineOilType = (TextView)getView().findViewById(R.id.engineOilType);
            tv_engineCoolantType = (TextView)getView().findViewById(R.id.engineCoolantType);
            tv_brakeType = (TextView)getView().findViewById(R.id.brakeType);
            tv_powerSteeringType = (TextView)getView().findViewById(R.id.powerSteeringType);

            JSONObject parentObject = null;
            try {
                String makeName = "Cheverolet";
                String modelName = "Cheverolet";
                String year = "2012";
                String style_id = "";
                String vin = getActivity().getIntent().getStringExtra("VIN");;
                if (result != null) {
                    parentObject = new JSONObject(result);
                    if (parentObject != null) {
                        if (parentObject.optJSONObject("make") != null) {
                            JSONObject makeObject = parentObject.getJSONObject("make");
                            if (makeObject != null) {
                                makeName = makeObject.getString("name") ;
                            }
                            JSONObject modelObject = parentObject.getJSONObject("model");
                            if (modelObject != null) {
                                modelName = modelObject.getString("name");
                            }
                            vin = parentObject.getString("vin");
                            JSONArray yearArray = parentObject.getJSONArray("years");
                            JSONObject yearObject = null;
                            if (yearArray != null) {
                                yearObject = yearArray.getJSONObject(0);
                                if (yearObject != null) {
                                    year = yearObject.getString("year");
                                    JSONArray stylesArray = yearObject.getJSONArray("styles");
                                    JSONObject stylesObject = stylesArray.getJSONObject(0);
                                    style_id = stylesObject.getString("id");
                                }
                            }
                            tv_make.setText(makeName);
                            tv_model.setText(modelName);
                            model = modelName;
                            tv_year.setText(year);
                            tv_vin.setText(vin);
                        }
                        if (parentObject != null) {
                            if (parentObject.optJSONArray("engines") != null) {
                                JSONArray enginesObject = parentObject.getJSONArray("engines");
                                JSONObject engines  = enginesObject.getJSONObject(0);
                                String type = engines.getString("type");
                                tv_engineOilType.setText("10W40");
                                tv_engineCoolantType.setText("IAT");
                                tv_brakeType.setText("DOT5");
                                tv_powerSteeringType.setText("Mineral Oil XXX");
                            }
                        }
                    }
                } else {
                    tv_make.setText(makeName);
                    tv_model.setText(modelName);
                    tv_year.setText(year);
                    tv_vin.setText(vin);
                    tv_engineOilType.setText("10W40");
                    tv_engineCoolantType.setText("IAT");
                    tv_brakeType.setText("DOT5");
                    tv_powerSteeringType.setText("Mineral Oil XXX");
                }
                if (style_id != "") {
                    if (task.getStatus().toString() == "RUNNING") {
                        task.cancel(true);
                    }
                    task2.execute("https://api.edmunds.com/api/vehicle/v2/styles/" + style_id + "/engines?availability=standard&fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //addTab();
        }
    }
}
