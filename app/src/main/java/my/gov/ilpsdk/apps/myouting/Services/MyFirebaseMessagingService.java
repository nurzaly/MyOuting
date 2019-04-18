package my.gov.ilpsdk.apps.myouting.Services;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import my.gov.ilpsdk.apps.myouting.Utils.NotificationHelper;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() != null){
            String title = remoteMessage.getData().get("title");
            String body = remoteMessage.getData().get("body");

            NotificationHelper.displayNotification(getApplicationContext(),title,body);
        }
    }
}
