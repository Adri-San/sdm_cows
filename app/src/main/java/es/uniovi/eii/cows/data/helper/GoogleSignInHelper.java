package es.uniovi.eii.cows.data.helper;


import android.app.Activity;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import es.uniovi.eii.cows.R;

public class GoogleSignInHelper {

	private static GoogleSignInHelper instance;                     // Singleton

	private GoogleSignInOptions gso;								// Options

	private GoogleSignInHelper(String requestIdToken) {
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(requestIdToken)
				.requestEmail()
				.requestProfile()
				.build();
	}

	public static GoogleSignInClient getClient(@NonNull Activity context) {
		if (instance == null) {
			instance = new GoogleSignInHelper(context.getString(R.string.default_web_client_id));
		}
		return GoogleSignIn.getClient(context, instance.gso);
	}

}
