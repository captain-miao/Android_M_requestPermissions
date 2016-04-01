package com.example.captain_miao.permissions;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.captain_miao.grantap.CheckAnnotatePermission;
import com.example.captain_miao.grantap.CheckPermission;
import com.example.captain_miao.grantap.annotation.PermissionCheck;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.example.captain_miao.grantap.utils.PermissionUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "MainActivity";



    String[] normalPermission = new String[]{Manifest.permission.INTERNET};

    String[] dangerousPermissionCamera = new String[]{Manifest.permission.CAMERA};
    @PermissionCheck()
    String[] dangerousPermissionLocation = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};

    //SYSTEM_ALERT_WINDOW write permission
    String[] systemAlertWindowPermission = new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW};
        //system settings write permission
    String[] settingsPermission = new String[]{Manifest.permission.WRITE_SETTINGS};

    TextView mTvPermissionInternet;
    TextView mTvPermissionCamera;
    TextView mTvPermissionLocation;
    TextView mTvPermissionSystem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mTvPermissionInternet = (TextView) findViewById(R.id.tv_permission_internet);
        mTvPermissionCamera = (TextView) findViewById(R.id.tv_permission_camera);
        mTvPermissionLocation = (TextView) findViewById(R.id.tv_permission_location);
        mTvPermissionSystem = (TextView) findViewById(R.id.tv_permission_system);
        findViewById(R.id.btn_request_permission_all).setOnClickListener(this);
        findViewById(R.id.btn_request_permission_internet).setOnClickListener(this);
        findViewById(R.id.btn_request_permission_camera).setOnClickListener(this);
        findViewById(R.id.btn_request_permission_location).setOnClickListener(this);
        findViewById(R.id.btn_request_permission_system).setOnClickListener(this);
        findViewById(R.id.btn_open_permission_setting).setOnClickListener(this);
        findViewById(R.id.btn_open_battery_optimization_settings).setOnClickListener(this);

        updatePermissionStatus();
    }



    @SuppressWarnings("deprecation")
    private void updatePermissionStatus() {
        boolean isGranted = PermissionUtils.hasSelfPermissions(this, normalPermission);
        mTvPermissionInternet.setText(getString(R.string.normal_permission_status, isGranted ? "Granted" : "Denied"));
        mTvPermissionInternet.setTextColor(getResources().getColor(isGranted ? R.color.green : R.color.red));

        isGranted = PermissionUtils.hasSelfPermissions(this, dangerousPermissionCamera);
        mTvPermissionCamera.setText(getString(R.string.dangerous_permission_status, isGranted ? "Granted" : "Denied"));
        mTvPermissionCamera.setTextColor(getResources().getColor(isGranted ? R.color.green : R.color.red));

        isGranted = PermissionUtils.hasSelfPermissions(this, dangerousPermissionLocation);
        mTvPermissionLocation.setText(getString(R.string.dangerous_permission_status, isGranted ? "Granted" : "Denied"));
        mTvPermissionLocation.setTextColor(getResources().getColor(isGranted ? R.color.green : R.color.red));

        isGranted = PermissionUtils.hasSelfPermissions(this, systemAlertWindowPermission);
        mTvPermissionSystem.setText(getString(R.string.system_permission_status, isGranted ? "Granted" : "Denied"));
        mTvPermissionSystem.setTextColor(getResources().getColor(isGranted ? R.color.green : R.color.red));
    }

    @PermissionGranted()
    public void permissionGranted() {
        Toast.makeText(this, "ACCESS_COARSE_LOCATION Permission Granted", Toast.LENGTH_SHORT).show();
        updatePermissionStatus();
    }
    @PermissionDenied()
    public void permissionDenied() {
        Toast.makeText(this, "ACCESS_COARSE_LOCATION Permission Denied", Toast.LENGTH_SHORT).show();
        updatePermissionStatus();
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_request_permission_all:
                CheckPermission
                        .from(this)
                        .setPermissions(dangerousPermissionCamera[0], dangerousPermissionLocation[0],systemAlertWindowPermission[0], systemAlertWindowPermission[0])
                        .setRationaleConfirmText("request all permission")
                        .setDeniedMsg("the permission denied")
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void permissionGranted() {
                                Toast.makeText(MainActivity.this, "all permission granted", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }

                            @Override
                            public void permissionDenied() {
                                Toast.makeText(MainActivity.this, "permission denied.", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }
                        })
                        .check();
                break;
            case R.id.btn_request_permission_camera:
                CheckPermission
                        .from(this)
                        .setPermissions(dangerousPermissionCamera)
                        .setRationaleConfirmText("Request Camera Permission")
                        .setDeniedMsg("The Camera Permission Denied")
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void permissionGranted() {
                                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }

                            @Override
                            public void permissionDenied() {
                                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }
                        })
                        .check();
                break;
            case R.id.btn_request_permission_location:
                CheckAnnotatePermission
                        .from(this, this)
                        .check();
                break;
            case R.id.btn_request_permission_system:
                CheckPermission
                        .from(this)
                        .setPermissions(systemAlertWindowPermission)
                        .setRationaleConfirmText("Request SYSTEM_ALERT_WINDOW")
                        .setDeniedMsg("The SYSTEM_ALERT_WINDOW Denied")
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void permissionGranted() {
                                Toast.makeText(MainActivity.this, "SYSTEM_ALERT_WINDOW Permission Granted", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }

                            @Override
                            public void permissionDenied() {
                                Toast.makeText(MainActivity.this, "SYSTEM_ALERT_WINDOW Permission Denied", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }
                        })
                        .check();
                break;
            case R.id.btn_request_permission_internet:
                CheckPermission
                        .from(this)
                        .setPermissions(normalPermission)
                        .setRationaleConfirmText("Request INTERNET")
                        .setDeniedMsg("The INTERNET Denied")
                        .setPermissionListener(new PermissionListener() {
                            @Override
                            public void permissionGranted() {
                                Toast.makeText(MainActivity.this, "INTERNET Permission Granted", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }

                            @Override
                            public void permissionDenied() {
                                Toast.makeText(MainActivity.this, "INTERNET Permission Denied", Toast.LENGTH_SHORT).show();
                                updatePermissionStatus();
                            }
                        })
                        .check();
                break;
            case R.id.btn_open_permission_setting:
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivity(intent);
                break;
            case R.id.btn_open_battery_optimization_settings:
                if(PermissionUtils.isOverMarshmallow()) {
                    Intent batteryIntent = new Intent();
                    String packageName = getPackageName();
                    PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                    if (pm.isIgnoringBatteryOptimizations(packageName))
                        batteryIntent.setAction(Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS);
                    else {
                        batteryIntent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                        batteryIntent.setData(Uri.parse("package:" + packageName));
                    }

                    startActivity(batteryIntent);
                } else {
                    Toast.makeText(MainActivity.this, "battery optimizations on Android6.0", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
