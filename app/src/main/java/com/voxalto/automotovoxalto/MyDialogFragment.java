package com.voxalto.automotovoxalto;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
 * Created by Hassnae on 17/06/2016.
 */
public class MyDialogFragment extends DialogFragment {
    private JsonTask task;
    private String output;
    private String model_tabs, make_tabs, year_tabs;
    private String engineOilType_tabs = "10W40";
    private String engineCoolantType_tabs = "IAT";
    private String brakeType_tabs = "DOT5";
    private String powerSteeringType_tabs = "Mineral Oil XXX";
    private boolean ByVin = false;

    static MyDialogFragment newInstance() {
        return new MyDialogFragment();
    }


    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alertBuilder =  new  AlertDialog.Builder(getActivity());
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View dialogView = inflater.inflate(R.layout.pop_up, null);
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle("Add a Car");
        alertBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alertBuilder.setNegativeButton("Scan By Model", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText model = (EditText)getDialog().findViewById(R.id.TFmodelPopUp);
                EditText make = (EditText)getDialog().findViewById(R.id.TFmakePopUp);
                EditText year = (EditText)getDialog().findViewById(R.id.TFyearPopUp);

                String str_make = make.getText().toString();
                String str_model = model.getText().toString();
                String str_year = year.getText().toString();

                if (str_make == null || str_make.isEmpty() || str_make.equals("null")){
                    Toast.makeText(getDialog().getContext(), "Please enter a valid Make", Toast.LENGTH_LONG).show();
                }
                else if(str_model == null || str_model.isEmpty() || str_model.equals("null")){
                    Toast.makeText(getDialog().getContext(), "Please enter a valid Model", Toast.LENGTH_LONG).show();
                }
                else if (str_year == null || str_year.isEmpty() || str_year.equals("null")){
                    Toast.makeText(getDialog().getContext(), "Please enter a valid Year", Toast.LENGTH_LONG).show();
                }
                else{
                    if (!str_model.equals("")) {
                        EditDialogListenerScanByModel activity = (EditDialogListenerScanByModel) getActivity();
                        ByVin = false;
                        activity.updateResultByModel(ByVin,str_make, str_model, str_year, engineOilType_tabs, engineCoolantType_tabs, brakeType_tabs, powerSteeringType_tabs);
                        getDialog().dismiss();
                    }
                }
            }
        });
        alertBuilder.setPositiveButton("Scan By Vin", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                EditText vin = (EditText) getDialog().findViewById(R.id.TFvinPopUp);
                String vin_tabs = vin.getText().toString();
                //TO DO : should modify it
                if(vin_tabs == null || vin_tabs.isEmpty() || vin_tabs.equals("null")){
                    Toast.makeText(getDialog().getContext(), "Please enter a valid VIN", Toast.LENGTH_LONG).show();
                    return;
                }
                else{
                    JSONObject obj = null;
                    task = new JsonTask();
                    try {
                        output = task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vin_tabs + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy").get();
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
                            model_tabs = obj.getJSONObject("model").getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            make_tabs = obj.getJSONObject("make").getString("name");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        try {
                            year_tabs = obj.getJSONArray("years").getJSONObject(0).getString("year");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        EditDialogListener activity = (EditDialogListener) getActivity();
                        ByVin = true;
                        activity.updateResult(ByVin,vin_tabs,model_tabs);
                        getDialog().dismiss();
                    }
                    else {
                    Toast.makeText(getDialog().getContext(), "Please enter a valid VIN", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        return alertBuilder.create();
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
                            JSONArray yearArray = parentObject.getJSONArray("years");
                            JSONObject yearObject = null;
                            if (yearArray != null) {
                                yearObject = yearArray.getJSONObject(0);
                                if (yearObject != null) {
                                    year_tabs = yearObject.getString("year");
                                    JSONArray stylesArray = yearObject.getJSONArray("styles");
                                    JSONObject stylesObject = stylesArray.getJSONObject(0);
                                }
                            }
                        }
                        if (parentObject != null) {
                            if (parentObject.optJSONArray("engines") != null) {
                                JSONArray enginesObject = parentObject.getJSONArray("engines");
                                JSONObject engines  = enginesObject.getJSONObject(0);
                            }
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public interface EditDialogListener {
        void updateResult(boolean ByVin,String vin_tabs,String model_tabs);
    }

    public interface EditDialogListenerScanByModel {
        void updateResultByModel(boolean ByVin,String make_tabs,String model_tabs,String year_tabs, String engineOilType_tabs,String engineCoolantType_tabs,String brakeType_tabs,String powerSteeringType_tabs);
    }

}
