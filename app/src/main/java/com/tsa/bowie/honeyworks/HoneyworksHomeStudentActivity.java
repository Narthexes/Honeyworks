package com.tsa.bowie.honeyworks;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.content.IntentSender.SendIntentException;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;

import java.io.IOException;



/**
 * Created by Kevin on 2/16/2016.
 */
public class HoneyworksHomeStudentActivity extends FragmentActivity implements ConnectionCallbacks, OnConnectionFailedListener, InfoListFragment.OnImageSelected {

    GoogleApiClient mGoogleApiClient;
    GoogleSignInOptions mGoogleSignInOptions;
    int SIGN_IN_MODE_OPTIONAL = 2;

    private static final String TAG = "StudentHomeActivity";
    private static final int REQUEST_CODE_RESOLUTION = 3;

    private Uri filePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_view);

        FloatingActionButton mButtonLogOut = (FloatingActionButton) findViewById(R.id.buttonLogout);
        mButtonLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder logoutDialog = new AlertDialog.Builder(HoneyworksHomeStudentActivity.this);
                logoutDialog.setTitle("Sign out");
                logoutDialog.setMessage("Are you sure you want to sign out?");
                logoutDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        signOut();
                    }
                });
                logoutDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                logoutDialog.show();
            }

        }
        );


        Button mButtonRefresh = (Button) findViewById(R.id.buttonRefresh);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
                                              @Override
                                              public void onClick(View view) {
                                                  getSupportFragmentManager()
                                                          .beginTransaction()
                                                          .add(R.id.root_layout, InfoListFragment.newInstance(), "fragment")
                            .commit();
                }
            }
        );

    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        finish();

                    }
                });
    }

    @Override
    public void onImageSelected(int imageId) {
        DataHolder.setBitmap(BitmapFactory.decodeResource(getResources(), imageId));
        Intent startDraw = new Intent(HoneyworksHomeStudentActivity.this, ImageDrawActivity.class);
        startActivity(startDraw);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            filePath = data.getData();
            try {
                DataHolder.setBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), filePath));
                Intent startDraw = new Intent(HoneyworksHomeStudentActivity.this, ImageDrawActivity.class);
                startActivity(startDraw);
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);
        } catch (SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            // Create the API client and bind it to an instance variable.
            // We use this instance as the callback for connection and connection
            // failures.
            // Since no account name is passed, the user is prompted to choose.
            mGoogleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
                    .requestScopes(new Scope(Scopes.DRIVE_FILE))
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, mGoogleSignInOptions)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }
        // Connect the client. Once connected, the camera is launched.
        mGoogleApiClient.connect(SIGN_IN_MODE_OPTIONAL);
    }

}
