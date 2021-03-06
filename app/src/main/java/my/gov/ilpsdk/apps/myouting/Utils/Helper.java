package my.gov.ilpsdk.apps.myouting.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.error.ANError;

public final class Helper {

    private static Toast toast;

    public static void error_connection(Context context, ANError error, String tag){
        if(error.getErrorDetail() == "connectionError"){
            Toast.makeText(context,"Connection Error", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(context,"Server Error", Toast.LENGTH_SHORT).show();
            Log.d(tag, "error_connection: " + error.getErrorBody());
            Log.d(tag, "error_connection: " + error.getErrorDetail());
        }
    }
    public static void showToast(Context context, String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    public static void showToastLong(Context context, String message) {
        if (toast != null) {
            toast.cancel();
            toast = null;
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        toast.show();
    }

}
