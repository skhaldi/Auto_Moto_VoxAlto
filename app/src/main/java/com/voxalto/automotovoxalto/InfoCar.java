package com.voxalto.automotovoxalto;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.concurrent.ExecutionException;


public class InfoCar extends Fragment{
    private String model,modelEntered,make,makeEntered,year,yearEntered,vinEntered,engineOilType,engineCoolantType,brakeType,powerSteeringType;
    
    private TextView tv_vin, tv_make, tv_model, tv_year, tv_engineOilType, tv_engineCoolantType, tv_brakeType, tv_powerSteeringType;

    private DatabaseHelper helper;
    private JsonTask task;

    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        helper = new DatabaseHelper(getActivity());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info_car, container, false);

        SharedPreferences prefModel = getContext().getSharedPreferences("KeyByModel", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorModel = prefModel.edit();
        String comingFrom1 = prefModel.getString("ComingFrom", null);

        SharedPreferences prefVin = getContext().getSharedPreferences("KeyByVin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorVin = prefVin.edit();
        String comingFrom2 = prefVin.getString("ComingFrom", null);

        if ( (comingFrom1 == null || comingFrom1.isEmpty() || comingFrom1.equals("null")) && (comingFrom2 == null || comingFrom2.isEmpty() || comingFrom2.equals("null")) ) {
            GetVehicleInfoFromAddCar(v);
        }
        else{
            GetVehicleInfoFromDialog(v);
        }
        return v;
    }



    public void GetVehicleInfoFromDialog(View view){
        SharedPreferences prefVin = getContext().getSharedPreferences("KeyByVin", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorVin = prefVin.edit();
        boolean ByVin = prefVin.getBoolean("ByVin", false);

        if (ByVin == true) {
            vinEntered = prefVin.getString("VIN", null);
            editorVin.clear();
            editorVin.commit();
            JSONObject obj = null;
        if (vinEntered != null) {
            task = new JsonTask();
                String output = null;
                try {
                    output = task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vinEntered + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy").get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (output != null) {
                    try {
                        obj = new JSONObject(output);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        model = obj.getJSONObject("model").getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    try {
                        make = obj.getJSONObject("make").getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
        }
                    try {
                        year = obj.getJSONArray("years").getJSONObject(0).getString("year");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    engineOilType = "10W40";
                    engineCoolantType = "IAT";
                    brakeType = "DOT5";
                    powerSteeringType = "Mineral Oil XXX";
                }
            }
        }
        else {
            SharedPreferences prefModel = getContext().getSharedPreferences("KeyByModel", Context.MODE_PRIVATE);
            SharedPreferences.Editor editorModel = prefModel.edit();
            String modelEntered = prefModel.getString("Model", null);
            String makeEntered = prefModel.getString("Make", null);
            String yearEntered = prefModel.getString("Year", null);
            String engineOilType = prefModel.getString("engineOilType", null);
            String engineCoolantType = prefModel.getString("engineCoolantType", null);
            String brakeType = prefModel.getString("brakeType", null);
            String powerSteeringType = prefModel.getString("powerSteeringType", null);
            editorModel.clear();
            editorModel.commit();

            TableRow tableVin = (TableRow) view.findViewById(R.id.vin_table);
            tableVin.setVisibility(tableVin.GONE);

            tv_make = (TextView) view.findViewById(R.id.V_make);
            tv_model = (TextView) view.findViewById(R.id.V_model);
            tv_year = (TextView) view.findViewById(R.id.V_year);

            tv_engineOilType = (TextView) view.findViewById(R.id.engineOilType);
            tv_engineCoolantType = (TextView) view.findViewById(R.id.engineCoolantType);
            tv_brakeType = (TextView) view.findViewById(R.id.brakeType);
            tv_powerSteeringType = (TextView) view.findViewById(R.id.powerSteeringType);

            tv_make.setText(makeEntered);
            tv_model.setText(modelEntered);
            tv_year.setText(yearEntered);
            tv_engineOilType.setText(engineOilType);
            tv_engineCoolantType.setText(engineCoolantType);
            tv_brakeType.setText(brakeType);
            tv_powerSteeringType.setText(powerSteeringType);
        }
    }



    public void GetVehicleInfoFromAddCar(View view) {
        vinEntered = getActivity().getIntent().getStringExtra("VIN");
        JSONObject obj = null;
        if (vinEntered != null) {
            task = new JsonTask();
            String output = null;
            try {
                output = task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vinEntered + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy").get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if (output != null) {
                try {
                    obj = new JSONObject(output);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    model = obj.getJSONObject("model").getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    make = obj.getJSONObject("make").getString("name");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    year = obj.getJSONArray("years").getJSONObject(0).getString("year");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                engineOilType = "10W40";
                engineCoolantType = "IAT";
                brakeType = "DOT5";
                powerSteeringType = "Mineral Oil XXX";
            }
        }
        else {
            makeEntered = getActivity().getIntent().getStringExtra("Make");
            modelEntered = getActivity().getIntent().getStringExtra("Model");
            yearEntered = getActivity().getIntent().getStringExtra("Year");
        engineOilType = getActivity().getIntent().getStringExtra("EngineOilType");
        engineCoolantType = getActivity().getIntent().getStringExtra("EngineCoolantType");
        brakeType = getActivity().getIntent().getStringExtra("BrakeType");
        powerSteeringType = getActivity().getIntent().getStringExtra("PowerSteeringType");

            TableRow tableVin = (TableRow) view.findViewById(R.id.vin_table);
            tableVin.setVisibility(tableVin.GONE);

            tv_make = (TextView) view.findViewById(R.id.V_make);
            tv_model = (TextView) view.findViewById(R.id.V_model);
            tv_year = (TextView) view.findViewById(R.id.V_year);

            tv_engineOilType = (TextView) view.findViewById(R.id.engineOilType);
            tv_engineCoolantType = (TextView) view.findViewById(R.id.engineCoolantType);
            tv_brakeType = (TextView) view.findViewById(R.id.brakeType);
            tv_powerSteeringType = (TextView) view.findViewById(R.id.powerSteeringType);

            tv_make.setText(makeEntered);
            tv_model.setText(modelEntered);
            tv_year.setText(yearEntered);
            tv_engineOilType.setText(engineOilType);
            tv_engineCoolantType.setText(engineCoolantType);
            tv_brakeType.setText(brakeType);
            tv_powerSteeringType.setText(powerSteeringType);
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
                            }
                            else vin = vinEntered.toUpperCase();
                            JSONArray yearArray = parentObject.getJSONArray("years");
                            JSONObject yearObject = null;
                            if (yearArray != null) {
                                yearObject = yearArray.getJSONObject(0);
                                if (yearObject != null) {
                                    yearName = yearObject.getString("year");
                                    JSONArray stylesArray = yearObject.getJSONArray("styles");
                                    JSONObject stylesObject = stylesArray.getJSONObject(0);
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
