package com.example.captain_miao.grantap;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.StringRes;
import android.util.Log;

import com.example.captain_miao.grantap.listeners.PermissionListener;
import com.example.captain_miao.grantap.utils.ObjectUtils;
import com.example.captain_miao.grantap.utils.PermissionUtils;

/**
 * Copyright 2016 Ted Park
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.```
 *
 *
 * modify from https://github.com/ParkSangGwon/TedPermission
 */

public class CheckPermission {
    private static final String TAG = "CheckPermission";
    private final Context mContext;
    private PermissionListener mPermissionListener;

    private String[] mPermissions;
    private String   mRationaleConfirmText;
    private String   mRationaleMessage;

    private String   mDenyMessage;
    private String   mDeniedCloseButtonText;

    private boolean  mHasSettingBtn = false;

    public CheckPermission(Context context) {
        this.mContext = context;
    }

    public static CheckPermission from(Context context) {
        return new CheckPermission(context);
    }

    /**
     * granted or denied callback
     * @param listener
     * @return
     */
    public CheckPermission setPermissionListener(PermissionListener listener) {
        this.mPermissionListener = listener;
        return this;
    }

    /**
     * ask for permissions
     * @param permissions
     * @return
     */
    public CheckPermission setPermissions(String... permissions) {
        this.mPermissions = permissions;
        return this;
    }

    /**
     * explain to the user why your app wants the permissions
     * @param rationaleMessage
     * @return
     */

    public CheckPermission setRationaleMsg(String rationaleMessage) {
        this.mRationaleMessage = rationaleMessage;
        return this;
    }

    /**
     * explain to the user why your app wants the permissions
     * @param stringRes
     * @return
     */
    public CheckPermission setRationaleMsg(@StringRes int stringRes) {
        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for RationaleMessage");
        }
        this.mRationaleMessage = mContext.getString(stringRes);
        return this;
    }

    /**
     * The text to display in the positive button of rationale message dialog
     * @param rationaleConfirmText
     * @return
     */
    public CheckPermission setRationaleConfirmText(String rationaleConfirmText) {

        this.mRationaleConfirmText = rationaleConfirmText;
        return this;
    }
    /**
     * The text to display in the positive button of rationale message dialog
     * @param stringRes
     * @return
     */
    public CheckPermission setRationaleConfirmText(@StringRes int stringRes) {

        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for RationaleConfirmText");
        }
        this.mRationaleConfirmText = mContext.getString(stringRes);

        return this;
    }


    /**
     * when user deny permission, show deny message
     * @param denyMessage
     * @return
     */
    public CheckPermission setDeniedMsg(String denyMessage) {
        this.mDenyMessage = denyMessage;
        return this;
    }
    /**
     * when user deny permission, show deny message
     * @param stringRes
     * @return
     */
    public CheckPermission setDeniedMsg(@StringRes int stringRes) {
        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for DeniedMessage");
        }
        this.mDenyMessage = mContext.getString(stringRes);
        return this;
    }


    /**
     * The text to display in the close button of deny message dialog
     * @param stringRes
     * @return
     */
    public CheckPermission setDeniedCloseButtonText(@StringRes int stringRes) {

        if (stringRes <= 0) {
            throw new IllegalArgumentException("Invalid value for DeniedCloseButtonText");
        }
        this.mDeniedCloseButtonText = mContext.getString(stringRes);

        return this;
    }
    /**
     * The text to display in the close button of deny message dialog
     * @param deniedCloseButtonText
     * @return
     */
    public CheckPermission setDeniedCloseButtonText(String deniedCloseButtonText) {

        this.mDeniedCloseButtonText = deniedCloseButtonText;
        return this;
    }

    /**
     * when user deny permission,show the setting button
     * @param hasSettingBtn
     * @return
     */
    public CheckPermission setGotoSettingButton(boolean hasSettingBtn) {

        this.mHasSettingBtn = hasSettingBtn;
        return this;
    }


    // requestPermissions
    public void check() {

        if (mPermissionListener == null) {
            throw new NullPointerException("You must setPermissionListener() on CheckPermission");
        }

        if (ObjectUtils.isEmpty(mPermissions)) {
            throw new NullPointerException("You must setPermissions() on CheckPermission");
        }


        if (PermissionUtils.isOverMarshmallow()) {
            Log.d(TAG, "Marshmallow");
            requestPermissions();
        } else {
            Log.d(TAG, "pre Marshmallow");
            mPermissionListener.permissionGranted();
        }
    }


    private void requestPermissions( ){
        ShadowPermissionActivity.setPermissionListener(mPermissionListener);
        Intent intent = new Intent(mContext, ShadowPermissionActivity.class);
        intent.putExtra(ShadowPermissionActivity.EXTRA_PERMISSIONS, mPermissions);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_MESSAGE, mRationaleMessage);
        intent.putExtra(ShadowPermissionActivity.EXTRA_RATIONALE_CONFIRM_TEXT, mRationaleConfirmText);
        intent.putExtra(ShadowPermissionActivity.EXTRA_SETTING_BUTTON, mHasSettingBtn);
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENY_MESSAGE, mDenyMessage);
        intent.putExtra(ShadowPermissionActivity.EXTRA_DENIED_DIALOG_CLOSE_TEXT, mDeniedCloseButtonText);
        mContext.startActivity(intent);
    }

}
