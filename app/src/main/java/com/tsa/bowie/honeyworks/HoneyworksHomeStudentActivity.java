package com.tsa.bowie.honeyworks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;

/**
 * Created by Kevin on 2/16/2016.
 */
public class HoneyworksHomeStudentActivity extends Activity {
    GoogleDriveHelper helper = new GoogleDriveHelper();
    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions mGoogleSignInOptions;
    int SIGN_IN_MODE_OPTIONAL = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_window);

        mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Drive.API)
                .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                .build();

        mGoogleApiClient.connect(SIGN_IN_MODE_OPTIONAL);

        Button mButtonUpload = (Button) findViewById(R.id.buttonUpload);
        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                helper.saveFileToDrive();
            }
        });

        Button mButtonLogOut = (Button) findViewById(R.id.buttonGoogleLogOut);
        mButtonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });
    }

    @Override //necessary?
    protected void onPause() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
        }
        super.onDestroy();
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        mGoogleApiClient.disconnect();
                        Intent backIntent = new Intent(HoneyworksHomeStudentActivity.this, HoneyworksLoginActivity.class);
                        startActivity(backIntent);
                        // [END_EXCLUDE]
                    }
                });
    }


}
