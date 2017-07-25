
package com.aceturtle.logutil;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.LocationListener;

import java.util.List;

public class GeofenceIntentService extends IntentService implements LocationListener {


    private BluetoothAdapter mBluetoothAdapter;
    private int enterInt, exitInt;
    private Notification notification;
    private String location_id;
    private String location_name = null;
    private final String TAG = this.getClass().getCanonicalName();

    public GeofenceIntentService() {
        super("GeofenceIntentService");
        Log.v(TAG, "Constructor.");
    }

    public void onCreate() {
        super.onCreate();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Log.v(TAG, "onCreate");
    }

    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            bluetoothNotAvailable();
        } else {
            GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
            Log.v(TAG, "onHandleIntent");
            if (!geofencingEvent.hasError()) {
                int transition = geofencingEvent.getGeofenceTransition();
                String notificationTitle;
                switch (transition) {
                    case Geofence.GEOFENCE_TRANSITION_ENTER:
                        notificationTitle = "Geofence Intent Entered";
                        Log.v(TAG, "Geofence Entered");
                        mBluetoothAdapter.enable();
                        break;
                    case Geofence.GEOFENCE_TRANSITION_DWELL:
                        notificationTitle = "Geofence Intent Dwell";
                        Log.v(TAG, "Dwelling in Geofence");
                        break;
                    case Geofence.GEOFENCE_TRANSITION_EXIT:
                        notificationTitle = "Geofence Intent Exit";
                        Log.v(TAG, "Geofence Exited");
                        mBluetoothAdapter.disable();
                        break;
                    default:
                        notificationTitle = "Geofence Unknown";
                }
                sendNotification(this, getTriggeringGeofences(intent), notificationTitle);
            }
        }
    }

    private void sendNotification(Context context, String notificationText,
                                  String notificationTitle) {

        PowerManager pm = (PowerManager) context
                .getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(
                PowerManager.PARTIAL_WAKE_LOCK, "");
        wakeLock.acquire();
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        int iUniqueId = (int) (System.currentTimeMillis() & 0xfffffff);
        // Prepare intent which is triggered if the notification is selected
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ace_logo);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.ace_logo);
        builder.setLargeIcon(bitmap);
        builder.setContentTitle(notificationTitle);
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText));
        builder.setContentText(notificationText);
        builder.setAutoCancel(true);
        builder.setSound(soundUri);
        builder.setDefaults(Notification.DEFAULT_ALL).setAutoCancel(true);
        notification = builder.build();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(iUniqueId, notification);

        wakeLock.release();
    }

    private String getTriggeringGeofences(Intent intent) {
        String geodata = null;
		/*Triggering geofence event from intent */
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        List<Geofence> geofences = geofencingEvent.getTriggeringGeofences();
        String[] geofenceIds = new String[geofences.size()];
        geofenceIds = new String[geofences.size()];
        for (int i = 0; i < geofences.size(); i++) {
            geofenceIds[i] = geofences.get(i).getRequestId();
            geodata = geofenceIds[i];
            if (geodata != null)
                getTriggeringLocationId(geodata);
        }
        return getTriggeringLocationId(geodata);

    }
    /* Split gettrigger data and return location name */
    private String getTriggeringLocationId(String geofenceId) {
        String[] locationid = geofenceId.split("#");
        location_name = locationid[0];
        location_id = locationid[1];
        //sessionManager.storeLoctionId(location_id);
        return location_name;
    }

    private void bluetoothNotAvailable() {

		 /* mBluetoothStatusBtn.setImageResource(R.drawable.grey_button);
          mBluetoothStatusText.setText("Bluetooth is not available");
		  mBluetoothStatusBtn.setEnabled(false);
		*/
        //Toast.makeText(context.getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_LONG).show();
    }

    private void bluetoothEnabled() {

		/* mBluetoothStatusBtn.setImageResource(R.drawable.green_button);
		 mBluetoothStatusText.setText("Bluetooth is ON");*/

        //Toast.makeText(context.getApplicationContext(), "Bluetooth is ON", Toast.LENGTH_LONG).show();
    }

    private void bluetoothDisabled() {

		  /*mBluetoothStatusBtn.setImageResource(R.drawable.red_button);
		  mBluetoothStatusText.setText("Bluetooth is OFF");*/

        //	Toast.makeText(context.getApplicationContext(), "Bluetooth is OFF", Toast.LENGTH_LONG).show();
    }

    /* Bluetooth turn on automatically and start beacon service to monitor*/
    private void BeaconService() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        } else if (mBluetoothAdapter.isEnabled()) {
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }
}
