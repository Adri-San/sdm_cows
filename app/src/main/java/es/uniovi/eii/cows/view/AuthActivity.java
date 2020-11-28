package es.uniovi.eii.cows.view;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import es.uniovi.eii.cows.R;

public class AuthActivity extends AppCompatActivity {

	private static final int RC_SIGN_IN = 9001;
	private static final String TAG = "GoogleActivity";

	private FirebaseAuth firebase;
	private GoogleSignInClient client;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		// Configure the Google Auth Options
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.requestProfile()
				.build();
		client = GoogleSignIn.getClient(this, gso);
		// Get the instance of FirebaseAuth
		firebase = FirebaseAuth.getInstance();

		Button btnAuth = findViewById(R.id.btnAuth);
		btnAuth.setOnClickListener(view -> signIn());
	}

	@Override
	public void onStart() {
		super.onStart();
		// Check if user is signed in (non-null) and update UI accordingly.
		FirebaseUser currentUser = firebase.getCurrentUser();
		if (currentUser != null) {
			goToMainActivity();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			try {
				// Google Sign In was successful, authenticate with Firebase
				GoogleSignInAccount account = task.getResult(ApiException.class);
				assert account != null;
				Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
				firebaseAuthWithGoogle(account.getIdToken());
			} catch (ApiException e) {
				// Google Sign In failed, update UI appropriately
				Log.w(TAG, "Google sign in failed", e);
				Toast.makeText(this, R.string.error_logging_in_google, Toast.LENGTH_LONG).show();
			}
		}
	}

	private void signIn() {
		Intent signInIntent = client.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	private void firebaseAuthWithGoogle(String idToken) {
		AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
		firebase.signInWithCredential(credential)
				.addOnCompleteListener(this, task -> {
					if (task.isSuccessful()) {
						// Sign in success, update UI with the signed-in user's information
						Log.d(TAG, "signInWithCredential:success");
						FirebaseUser user = firebase.getCurrentUser();
						goToMainActivity();
					} else {
						// If sign in fails, display a message to the user.
						Log.w(TAG, "signInWithCredential:failure", task.getException());
						Toast.makeText(this, R.string.error_logging_in, Toast.LENGTH_LONG).show();
					}
				});
	}

	private void goToMainActivity() {
		// Animation
		ActivityOptions animation = ActivityOptions.makeCustomAnimation(this,
				android.R.anim.fade_in, android.R.anim.fade_out);
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent, animation.toBundle());
		finish(); // don't return to this activity
	}
}