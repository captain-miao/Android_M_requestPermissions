package com.example.captain_miao.permissions;

import android.Manifest;
import android.app.AlertDialog;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.example.captain_miao.grantap.CheckAnnotatePermission;
import com.example.captain_miao.grantap.CheckPermission;
import com.example.captain_miao.grantap.annotation.PermissionCheck;
import com.example.captain_miao.grantap.annotation.PermissionDenied;
import com.example.captain_miao.grantap.annotation.PermissionGranted;
import com.example.captain_miao.grantap.listeners.PermissionListener;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";


    String[] dangerousPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.CAMERA};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //dangerous permission
    //check permission use CheckPermission
    public void showRequestPermissionAccessCoarseLocation(View view) {
        CheckPermission
                .from(this)
                .setPermissions(dangerousPermission)
                .setRationaleConfirmText("Request ACCESS_COARSE_LOCATION")
                .setDeniedMsg("The ACCESS_COARSE_LOCATION Denied")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void permissionGranted() {
                        Toast.makeText(MainActivity.this, "ACCESS_COARSE_LOCATION Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionDenied() {
                        Toast.makeText(MainActivity.this, "ACCESS_COARSE_LOCATION Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                })
                .check();

        Toast.makeText(this, PermissionChecker.checkSelfPermission(this, Manifest.permission.CAMERA) + "", Toast.LENGTH_SHORT).show();

    }




    //normal permission
    //check permission use CheckAnnotatePermission
    @PermissionCheck()
    String[] normalPermission = new String[]{Manifest.permission.INTERNET};
    public void showPermissionInternet(View view) {
        CheckAnnotatePermission
                .from(this, this)
                .check();
    }
    @PermissionGranted()
    public void permissionGranted() {
        Toast.makeText(this, "INTERNET Permission Granted", Toast.LENGTH_SHORT).show();
    }
    @PermissionDenied()
    public void permissionDenied() {
        Toast.makeText(this, "INTERNET Permission Denied", Toast.LENGTH_SHORT).show();
    }


    //system settings write permission
    String[] settingsPermission = new String[]{Manifest.permission.WRITE_SETTINGS};
    private static final int WRITE_SETTINGS_REQUEST_CODE = 8;
    public void showRequestPermissionWriteSettings(View view) {
        // for Settings.ACTION_MANAGE_WRITE_SETTINGS: Settings.System.canWrite
        // CommonsWare's blog post:https://commonsware.com/blog/2015/08/17/random-musings-android-6p0-sdk.html
        boolean hasSelfPermission = Settings.System.canWrite(this);
        if(hasSelfPermission) {
            new AlertDialog.Builder(this)
                    .setTitle("showPermissionWriteSettings")
                    .setMessage(settingsPermission[0] + "\r\n" + "granted")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        } else {
            CheckPermission
                    .from(this)
                    .setPermissions(dangerousPermission)
                    .setRationaleConfirmText("Request WRITE_SETTINGS")
                    .setDeniedMsg("The WRITE_SETTINGS Denied")
                    .setPermissionListener(new PermissionListener() {
                        @Override
                        public void permissionGranted() {
                            Toast.makeText(MainActivity.this, "WRITE_SETTINGS Permission Granted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void permissionDenied() {
                            Toast.makeText(MainActivity.this, "WRITE_SETTINGS Permission Denied", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .check();
        }
    }

    //SYSTEM_ALERT_WINDOW write permission
    String[] systemAlertWindowPermission = new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW};
    public void showRequestPermissionAlertWindow(View view) {
        CheckPermission
                .from(this)
                .setPermissions(systemAlertWindowPermission)
                .setRationaleConfirmText("Request SYSTEM_ALERT_WINDOW")
                .setDeniedMsg("The SYSTEM_ALERT_WINDOW Denied")
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void permissionGranted() {
                        Toast.makeText(MainActivity.this, "SYSTEM_ALERT_WINDOW Permission Granted", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void permissionDenied() {
                        Toast.makeText(MainActivity.this, "SYSTEM_ALERT_WINDOW Permission Denied", Toast.LENGTH_SHORT).show();
                    }
                })
                .check();
    }

}
