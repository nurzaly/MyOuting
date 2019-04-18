package my.gov.ilpsdk.apps.myouting;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


import java.util.List;

import my.gov.ilpsdk.apps.myouting.Services.UpdateToken;
import my.gov.ilpsdk.apps.myouting.Utils.Helper;
import my.gov.ilpsdk.apps.myouting.data.Constant;
import my.gov.ilpsdk.apps.myouting.model.Outing;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private SharedPreferences config;
    //private EditText mtoken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        config = getSharedPreferences(Constant.KEY_CONFIG,0);
        //setContentView(R.layout.activity_main);

        try{
            if(!config.getString(Constant.KEY_USER,null).isEmpty()){
                //Log.d(TAG, "onCreate: " + config.getString(Constant.KEY_USER,"user"));
                home();
            }
        }catch (Exception e){
            login();
        }

        //home();
        //updateToken();
        //login();
        //get_token();
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

    public void get_token(){
        FirebaseApp.initializeApp(this);
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if(task.isSuccessful()){
                            String token = task.getResult().getToken();
                            SharedPreferences.Editor editor = config.edit();
                            editor.putString(Constant.KEY_TOKEN,token);
                            editor.commit();
                            //Log.d(TAG, "onComplete: " + token);
                            //save_token_to_database(token);
                        }
                        else {
                            //mtoken.setText("Token not found");
                            //cTIMBuQc3R8:APA91bERhj_frBg1QDib5LniL87VuXICQ5YWwYj14pgWHZrQo3xrsfApjXhaY_-3OEa3zAHzRWMogY57Lt5sy950osYiBkyUmozQqPZC81MU_FQ-uNn8vmYNbavC8UQGBf1CW9IQlKix
                        }
                    }
                });
    }

    public void updateToken(){
        try {
            Intent k = new Intent(MainActivity.this, UpdateToken.class);
            startActivity(k);
            finish();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void home(){
        try {
            Intent k = new Intent(MainActivity.this, HomeActivity.class);
            startActivity(k);
            finish();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void login(){
        try {
            Intent k = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(k);
            finish();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
