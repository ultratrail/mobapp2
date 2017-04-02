package teamtreehouse.com.iamhere;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;

import java.lang.ref.WeakReference;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import static teamtreehouse.com.iamhere.MainActivity.BLUETOOTH_SERVICE_ACTIVE;


public class MapsActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, GoogleMap.OnMarkerClickListener, OnMapReadyCallback {

    public static final String TAG = MapsActivity.class.getSimpleName();
    private Hashtable<String, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();

    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Boolean markerCliked = false;
    private Polyline polylineChefAMarker;
    private Polyline polylineProcheAMarker;
    public Hashtable<String, Marker> listeDesMarkers = new Hashtable<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        //setUpMapIfNeeded();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        // USB Section


        mHandler = new MyHandler(this);
        if (BLUETOOTH_SERVICE_ACTIVE) {
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }



    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {

            ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

            if(mMap!=null)
                setUpMap();
        } else {
            setUpMap();
        }

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {



        int nombrePersonne = UltraTeamApplication.getInstance().getNbPersonnes();

        Random random = new Random();

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        for (int i = 0; i < nombrePersonne; i++) {
            Double randomLat = random.nextDouble() * 2 - 1;
            Double randomLon = random.nextDouble() * 2 - 1;

            LatLng randomPosition = new LatLng(45.166672 + randomLat, 5.71667 + randomLon);
            Marker m = mMap.addMarker(new MarkerOptions().position(randomPosition).title(personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(i)).getNom()));
            listeDesMarkers.put(personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(i)).getNom(), m);
            personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(i)).setMarker(m);

            personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(i)).setPosition(randomPosition);

            builder.include(m.getPosition());
        }

        //Centrer la camera pour voir tous les markers
        LatLngBounds bounds = builder.build();
        int padding = 0 ;
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,padding);
        mMap.moveCamera(cu);







       /* //Comme je peux pas test avec mon tél je mais des positions random pour tester
        Marker m1 = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Membre 1"));
        Marker m2 = mMap.addMarker(new MarkerOptions().position(new LatLng(45, 0)).title("Membre 2"));
        Marker m3 = mMap.addMarker(new MarkerOptions().position(new LatLng(0, 45)).title("Membre 3"));*/




    }

    private void handleNewLocation(Location location) {
        Log.d(TAG, location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();

        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        Hashtable<String, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();

        //TODO Le marker du chef sera pas mis à jour

        personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).setPosition(latLng);

        Marker m = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("You"));

        //personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).setMarker(m);



        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


        if(UltraTeamApplication.getInstance().getBase()==null){

            m=mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(location.getLatitude()+1, location.getLongitude()))
                    .title("Point de rdv")
                    .draggable(true)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            UltraTeamApplication.getInstance().setBase(m);


        }

    }

    @Override
    public void onConnected(Bundle bundle) {
        //if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.

        // return;
        // }
        try {
            Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            } else {
                handleNewLocation(location);
            }
        } catch (SecurityException s) {
            //TODO
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                /*
                 * Thrown if Google Play services canceled the original
                 * PendingIntent
                 */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
            /*
             * If no resolution is available, display a dialog to the
             * user with the error.
             */
            Log.i(TAG, "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }


    // USB


    /*
    * Notifications from UsbService will be received here.
    */
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction()) {
                case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
                    Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
                    Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_NO_USB: // NO USB CONNECTED
                    Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
                    Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
                    break;
                case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
                    Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };



    private void updateMarker(String data) {
        /*
        String id = null;
        Double lat = null;
        Double lon = null;

        Hashtable<Integer, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();

        if (p.containsKey(id)) {
            listeDesMarkers.get(id).setPosition(new LatLng(lat, lon));
        } else {
            Marker m = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(10, 10))
                    .title("Position de " + id));
            listeDesMarkers.put(Integer.getInteger(id), m);

        }*/


    }

    // USB Fonctions

    private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
        if (!UsbService.SERVICE_CONNECTED) {
            Intent startService = new Intent(this, service);
            if (extras != null && !extras.isEmpty()) {
                Set<String> keys = extras.keySet();
                for (String key : keys) {
                    String extra = extras.getString(key);
                    startService.putExtra(key, extra);
                }
            }
            startService(startService);
        }
        Intent bindingIntent = new Intent(this, service);
        bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void setFilters() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
        filter.addAction(UsbService.ACTION_NO_USB);
        filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
        filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
        filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
        registerReceiver(mUsbReceiver, filter);
    }


    public double distance(LatLng pa, LatLng pb) {
        double lat_a = pa.latitude;
        double lng_a = pa.longitude;
        double lat_b = pb.latitude;
        double lng_b = pb.longitude;


        double earthRadius = 3958.75;
        double latDiff = Math.toRadians(lat_b - lat_a);
        double lngDiff = Math.toRadians(lng_b - lng_a);
        double a = Math.sin(latDiff / 2) * Math.sin(latDiff / 2) +
                Math.cos(Math.toRadians(lat_a)) * Math.cos(Math.toRadians(lat_b)) *
                        Math.sin(lngDiff / 2) * Math.sin(lngDiff / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = earthRadius * c;

        int meterConversion = 1609;

        return new Float(distance * meterConversion).floatValue();
    }

    String getHumanDistance(double distance){
        if(distance<1000){
            return distance + "m";
        } else {
            return distance/1000 + "km";
        }
    }


    private Personne getPersonneLaPlusProche(LatLng position){
        Hashtable<String, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();

        Personne personneLaplusProche = personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0));
        double distanceChef= distance(personneLaplusProche.getPosition(), position);

        Iterator<Personne> itr = personnes.values().iterator();
        double distanceMin=distanceChef;
        Personne personneCourante=null;

        while(itr.hasNext()) {
            personneCourante = itr.next();

            double distanceCourante = distance(position,personneCourante.getPosition());


            if(distanceCourante!=0 && distanceMin>distanceCourante){
                personneLaplusProche=personneCourante;
                distanceMin=distanceCourante;
            }
        }

        return personneLaplusProche;

    }




    @Override
    public boolean onMarkerClick(Marker marker) {

        Hashtable<String, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();

        if (!marker.getTitle().contains("Point de rdv")) {

            if (personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).getNom().equals(marker.getTitle()))
                markerCliked = true;
            else
                markerCliked = false;
            //Hashtable<String, Personne> personnes = UltraTeamApplication.getInstance().getPersonnes();
            LatLng posChef  = personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).getPosition();

            LatLng posMembre = marker.getPosition();

            double distanceChef= distance(posChef, posMembre);
            Personne personneLaplusProche = getPersonneLaPlusProche(posMembre);


            marker.setSnippet("est à : " + getHumanDistance(distanceChef));
            personneLaplusProche.getMarker().setSnippet("");

            double distanceMin = distance(personneLaplusProche.getPosition(),posMembre);

            Snackbar snackbar = Snackbar
                    .make(findViewById(R.id.map), personneLaplusProche.getNom() + " est la personne la plus proche de " + marker.getTitle() +" à " + getHumanDistance(distanceMin) , Snackbar.LENGTH_LONG);


            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(Color.DKGRAY);


            TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();




            if(polylineChefAMarker !=null){
                polylineChefAMarker.remove();
            }

            PolylineOptions polylineOptions = new PolylineOptions().add(posChef).add(posMembre).color(0xFFFF0000).startCap(new RoundCap()).endCap(new RoundCap());

            polylineChefAMarker =mMap.addPolyline(polylineOptions);

            if(polylineProcheAMarker !=null){
                polylineProcheAMarker.remove();
            }

            polylineOptions = new PolylineOptions().add(personneLaplusProche.getPosition()).add(posMembre).color(0xFF0000FF).startCap(new RoundCap()).endCap(new RoundCap());

            polylineProcheAMarker =mMap.addPolyline(polylineOptions);



        }
        return false;


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;
        mMap.setOnMarkerClickListener(this);
        setUpMapIfNeeded();
    }

    /*
     * This handler will be passed to UsbService. Data received from serial port is displayed through this handler
     */
    private static class MyHandler extends Handler {
        private final WeakReference<MapsActivity> mActivity;

        public MyHandler(MapsActivity activity) {
            mActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbService.MESSAGE_FROM_SERIAL_PORT:
                    String data = (String) msg.obj;
                    //mActivity.get().add.append(data);

                    mActivity.get().updateMarker(data);
                    break;
            }
        }
    }

    private MyHandler mHandler;

    private UsbService usbService;

    private final ServiceConnection usbConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName arg0, IBinder arg1) {
            usbService = ((UsbService.UsbBinder) arg1).getService();
            usbService.setHandler(mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            usbService = null;
        }
    };

    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!listeDesMarkers.isEmpty()) {
                final String action = intent.getAction();
                if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                    Random random = new Random();
                    Double randomLat = random.nextDouble() * 2 - 1;
                    Double randomLon = random.nextDouble() * 2 - 1;
                    Double tmpLat = listeDesMarkers.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).getPosition().latitude + randomLat;
                    Double tmpLon = listeDesMarkers.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).getPosition().longitude + randomLon;
                    LatLng position = new LatLng(tmpLat, tmpLon);
                    listeDesMarkers.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).remove();
                    listeDesMarkers.remove(UltraTeamApplication.getInstance().getAdapter().getItem(0));
                    Marker m = mMap.addMarker(new MarkerOptions().position(position).title(personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).getNom()));
                    listeDesMarkers.put(personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).getNom(), m);
                    LatLng posChef = personnes.get(UltraTeamApplication.getInstance().getAdapter().getItem(0)).getPosition();

                    LatLng posMembre = m.getPosition();
                    if (markerCliked) {
                        if (polylineChefAMarker != null) {
                            polylineChefAMarker.remove();
                        }

                        PolylineOptions polylineOptions = new PolylineOptions().add(posChef).add(posMembre).color(0xFFFF0000).startCap(new RoundCap()).endCap(new RoundCap());

                        polylineChefAMarker = mMap.addPolyline(polylineOptions);

                        if (polylineProcheAMarker != null) {
                            polylineProcheAMarker.remove();
                        }

                        polylineOptions = new PolylineOptions().add(getPersonneLaPlusProche(position).getPosition()).add(posMembre).color(0xFF0000FF).startCap(new RoundCap()).endCap(new RoundCap());

                        polylineProcheAMarker = mMap.addPolyline(polylineOptions);
                    }
                }
            }
        }
    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
}
