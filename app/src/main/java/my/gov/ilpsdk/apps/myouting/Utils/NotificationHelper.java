package my.gov.ilpsdk.apps.myouting.Utils;

import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import my.gov.ilpsdk.apps.myouting.R;
import my.gov.ilpsdk.apps.myouting.data.Constant;

public class NotificationHelper {
    public static void displayNotification(Context context,String title,String content){
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context, Constant.CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifcation)
                    .setContentTitle(title)
                    .setContentText(content)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationCompat = NotificationManagerCompat.from(context);
        ((NotificationManagerCompat) notificationCompat).notify(1,mBuilder.build());
    }
}
