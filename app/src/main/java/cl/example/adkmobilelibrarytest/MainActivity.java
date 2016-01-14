package cl.example.adkmobilelibrarytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import cl.niclabs.adkmobile.monitor.Connectivity;
import cl.niclabs.adkmobile.monitor.Location;
import cl.niclabs.adkmobile.monitor.Monitor;
import cl.niclabs.adkmobile.monitor.Screen;
import cl.niclabs.adkmobile.monitor.Telephony;
import cl.niclabs.adkmobile.monitor.Traffic;
import cl.niclabs.adkmobile.monitor.data.ConnectivityObservation;
import cl.niclabs.adkmobile.monitor.data.LocationObservation;
import cl.niclabs.adkmobile.monitor.data.StateChange;
import cl.niclabs.adkmobile.monitor.data.TelephonyObservation;
import cl.niclabs.adkmobile.monitor.data.TrafficObservation;
import cl.niclabs.adkmobile.monitor.listeners.ConnectivityListener;
import cl.niclabs.adkmobile.monitor.listeners.LocationListener;
import cl.niclabs.adkmobile.monitor.listeners.ScreenListener;
import cl.niclabs.adkmobile.monitor.listeners.TelephonyListener;
import cl.niclabs.adkmobile.monitor.listeners.TrafficListener;

public class MainActivity extends AppCompatActivity implements TrafficListener, ConnectivityListener, ScreenListener, TelephonyListener, LocationListener{

    private Monitor.Controller<TrafficListener> trafficController;
    private Monitor.Controller<ConnectivityListener> connController;
    private Monitor.Controller<ScreenListener> screenController;
    private Monitor.Controller<TelephonyListener> phoneController;
    private Monitor.Controller<LocationListener> locationController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*  Control de Tráfico  */
        // Asociar el controller al contexto y activarlo
        trafficController = Traffic.bind(Traffic.class, this);
        trafficController.listen(this, true);
        // Frecuencia de muestreo cada X segundos
        Bundle bundle = new Bundle();
        bundle.putInt(Traffic.TRAFFIC_UPDATE_INTERVAL_EXTRA, 1);
        // Activar
        trafficController.activate(Monitor.TRAFFIC_MOBILE | Monitor.TRAFFIC_WIFI | Monitor.TRAFFIC_APPLICATION, bundle);

        /* Control de Conectividad */
        // Asociar el controller al contexto y activarlo
        connController = Connectivity.bind(Connectivity.class, this);
        connController.listen(this, true);
        // Activar
        connController.activate(Monitor.CONNECTIVITY);

        /* Control de Pantalla */
        screenController = Screen.bind(Screen.class, this);
        screenController.listen(this, true);
        // Activar
        screenController.activate(Monitor.SCREEN);

        /* Control de telefono */
        phoneController = Telephony.bind(Telephony.class, this);
        phoneController.listen(this, true);
        // Activar
        phoneController.activate(Monitor.TELEPHONY);

        /* Control de Locación */
        locationController = Location.bind(Location.class, this);
        locationController.listen(this, true);
        // Activar
        locationController.activate(Monitor.LOCATION_NETWORK);

        /* Visual */
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        trafficController.unbind();
        connController.unbind();
        screenController.unbind();
        phoneController.unbind();
        locationController.unbind();
    }

    /* Métodos de TrafficListener */
    @Override
    public void onMobileTrafficChange(TrafficObservation trafficState) {
        setDataTextView(R.id.tv_mobile, trafficState.toString());
    }

    @Override
    public void onWiFiTrafficChange(TrafficObservation trafficState) {
        setDataTextView(R.id.tv_wifi, trafficState.toString());
    }

    @Override
    public void onApplicationTrafficChange(TrafficObservation trafficState) {
        setDataTextView(R.id.tv_app, trafficState.toString());
    }

    /* Métodos de ConnectivityListener */
    @Override
    public void onConnectivityChange(ConnectivityObservation connectivityState) {
        setDataTextView(R.id.tv_conn, connectivityState.toString());
    }

    /* Métodos de ScreenListener */
    @Override
    public void onScreenStateChange(StateChange screenState) {
        appendDataTextView(R.id.tv_screen, screenState.toString());
    }

    /* Métodos de TelephonyListener */
    @Override
    public void onMobileTelephonyChange(TelephonyObservation<?> telephonyState) {
        setDataTextView(R.id.tv_phone_obs, telephonyState.toString());
    }

    @Override
    public void onSimStateChange(StateChange stateChange) {
        appendDataTextView(R.id.tv_phone_sim, stateChange.toString());
    }

    @Override
    public void onServiceStateChange(StateChange stateChange) {
        appendDataTextView(R.id.tv_phone_service, stateChange.toString());
    }

    @Override
    public void onDataConnectionStateChange(StateChange stateChange) {
        appendDataTextView(R.id.tv_phone_dataconn, stateChange.toString());
    }

    @Override
    public void onAirplaneModeChange(StateChange stateChange) {
        appendDataTextView(R.id.tv_phone_airplane, stateChange.toString());
    }

    /* Métodos de LocationListener */

    @Override
    public void onLocationChanged(LocationObservation locationState) {
        appendDataTextView(R.id.tv_location_change, locationState.toString());
    }

    @Override
    public void onLocationStateChanged(StateChange state) {
        appendDataTextView(R.id.tv_location_state, state.toString());
    }

    /* Métodos adicionales */
    public void setDataTextView(final int idTV, final String newText){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = (TextView) findViewById(idTV);
                String text = newText;
                tv.setText(text);
            }
        });
    }

    public void appendDataTextView(final int idTV, final String newText){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TextView tv = (TextView) findViewById(idTV);
                String text = tv.getText().toString() + newText + "\n";
                tv.setText(text);
            }
        });
    }
}
