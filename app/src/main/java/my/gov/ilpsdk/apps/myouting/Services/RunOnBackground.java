package my.gov.ilpsdk.apps.myouting.Services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import my.gov.ilpsdk.apps.myouting.Utils.NotificationHelper;

public class RunOnBackground extends Service {
    //@androidx.annotation.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {

        super.onStart(intent, startId);
    }
}
