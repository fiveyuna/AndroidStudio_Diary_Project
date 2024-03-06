package ddwu.mobile.finalproject.ma01_20190978;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class LocationActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQ_LOC = 100;
    private GoogleMap mGoogleMap;
    private LocationManager locationManager;

    private Marker centerMarker;
    private TextView tvSelectLoc;
    private String selectLoc = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(mapReadyCallBack);

        tvSelectLoc = findViewById(R.id.tvSelectLoc);
        tvSelectLoc.setText("경도, 위도");


    }

    public void onClick(View v) {
        mGoogleMap.clear();
        switch (v.getId()) {
            case R.id.btnStart:
                locationUpate();
                break;
            case R.id.btnStop:
                locationManager.removeUpdates(locationListener);
                setResult(RESULT_CANCELED);
                finish();
                break;
            case R.id.btnSaveLoc:
                Intent resultIntent = new Intent();;
                locationManager.removeUpdates(locationListener);
                resultIntent.putExtra("location", selectLoc);
                setResult(RESULT_OK, resultIntent);
                finish();
                break;
        }
    }

    private void locationUpate() {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 0, locationListener);
        }
    }

    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) { // 이 위치 받아온 걸로 지도 이동시킴
            LatLng currentLoc = new LatLng(location.getLatitude(), location.getLongitude());
            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));
            // => 이 위치로 움직이는  애니메이션으로 보여줌
            centerMarker.setPosition(currentLoc);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }
        @Override
        public void onProviderEnabled(String provider) { }
        @Override
        public void onProviderDisabled(String provider) { }
    };

    OnMapReadyCallback mapReadyCallBack = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            Location lastLoc = null;
            LatLng currentLoc;

            // 마지막으로 Provider가 수신한 위치로 지도를 이동하여 마커 표시 (lastLoc)
            lastLoc = checkLastLocation();
            if (lastLoc == null) {
                currentLoc = new LatLng(37.606320, 127.041808);
            } else {
                currentLoc = new LatLng(lastLoc.getLatitude(), lastLoc.getLongitude());
            }

            mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLoc, 17));

            MarkerOptions options = new MarkerOptions();
            options.position(currentLoc);
            options.title("현재 위치");
            options.snippet("탐색 중");
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            centerMarker = mGoogleMap.addMarker(options);
            centerMarker.showInfoWindow();

            mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    options.title("현재 위치");
                    options.snippet("위도: " + marker.getPosition().latitude + "\n경도" + marker.getPosition().longitude);
                    centerMarker = mGoogleMap.addMarker(options);
                    centerMarker.showInfoWindow();
                    return false;
                }
            });

            mGoogleMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    selectLoc = marker.getPosition().latitude + "," + marker.getPosition().longitude;
                    tvSelectLoc.setText(selectLoc);
                }
            });

            mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Toast.makeText(LocationActivity.this, "위도: " + latLng.latitude +
                            "\n경도: " + latLng.longitude, Toast.LENGTH_SHORT).show();
                }
            });

            mGoogleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                @Override
                public void onMapLongClick(LatLng latLng) {
                    MarkerOptions options = new MarkerOptions();
                    options.position(latLng);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                    Marker newMarker = mGoogleMap.addMarker(options);

                    selectLoc =  latLng.latitude + "," + latLng.longitude;
                    tvSelectLoc.setText(selectLoc);
                }
            });
        }
    };

    private Location checkLastLocation() {
        Location lastLoc = null;
        if (checkPermission()) {
            lastLoc = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
        }
        return lastLoc;
    }

    private boolean checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQ_LOC);
                return false;
            } else
                return true;
        }
        return false;
    }


    /*권한승인 요청에 대한 사용자의 응답 결과에 따른 수행*/
    // requestCode <- MY_PERMISSIONS_REQ_LOC
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQ_LOC) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 퍼미션 획득 - 맵 로딩 실행
                locationUpate();
            } else {
                // 퍼미션 미획득
                Toast.makeText(this, "Permissions are not granted.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}