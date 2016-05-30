package com.voxalto.automotovoxalto;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;

import com.voxalto.automotovoxalto.adapter.SlidingMenuAdapter;
import com.voxalto.automotovoxalto.fragment.Fragment1;
import com.voxalto.automotovoxalto.fragment.Fragment2;
import com.voxalto.automotovoxalto.fragment.Fragment3;
import com.voxalto.automotovoxalto.model.ItemSildeMenu;

import java.util.ArrayList;
import java.util.List;


public class CarInfoMnt extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String model_car, make_car, year_car, engineOilType_car, engineCoolantType_car, brakeType_car, powerSteeringType_car;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.car_info_mnt);

        make_car = getIntent().getStringExtra("Make");
        model_car = getIntent().getStringExtra("Model");
        year_car = getIntent().getStringExtra("Year");
        engineOilType_car = getIntent().getStringExtra("EngineOilType");
        engineCoolantType_car = getIntent().getStringExtra("EngineCoolantType");
        brakeType_car = getIntent().getStringExtra("BrakeType");
        powerSteeringType_car = getIntent().getStringExtra("PowerSteeringType");

        Intent intent = new Intent(this, InfoCar.class);
        intent.putExtra("Model", model_car);
        intent.putExtra("Make", make_car);
        intent.putExtra("Year", year_car);
        intent.putExtra("EngineOilType", engineOilType_car);
        intent.putExtra("EngineCoolantType", engineCoolantType_car);
        intent.putExtra("BrakeType", brakeType_car);
        intent.putExtra("PowerSteeringType", powerSteeringType_car);

        getSupportActionBar().hide();

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new InfoCar(), "Info");
        adapter.addFragment(new Maintenance(), "Maintenance");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}