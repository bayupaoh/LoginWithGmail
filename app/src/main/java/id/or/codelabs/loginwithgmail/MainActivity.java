package id.or.codelabs.loginwithgmail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;

    GoogleSignInOptions googleSignInOptions;
    GoogleApiClient googleApiClient;

    SignInButton signInButton;
    Button logout;
    TextView displayName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        signInButton = (SignInButton) findViewById(R.id.signInButton);
        logout = (Button) findViewById(R.id.logOut);
        displayName = (TextView) findViewById(R.id.txtNama);

        displayName.setVisibility(View.INVISIBLE);
        logout.setVisibility(View.GONE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(getApplicationContext(),connectionResult.getErrorMessage(),Toast.LENGTH_LONG).show();

    }

    public void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            GoogleSignInResult hasil = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(hasil);
        }
    }

    public void handleSignInResult(GoogleSignInResult result){
        if(result.isSuccess()){
            GoogleSignInAccount akun = result.getSignInAccount();;

            String nama = akun.getDisplayName();
            Toast.makeText(getApplicationContext(),nama,Toast.LENGTH_LONG).show();
            displayName.setVisibility(View.VISIBLE);
            displayName.setText(nama);
            logout.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.INVISIBLE);
        }else {
            Toast.makeText(getApplicationContext(),"Gagal Login",Toast.LENGTH_LONG).show();
        }

    }

    public void logout(){
        Auth.GoogleSignInApi.revokeAccess(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                Toast.makeText(getApplicationContext(),"Logout",Toast.LENGTH_LONG).show();
                logout.setVisibility(View.GONE);
                logout.setVisibility(View.INVISIBLE);
                signInButton.setVisibility(View.VISIBLE);
            }
        });
    }
}
