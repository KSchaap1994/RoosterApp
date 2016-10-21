package io.github.kschaap1994.roosterapp.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
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
import io.github.kschaap1994.roosterapp.fragment.PlaceholderFragment;
import io.github.kschaap1994.roosterapp.fragment.ScheduleFragment;
import io.github.kschaap1994.roosterapp.fragment.SettingsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.toolbar)
    public Toolbar toolbar;
    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;

    private DbLab dbLab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        final ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        dbLab = DbLab.get(this);

        setupFragment();
    }

    public void setupFragment() {
        final FragmentTransaction tx = getSupportFragmentManager().beginTransaction();

        if (dbLab.hasSettings()) {
            tx.replace(R.id.content_main, new ScheduleFragment());
            navigationView.getMenu().getItem(0).setChecked(true);
        } else {
            tx.replace(R.id.content_main, new PlaceholderFragment());
        }

        tx.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull final MenuItem item) {
        // Handle navigation view item clicks here.
        final int id = item.getItemId();

        final FragmentManager manager = getSupportFragmentManager();

        switch (id) {
            case R.id.nav_home:
                manager.beginTransaction().replace(R.id.content_main,
                        new ScheduleFragment()).commit();
                break;
            case R.id.nav_settings:
                manager.beginTransaction().replace(R.id.content_main,
                        new SettingsFragment()).addToBackStack("settings").commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addSchedule(final View view) {
        FragmentTransaction tx = getSupportFragmentManager().beginTransaction();
        tx.replace(R.id.content_main, new SettingsFragment(), "settings");
        tx.addToBackStack("settings");

        tx.commit();

        navigationView.getMenu().getItem(1).setChecked(true);
    }
}
