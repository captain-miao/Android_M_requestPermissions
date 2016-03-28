# Android_M_requestPermissions
1. simple Android permission request  
2. support SYSTEM_ALERT_WINDOW and WRITE_SETTINGS permission request
  
<br/>
  
## GrantAndroidPermission library

### Gradle
Get library from  [oss.sonatype.org.io](https://oss.sonatype.org/content/repositories/snapshots)
```javascript

repositories {
    
    maven { url "https://oss.sonatype.org/content/repositories/snapshots" }

}

dependencies {
    compile compile 'com.github.captain-miao:grantap:1.0.0'
}

```
<br/>
### targetSdkVersion
```
android {
    //must set targetSdkVersion 23，otherwise checkSelfPermission() always return PERMISSION_GRANTED
    targetSdkVersion 23
}
```
<br/>

### check permission use CheckPermission
```
    //SYSTEM_ALERT_WINDOW write permission
    String[] systemAlertWindowPermission = new String[]{Manifest.permission.SYSTEM_ALERT_WINDOW};
    public void showRequestPermissionAlertWindow(View view) {
        CheckPermission
                .from(this)
                .setPackageName(getPackageName())
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
```    
    
### check permission use CheckAnnotatePermission
```
    @PermissionCheck()
    String[] normalPermission = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION};
    public void showPermissionInternet(View view) {
        CheckAnnotatePermission
                .from(this, this)
                .check();
    }
    @PermissionGranted()
    public void permissionGranted() {
        Toast.makeText(this, "ACCESS_COARSE_LOCATION Permission Granted", Toast.LENGTH_SHORT).show();
    }
    @PermissionDenied()
    public void permissionDenied() {
        Toast.makeText(this, "ACCESS_COARSE_LOCATION Permission Denied", Toast.LENGTH_SHORT).show();
    }
```

##Thanks 
* [TedPermission](https://github.com/ParkSangGwon/TedPermission) - Easy check permission library for Android Marshmallow
* [PermissionGen](https://github.com/lovedise/PermissionGen) - Android API easy to use permission for Android M




<br/><br/>


## requestPermissions Android example 
    
###  0.  three permission types
1. Normal permissions  
    the system automatically grants the permission to the app
2. Dangerous permissions  
    has to explicitly grant the permission to the app.
3. Other permissions:   
    [Random Musings on the Android 6.0 SDK](https://commonsware.com/blog/2015/08/17/random-musings-android-6p0-sdk.html)  
    
###  1. Normal permission(such as INTERNET)      
If your app lists [normal permissions](https://developer.android.com/intl/zh-cn/guide/topics/security/normal-permissions.html)
 in its manifest (that is, permissions that don't pose much risk to the user's privacy or the device's operation),
  the system automatically grants those permissions.     
    
###  2. Dangerous permissions(such as ACCESS_COARSE_LOCATION)    
[dangerous permissions](https://developer.android.com/intl/zh-cn/guide/topics/security/permissions.html#normal-dangerous) has to explicitly grant the permission to the app.  
```
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
```  
###  3. Other permissions(such as WRITE_SETTINGS)
```
    //system settings permission
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
            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS,
                            Uri.parse("package:" + getPackageName()));
                    startActivityForResult(intent, WRITE_SETTINGS_REQUEST_CODE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == WRITE_SETTINGS_REQUEST_CODE) {
            boolean hasSelfPermission = Settings.System.canWrite(this);
            new AlertDialog.Builder(this)
                    .setTitle("showPermissionInternet")
                    .setMessage(settingsPermission[0] + "\r\n" + (hasSelfPermission ? "granted" : "not granted"))
                    .setPositiveButton(android.R.string.ok, null)
                    .show();

        }
    }
```
<br/><br/>
##License 
```
Copyright 2016 captain_miao

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.```
