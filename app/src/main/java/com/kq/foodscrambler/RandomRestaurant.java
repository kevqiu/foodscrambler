package com.kq.foodscrambler;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class RandomRestaurant extends AppCompatActivity implements OnMapReadyCallback
{
    private GoogleMap map;
    SharedPreferences sharedPrefs;
    private static final int LOCATION_REQUEST_CODE = 1;
    private static final String scramLoc = "scrambleLocation";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.random_restaurant);

        // initialize sharedPrefs
        sharedPrefs = getSharedPreferences(scramLoc, Context.MODE_PRIVATE);

        // set title bar
        getSupportActionBar().setTitle("Random Restaurant");
        Button scrambleButton = (Button) findViewById(R.id.scrambleButton);

        // check if location/gps is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
        {
            // alert user that location/gps is not active
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Location Services Not Active");
            builder.setMessage("Please enable Location Services and GPS");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialogInterface, int i)
                {
                    // Show location settings when the user acknowledges the alert dialog
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            Dialog alertDialog = builder.create();
            alertDialog.setCanceledOnTouchOutside(false);
            alertDialog.show();
        }

        // for >M, check if location perm is given, prompt if not
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST_CODE);
        }

        // initialize googleMpa fragment
        MapFragment mapFrag = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFrag.getMapAsync(this);

        // scramble button
        scrambleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(v.getContext(),
                        "Latitude: " + Double.longBitsToDouble(sharedPrefs.getLong("Latitude", 0))
                                + " Longitude: " + Double.longBitsToDouble(sharedPrefs.getLong("Longitude", 0)),
                        Toast.LENGTH_SHORT).show();
                Intent locSelect = new Intent(getApplicationContext(), LocationSelector.class);
                startActivity(locSelect);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);
            }
        });

    }

    // run when map is initialized
    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        map = googleMap;
        // if permission not granted, stop
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED)
        {
            Toast.makeText(this, "Location permission not granted!", Toast.LENGTH_SHORT).show();
            return;
        }
        else
        {
            // automatically set initial location
            double lat = Double.longBitsToDouble(sharedPrefs.getLong("Latitude", 0));
            double lon = Double.longBitsToDouble(sharedPrefs.getLong("Longitude", 0));
            if(lat == 0 && lon == 0)
                goToMyLocation(false);
            else
                goToLocation(lat, lon);

            // when tapping on the screen
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener()
            {
                @Override
                public void onMapClick(LatLng latLng)
                {
                    map.animateCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder().target(latLng).zoom(16).bearing(0).build()));
                    placeMarker(latLng);
                    updateScrambleLocation(latLng);
                }
            });

            // clicking on my location button
            map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener()
            {
                @Override
                public boolean onMyLocationButtonClick()
                {
                    goToMyLocation(true);
                    return false;
                }
            });
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if (requestCode == LOCATION_REQUEST_CODE)
        {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
            {
                Toast.makeText(this, "Location permission has not been granted", Toast.LENGTH_SHORT).show();
            }
            else
            {
                goToMyLocation(false);
            }
        }
        else
        {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public void goToMyLocation(boolean animate)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        map.setMyLocationEnabled(true);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), false);
        Location myLoc = lm.getLastKnownLocation(provider);
        LatLng myLatLng = new LatLng(myLoc.getLatitude(), myLoc.getLongitude());
        CameraPosition myPos = new CameraPosition.Builder()
                .target(myLatLng).zoom(16).bearing(0).build();
        if(animate)
            map.animateCamera(CameraUpdateFactory.newCameraPosition(myPos));
        else
            map.moveCamera(CameraUpdateFactory.newCameraPosition(myPos));
        placeMarker(myLatLng);
        updateScrambleLocation(myLatLng);
    }

    public void goToLocation(double lat, double lon)
    {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            return;
        map.setMyLocationEnabled(true);
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        String provider = lm.getBestProvider(new Criteria(), false);
        Location myLoc = lm.getLastKnownLocation(provider);
        LatLng myLatLng = new LatLng(lat, lon);
        CameraPosition myPos = new CameraPosition.Builder()
                .target(myLatLng).zoom(16).bearing(0).build();
        map.moveCamera(CameraUpdateFactory.newCameraPosition(myPos));
        placeMarker(myLatLng);
        updateScrambleLocation(myLatLng);
    }

    // save the tapped location
    public void updateScrambleLocation(LatLng latLng)
    {
        SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.putLong("Latitude", Double.doubleToLongBits(latLng.latitude));
        editor.putLong("Longitude", Double.doubleToLongBits(latLng.longitude));
        editor.commit();
    }

    // place a marker
    public void placeMarker(LatLng latLng)
    {
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title("Scramble Here!"));
    }

}
