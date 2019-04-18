package my.gov.ilpsdk.apps.myouting;

import android.app.Application;
import android.content.Intent;

import my.gov.ilpsdk.apps.myouting.Services.RunOnBackground;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //startService(new Intent(this, RunOnBackground.class));
    }
}
