package nanfei.api;

import android.app.Service;
import android.content.Context;
import android.net.ConnectivityManager;

public class ConnectivityDetector {
    Context context;

    public ConnectivityDetector(Context context) {
        this.context = context;
    }

    public boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Service.CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo()!=null ) {
            return true;
        }
        else
            return false;
    }
}
