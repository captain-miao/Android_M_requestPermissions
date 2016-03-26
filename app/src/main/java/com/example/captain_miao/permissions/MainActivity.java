package com.example.captain_miao.permissions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
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


    String[] dangerousPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    String[] settingsPermission = new String[]{Manifest.permission.WRITE_SETTINGS};


    private static final int WRITE_SETTINGS_REQUEST_CODE = 8;


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


    //system settings permission
    public void showRequestPermissionWriteSettings(View view) {
        // for Settings.ACTION_MANAGE_WRITE_SETTINGS: Settings.System.canWrite
        // CommonsWare's blog post:https://commonsware.com/blog/2015/08/17/random-musings-android-6p0-sdk.html
        boolean hasSelfPermission = PermissionUtils.hasSelfPermissions(this, settingsPermission);
        // or
        // boolean hasSelfPermission = Settings.System.canWrite(this);
        if(hasSelfPermission) {
            new AlertDialog.Builder(this)
                    .setTitle("showPermissionWriteSettings")
                    .setMessage(settingsPermission[0] + "\r\n" + "granted")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        } else {
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, WRITE_SETTINGS_REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WRITE_SETTINGS_REQUEST_CODE) {
            boolean hasSelfPermission = PermissionUtils.hasSelfPermissions(this, settingsPermission);
            new AlertDialog.Builder(this)
                    .setTitle("showPermissionWRITE_SETTINGS")
                    .setMessage(settingsPermission[0] + "\r\n" + (hasSelfPermission ? "granted" : "not granted"))
                    .setPositiveButton(android.R.string.ok, null)
                    .show();

        }
    }
}
