package com.example.jale.poi_app;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Search extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //test again

    Toolbar toolbar = null;
     NavigationView navigationView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set the fragment initially
        SearchFragment fragment = new SearchFragment();
        android.support.v4.app.FragmentTransaction fragmentTransaction =
                getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        // Permission grant/gewähren
        String[] permissions =
                {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET};
        ActivityCompat.requestPermissions(this, permissions,
                MY_PERMISSIONS_STORAGE_INTENRET);

        // Bind to RequestService
        Intent myIntent = new Intent(this, RequestService.class);
        // startService(myIntent); oder alternativ:
        bindService(myIntent, mConnection, Context.BIND_AUTO_CREATE);



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





    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.favoriten) {

            FavoriteFragment fragment = new FavoriteFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        } else if (id == R.id.suche) {

            SearchFragment fragment = new SearchFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();


        } else if (id == R.id.einstellungen) {
            SettingsFragment fragment = new SettingsFragment();
            android.support.v4.app.FragmentTransaction fragmentTransaction =
                    getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    final int MY_PERMISSIONS_STORAGE_INTENRET = 1;

    RequestService mService = null;
    boolean mBound = false;

    //--------------------------    service Verbindung einrichten
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // an den RequestService binden,
            // service-Objekt casting auf IBinder und LocalService instance erhalten
            RequestService.RequestServiceBinder binder =
                    (RequestService.RequestServiceBinder) service;
            mService = binder.getService();
            // callback setzen
            mService.setCallback(getHandler());

            mBound = true;
        }
        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    //--------------------------

    //--------------------------handler einrichten:
    //  callbacks vereinbaren für service binding,
    // weiterleiten an bindService() für ergebnismitteilung
    private Handler getHandler() {
        final Handler callbackHandler = new Handler() {
            public void handleMessage(Message msg) {
                Bundle bundle = msg.getData();
                String sfile = (String) bundle.get( RequestService.FILEPATH );
                String uniqueId = (String) bundle.get( RequestService.UNIQUEID );
                String note = (String) bundle.get( RequestService.NOTIFICATION );
                Toast.makeText(Search.this, uniqueId +" file: " + sfile+" Bytes: "+note, Toast.LENGTH_LONG).show();
            }// handleMessage
        };
        return callbackHandler;
    }
    //--------------------------



    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_STORAGE_INTENRET:
            {
                // wenn die anfrage gecancelled wird, sind die Ergnisfelder leer.
                if (grantResults.length > 0)
                {
                    for ( int grant = 0; grant < grantResults.length; grant++)
                    {
                        if ( grantResults[grant] == PackageManager.PERMISSION_GRANTED)
                            // Permissions wurden gewährt
                            System.out.println (permissions[grant]+" vorhanden");
                            // Zugriffe ausführen ..
                        else
                            System.out.println (permissions[grant]+"  n i c h t  vorhanden");
                        // Permissions werden abgelehnt,
                        // spezifische Zugriffe werden nicht ausgeführt
                    }
                }
                return;
            }
            // Prüfung anderer/weiterer Permissions
        }
    }

    @Override
    public void finish() {
        System.out.println("******  bye bye");
        unbindService(mConnection);
        super.finish();
    }}

