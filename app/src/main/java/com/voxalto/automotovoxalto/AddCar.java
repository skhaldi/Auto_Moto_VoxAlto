package com.voxalto.automotovoxalto;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
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
import android.widget.TextView;

import com.voxalto.automotovoxalto.adapter.SlidingMenuAdapter;
import com.voxalto.automotovoxalto.fragment.Fragment1;
import com.voxalto.automotovoxalto.fragment.Fragment2;
import com.voxalto.automotovoxalto.fragment.Fragment3;
import com.voxalto.automotovoxalto.model.ItemSildeMenu;

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

public class AddCar extends AppCompatActivity {
    EditText vin;
    String vinEntered = "";
    String model = "";
    String make = "";
    String year = "";
    String engineOilType = "10W40";
    String engineCoolantType = "IAT";
    String brakeType = "DOT5";
    String powerSteeringType = "Mineral Oil XXX";

    private JsonTask task = new JsonTask();
    private JsonTask task2 = new JsonTask();
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DatabaseHelper helper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_car);
        helper = new DatabaseHelper(this);
        slidingMenu();
    }

    public void OnClickButtonScanVin(View v) throws ExecutionException, InterruptedException, JSONException {
        if(v.getId() == R.id.ScanVin){
            vin = (EditText)findViewById(R.id.TFvin);
            vinEntered = vin.getText().toString();
            JSONObject obj = null;
            String output = task.execute("https://api.edmunds.com/api/vehicle/v2/vins/" + vinEntered + "?fmt=json&api_key=rp2xq63y4bf3nc2gusq9a2uy").get();
            if (output != null) {
                obj = new JSONObject(output);
            model = obj.getJSONObject("model").getString("name");
            }
            Intent i = new Intent(AddCar.this, CarModelTabs.class);
            i.putExtra("Model", model);
            i.putExtra("Make", make);
            i.putExtra("Year",year);
            i.putExtra("EngineOilType", engineOilType);
            i.putExtra("EngineCoolantType", engineCoolantType);
            i.putExtra("BrakeType", brakeType);
            i.putExtra("PowerSteeringType", powerSteeringType);
            i.putExtra("VIN", vinEntered);
            startActivity(i);
        }
    }

    public void OnClickButtonCancelVin(View v){
        if(v.getId() == R.id.CancelVin){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }

    public void OnClickButtonScanModel(View v){
        if(v.getId() == R.id.ScanModel){

            EditText model = (EditText)findViewById(R.id.TFmodel);
            EditText make = (EditText)findViewById(R.id.TFmake);
            EditText year = (EditText)findViewById(R.id.TFyear);

            String str_model = model.getText().toString();
            String str_make = make.getText().toString();
            String str_year = year.getText().toString();

            Intent i = new Intent(this, CarModelTabs.class);
            i.putExtra("Model",str_model);
            i.putExtra("Make",str_make);
            i.putExtra("Year",str_year);
            startActivity(i);
        }
    }

    public void OnClickButtonCancelModel(View v){
        if(v.getId() == R.id.CancelModel){
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
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
                String vin = "";
                if (result != null) {
                    parentObject = new JSONObject(result);
                    if (parentObject != null) {
                        if (parentObject.optJSONObject("make") != null) {
                            JSONObject makeObject = parentObject.getJSONObject("make");
                            if (makeObject != null) {
                                make = makeObject.getString("name");
                            }
                            JSONObject modelObject = parentObject.getJSONObject("model");
                            if (modelObject != null) {
                                model = modelObject.getString("name");
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
                                    year = yearObject.getString("year");
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
                } else {
                    List<Car> cars = helper.getAllCar();
                    if(cars != null && cars.size() > 0) {
                        Car car1 = cars.get(cars.size() - 1);
                        if (car1 != null) {
                            make = car1.getMake();
                            model = car1.getModel();
                            int yearString = car1.getYear();
                            year = String.valueOf(yearString);
                            vinEntered = car1.getVin();
                            engineOilType = car1.getEngineOilType();
                            engineCoolantType  = car1.getEngineCoolantType();
                            brakeType = car1.getBrakeType();
                            powerSteeringType = car1.getSteeringType();
                        }
                    }
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
