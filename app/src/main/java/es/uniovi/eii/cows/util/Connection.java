package es.uniovi.eii.cows.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Connection {
    private Context context;

    public Connection(Context context){
        this.context = context;
    }

    public boolean checkConnection(){
        boolean connected;

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        connected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        return connected;
    }
}
