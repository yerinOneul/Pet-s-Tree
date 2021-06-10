package kr.ac.gachon.sw.petstree.util;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

public class Gps extends Service implements LocationListener {

    private final Context context;
    Location location;
    double latitude, longtitude;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000*60*1;
    protected LocationManager locationManager;

    public Gps(Context context) {
        this.context = context;
        getLocation();
    }

    public Location getLocation() {
        try{
            locationManager = (LocationManager)context.getSystemService(LOCATION_SERVICE);
            boolean GPS_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean Net_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            int fineLoc_permission = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            int coarseLoc_permission = ContextCompat.checkSelfPermission(context,Manifest.permission.ACCESS_COARSE_LOCATION);
            if (fineLoc_permission != PackageManager.PERMISSION_GRANTED || coarseLoc_permission != PackageManager.PERMISSION_GRANTED )
                return null;
            if(Net_enabled){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                if(locationManager != null){
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null){
                        latitude = location.getLatitude();
                        longtitude = location.getLongitude();
                    }
                }
            }
            if(GPS_enabled){
                if (location == null){
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_BW_UPDATES,MIN_DISTANCE_CHANGE_FOR_UPDATES,this);
                    if (locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location != null){
                            latitude = location.getLatitude();
                            longtitude = location.getLongitude();
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }

    public double getLatitude(){
        if (location != null){
            latitude = location.getLatitude();
        }
        return latitude;
    }

    public double getLongitude(){
        if (location != null){
            longtitude = location.getLongitude();
        }
        return longtitude;
    }

    public void removeGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(Gps.this);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}
