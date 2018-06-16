package com.application.handing.vateapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import static com.application.handing.vateapp.Fragments.setFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.nav_open, R.string.nav_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        nvDrawer = findViewById(R.id.nav_view);
        nvDrawer.setNavigationItemSelectedListener(this);

        setFragment(this, HomeFragment.newInstance());
    }

    @Override
    public void onBackPressed() {
        //TODO handle backpressed calling single fragment method
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.frag_home:
                setFragment(this, HomeFragment.newInstance());
                break;
            case R.id.listaBeacon:
                setFragment(this, BeaconsFragment.newInstance());
                break;
            case R.id.info:
                setFragment(this, InfoFragment.newInstance());
                break;
            case R.id.bevi:
                setFragment(this, BevilacquaFragment.newInstance("bevilacqua"));
                break;
            case R.id.negozi:
                setFragment(this, BevilacquaFragment.newInstance("negozi"));
                break;
            case R.id.bar:
                setFragment(this, BevilacquaFragment.newInstance("bar"));
                break;
        }

        //TODO select item in the navigation drawer

        mDrawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

}