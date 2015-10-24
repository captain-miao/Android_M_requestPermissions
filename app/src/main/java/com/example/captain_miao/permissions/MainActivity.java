package com.example.captain_miao.permissions;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    String[] normalPermission = new String[]{Manifest.permission.INTERNET};
    String[] dangerousPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    String[] settingsPermission = new String[]{Manifest.permission.WRITE_SETTINGS};


    /**
     * Id to identify a permission request.
     */
    private static final int ACCESS_COARSE_LOCATION_REQUEST_CODE = 7;
    private static final int WRITE_SETTINGS_REQUEST_CODE = 8;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    //dangerous permission
    public void showRequestPermissionAccessCoarseLocation(View view) {
        Log.i(TAG, "Checking permission.");
        boolean hasSelfPermission = PermissionUtils.hasSelfPermissions(this, dangerousPermission);
        if(hasSelfPermission) {
            new AlertDialog.Builder(this)
                    .setTitle("showPermissionLocation")
                    .setMessage(settingsPermission[0] + "\r\n" + "granted")
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
        } else {
            boolean showRationale = PermissionUtils.shouldShowRequestPermissionRationale(this, dangerousPermission);
            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example if the user has previously denied the permission.
            if(showRationale){
                new AlertDialog.Builder(this)
                        .setTitle("needs location access")
                        .setMessage("Please grant location access :-)")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(dangerousPermission, ACCESS_COARSE_LOCATION_REQUEST_CODE);
                            }
                        })
                        .show();
            } else {
                //Build.VERSION.SDK_INT >= 23 Activity and Fragment use requestPermissions()
                requestPermissions(dangerousPermission, ACCESS_COARSE_LOCATION_REQUEST_CODE);
                // or
                //ActivityCompat.requestPermissions(this,
                //                            dangerousPermission,
                //                            ACCESS_COARSE_LOCATION_REQUEST_CODE);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ACCESS_COARSE_LOCATION_REQUEST_CODE: {

                boolean hasSelfPermission = PermissionUtils.verifyPermissions(grantResults);
                new AlertDialog.Builder(this)
                        .setTitle("showPermissionLocation")
                        .setMessage(dangerousPermission[0] + "\r\n" + (hasSelfPermission ? "granted" : "not granted"))
                        .setPositiveButton(android.R.string.ok, null)
                        .show();

            }
            break;
        }

    }



    //normal permission
    public void showPermissionInternet(View view) {
        boolean hasSelfPermission = PermissionUtils.hasSelfPermissions(this, normalPermission);

        new AlertDialog.Builder(this)
                .setTitle("showPermissionInternet")
                .setMessage(normalPermission[0] + "\r\n" + (hasSelfPermission ? "granted" : "not granted"))
                .setPositiveButton(android.R.string.ok, null)
                .show();
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
                    .setTitle("showPermissionInternet")
                    .setMessage(settingsPermission[0] + "\r\n" + (hasSelfPermission ? "granted" : "not granted"))
                    .setPositiveButton(android.R.string.ok, null)
                    .show();

        }
    }
}
