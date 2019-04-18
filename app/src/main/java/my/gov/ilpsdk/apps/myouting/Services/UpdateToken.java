package my.gov.ilpsdk.apps.myouting.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.util.List;

import my.gov.ilpsdk.apps.myouting.Utils.Helper;
import my.gov.ilpsdk.apps.myouting.data.Constant;
import my.gov.ilpsdk.apps.myouting.model.Outing;

public class UpdateToken extends IntentService {
    private static final String TAG = "UpdateToken";

    public UpdateToken() {
        super("updat token");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                            //mtoken.setText(token);
                            save_token_to_database(token);
                            Log.d(TAG, "onComplete: " + token);
                        }
                        else {
                            Log.d(TAG, "onComplete: token not found");
                        }
                    }
                });
    }

    public void save_token_to_database(String token){
        AndroidNetworking.post(Constant.URL_SAVE_TOKEN)
                .addBodyParameter("token",token)
                .addBodyParameter("ndp","123123")
                .build()
                .getAsObjectList(Outing.class, new ParsedRequestListener<List<Outing>>() {
                    @Override
                    public void onResponse(List<Outing> response) {
                    }
                    @Override
                    public void onError(ANError anError) {
                        Helper.error_connection(getApplicationContext(),anError,TAG);
                    }
                });
    }
}
