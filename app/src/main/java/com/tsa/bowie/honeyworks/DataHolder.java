package com.tsa.bowie.honeyworks;

import android.graphics.Bitmap;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Kevin on 2/21/2016.
 */
public class DataHolder {
    private static Bitmap mBitmapToSave;

    public static Bitmap getBitmap(){
        return DataHolder.mBitmapToSave;
    }
    public static void setBitmap(Bitmap map){
        DataHolder.mBitmapToSave = map;
    }

}
