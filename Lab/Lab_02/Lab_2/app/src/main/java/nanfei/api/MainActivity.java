package nanfei.api;

import android.net.ConnectivityManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.net.wifi.*;
import android.content.*;
import java.util.List;
import android.net.wifi.WifiInfo;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import static android.content.Context.WIFI_SERVICE;

public class MainActivity extends AppCompatActivity {

    WifiManager wifiManager;
    List<ScanResult> list;
    ConnectivityDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button1 = (Button) findViewById(R.id.button);

        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                init();
            }
        });
    }

    public void init() {
        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        cd = new ConnectivityDetector(this);
        if(!wifiManager.isWifiEnabled() && wifiManager.getWifiState() != wifiManager.WIFI_STATE_ENABLING){
            wifiManager.setWifiEnabled(true);
        }
//        wifiManager.getWifiState()==WifiManager.WIFI_STATE_ENABLED)

        list = wifiManager.getScanResults();
        sort(list);

        final ListView listView = (ListView) findViewById(R.id.listView);

        if (list == null) {
            Toast.makeText(this, "wifi closed！", Toast.LENGTH_LONG).show();
        } else {
            listView.setAdapter(new MyAdapter(this, list));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int selectedItemID = i;
                final ScanResult item = list.get(i);
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.create().show();
                View mView = getLayoutInflater().inflate(R.layout.dialog, null);
                final EditText mPassword = (EditText) mView.findViewById(R.id.password);
                final EditText mUsername = (EditText) mView.findViewById(R.id.username);
                Button mConnect = (Button) mView.findViewById(R.id.ConnectBtn);
                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                if(item.capabilities.toLowerCase().contains("eap")){
                    mUsername.setVisibility(View.VISIBLE);
                }
                else
                    mUsername.setVisibility(View.INVISIBLE);
                dialog.show();
                mConnect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String passwd = mPassword.getText().toString();
                        String uname = mUsername.getText().toString();
                        if(!passwd.isEmpty()){
                            ConnectWifi(selectedItemID, uname, passwd);

                            new CountDownTimer(5000, 1000){
                                @Override
                                public void onTick(long l) {

                                }
                                @Override
                                public void onFinish() {
                                    if(ConnectionCheck()) {
                                        int ip =wifiManager.getConnectionInfo().getIpAddress();
                                        String ipAddress = Integer.toString(ip);
                                        Toast.makeText(getApplicationContext(), "Connect successfully to network: "+item.SSID+"\nIP address: "+ipAddress, Toast.LENGTH_LONG).show();
                                        dialog.dismiss();
                                    }
                                    else{
                                        Toast.makeText(getApplicationContext(), "Wrong input.", Toast.LENGTH_SHORT).show();
                                        dialog.show();
                                    }
                                }
                            }.start();
                        }
                        else
                            Toast.makeText(getApplicationContext(), "Please enter the password.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    public boolean ConnectionCheck(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(CONNECTIVITY_SERVICE);
        if(connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()){
            return true;
        }
        else
            return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public void ConnectWifi(int i, String u_name, String password){
        ScanResult selectedItem = list.get(i);
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        if(selectedItem.capabilities.toLowerCase().contains("eap")){
            WifiEnterpriseConfig enterpriseConfig = new WifiEnterpriseConfig();
            enterpriseConfig.setIdentity(u_name);
            enterpriseConfig.setPassword(password);
            enterpriseConfig.setEapMethod(WifiEnterpriseConfig.Eap.PEAP);
            config.SSID = "\"" + selectedItem.SSID + "\"";
            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);

            config.enterpriseConfig = enterpriseConfig;
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
            wifiManager.saveConfiguration();
            int netID1 = wifiManager.addNetwork(config);
//            wifiManager.disconnect();
            wifiManager.enableNetwork(netID1, true);
//            wifiManager.reconnect();
            Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
        }
        else{
            config.SSID = "\""+ selectedItem.SSID +"\"";

            config.preSharedKey = "\"" + password + "\"";

            config.status = WifiConfiguration.Status.ENABLED;
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            wifiManager.saveConfiguration();
            int netID2 = wifiManager.addNetwork(config);
//        Toast.makeText(getApplicationContext(), "Wifi Selected : "+selectedItem.SSID, Toast.LENGTH_LONG).show();
            wifiManager.disconnect();
            wifiManager.enableNetwork(netID2, true);
            wifiManager.reconnect();
            Toast.makeText(getApplicationContext(), "Connecting...", Toast.LENGTH_SHORT).show();
        }

    }

    private void sort(List<ScanResult> list) {
        for (int i = 0; i < list.size(); i++)
            for (int j = 0; j < list.size(); j++) {
                if (list.get(i).level > list.get(j).level) {
                        ScanResult temp = null;
                        temp = list.get(i);
                        list.set(i, list.get(j));
                        list.set(j, temp);

                }
            }
    }



    public class MyAdapter extends BaseAdapter {

        LayoutInflater inflater;
        List<ScanResult> list;

        public MyAdapter(Context context, List<ScanResult> list) {
            // TODO Auto-generated constructor stub
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            View view = null;
            view = inflater.inflate(R.layout.wifi, null);
            ScanResult scanResult = list.get(position);
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(scanResult.SSID);
            TextView signalStrenth = (TextView) view.findViewById(R.id.signal_strenth);
            signalStrenth.setText(String.valueOf((scanResult.level)));

            return view;
        }
    }
}
