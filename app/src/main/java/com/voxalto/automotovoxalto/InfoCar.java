package com.voxalto.automotovoxalto;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TableRow;
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
import java.util.ArrayList;
import java.util.List;


public class InfoCar extends Fragment{
    private String model, make, year, vinEntered, engineOilType, engineCoolantType, brakeType, powerSteeringType;
    TabHost host;
    private TextView tv_vin, tv_make, tv_model, tv_year, tv_engineOilType, tv_engineCoolantType, tv_brakeType, tv_powerSteeringType;
    private JsonTask task = new JsonTask();
    private DatabaseHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DatabaseHelper(getActivity());
        vinEntered = getActivity().getIntent().getStringExtra("VIN");


        if (vinEntered != null) {
            task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vinEntered + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.info_car, container, false);
        vinEntered = getActivity().getIntent().getStringExtra("VIN");
        make = getActivity().getIntent().getStringExtra("Make");
        model = getActivity().getIntent().getStringExtra("Model");
        year = getActivity().getIntent().getStringExtra("Year");
        engineOilType = getActivity().getIntent().getStringExtra("EngineOilType");
        engineCoolantType = getActivity().getIntent().getStringExtra("EngineCoolantType");
        brakeType = getActivity().getIntent().getStringExtra("BrakeType");
        powerSteeringType = getActivity().getIntent().getStringExtra("PowerSteeringType");

        TableRow tableVin = (TableRow) v.findViewById(R.id.vin_table);

        tv_make = (TextView) v.findViewById(R.id.V_make);
        tv_model = (TextView) v.findViewById(R.id.V_model);
        tv_year = (TextView) v.findViewById(R.id.V_year);
        tv_engineOilType = (TextView) v.findViewById(R.id.engineOilType);
        tv_engineCoolantType = (TextView) v.findViewById(R.id.engineCoolantType);
        tv_brakeType = (TextView) v.findViewById(R.id.brakeType);
        tv_powerSteeringType = (TextView) v.findViewById(R.id.powerSteeringType);

        if (vinEntered == null) {
            tableVin.setVisibility(tableVin.GONE);
            tv_make.setText(make);
            tv_model.setText(model);
            tv_year.setText(year);
            tv_engineOilType.setText(engineOilType);
            tv_engineCoolantType.setText(engineCoolantType);
            tv_brakeType.setText(brakeType);
            tv_powerSteeringType.setText(powerSteeringType);
        }
        return v;
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
            tv_vin = (TextView) getView().findViewById(R.id.V_vin);
            tv_make = (TextView) getView().findViewById(R.id.V_make);
            tv_model = (TextView) getView().findViewById(R.id.V_model);
            tv_year = (TextView)getView().findViewById(R.id.V_year);

            tv_engineOilType = (TextView)getView().findViewById(R.id.engineOilType);
            tv_engineCoolantType = (TextView)getView().findViewById(R.id.engineCoolantType);
            tv_brakeType = (TextView)getView().findViewById(R.id.brakeType);
            tv_powerSteeringType = (TextView)getView().findViewById(R.id.powerSteeringType);

            JSONObject parentObject = null;
            Car car = new Car();
            try {
                String makeName = "";
                String modelName = "";
                String yearName = "";
                String style_id = "";
                String vin = "";
                make = getActivity().getIntent().getStringExtra("Make");
                model = getActivity().getIntent().getStringExtra("Model");
                year = getActivity().getIntent().getStringExtra("Year");

                List<String> vins = new ArrayList<String>();
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
                            if ( parentObject.has("vin") ){
                            vin = parentObject.getString("vin");
                            } else {
                                vin = vinEntered.toUpperCase();
                            }
                            JSONArray yearArray = parentObject.getJSONArray("years");
                            JSONObject yearObject = null;
                            if (yearArray != null) {
                                yearObject = yearArray.getJSONObject(0);
                                if (yearObject != null) {
                                    yearName = yearObject.getString("year");
                                    JSONArray stylesArray = yearObject.getJSONArray("styles");
                                    JSONObject stylesObject = stylesArray.getJSONObject(0);
                                    style_id = stylesObject.getString("id");
                                }
                            }
                            tv_vin.setText(vin);
                            tv_make.setText(makeName);
                            tv_model.setText(modelName);
                            tv_year.setText(yearName);
                            tv_engineOilType.setText(engineOilType);
                            tv_engineCoolantType.setText(engineCoolantType);
                            tv_brakeType.setText(brakeType);
                            tv_powerSteeringType.setText(powerSteeringType);

                            vins = helper.getAllVins();
                        }
                        int yearInteger = Integer.parseInt(yearName);

                        car.setMake(makeName);
                        car.setModel(modelName);
                        car.setYear(yearInteger);
                        car.setVin(vin);

                        car.setEngineOilType(engineOilType);
                        car.setEngineCoolantType(engineCoolantType);
                        car.setBrakeType(brakeType);
                        car.setSteeringType(powerSteeringType);

                        if (!vins.isEmpty() && !(vins.contains(vin))) {
                        helper.insertCar(car);
                    }
                    }
                } else {
                    List<Car> cars = helper.getAllCar();
                    if(cars != null && cars.size() > 0) {
                        Car car1 = cars.get(cars.size() - 1);
                        if (car1 != null) {
                            tv_make.setText(car1.getMake());
                            tv_model.setText(car1.getModel());
                            int yearString = car1.getYear();
                            tv_year.setText(String.valueOf(yearString));
                            tv_vin.setText(car1.getVin());
                            tv_engineOilType.setText(car1.getEngineOilType());
                            tv_engineCoolantType.setText(car1.getEngineCoolantType());
                            tv_brakeType.setText(car1.getBrakeType());
                            tv_powerSteeringType.setText(car1.getSteeringType());
                }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
