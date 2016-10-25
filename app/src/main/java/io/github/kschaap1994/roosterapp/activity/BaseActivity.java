package io.github.kschaap1994.roosterapp.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.kschaap1994.roosterapp.R;
import io.github.kschaap1994.roosterapp.database.DbLab;

/**
 * Created by Kevin on 21-10-2016.
 */

public abstract class BaseActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {

    public abstract Fragment createFragment();

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    protected DbLab lab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Set default orientation
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setSupportActionBar(toolbar);
        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        lab = DbLab.get(this);

        //Get FragmentManager
        final FragmentManager fm = getSupportFragmentManager();
        //Find the fragment container
        Fragment fragment = fm.findFragmentById(R.id.content_main);
        final FragmentTransaction tx = fm.beginTransaction();

        if (fragment == null) {
            fragment = createFragment();
            tx.add(R.id.content_main, fragment).commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                final Intent home = new Intent(this, ScheduleActivity.class).
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(home);
                break;
            case R.id.nav_settings:
                final Intent settings = new Intent(this, SettingsActivity.class).
                        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(settings);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}