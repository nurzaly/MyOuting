package my.gov.ilpsdk.apps.myouting;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import my.gov.ilpsdk.apps.myouting.Utils.Helper;
import my.gov.ilpsdk.apps.myouting.Utils.Tools;
import my.gov.ilpsdk.apps.myouting.data.Constant;
import my.gov.ilpsdk.apps.myouting.model.Outing;


public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private View parent_view;
    private EditText et_email,et_password;
    //private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //FirebaseApp.initializeApp(this);
        //mAuth = FirebaseAuth.getInstance();

        parent_view = findViewById(android.R.id.content);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);

        findViewById(R.id.btn_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

//        ((View) findViewById(R.id.sign_up)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(parent_view, "Sign Up", Snackbar.LENGTH_SHORT).show();
//            }
//        });


        Tools.setSystemBarColor(this);
    }

    private void login() {
        final String ndp = et_email.getText().toString().trim();
        final String password = et_password.getText().toString().trim();

        if(ndp.isEmpty()){
            et_email.findFocus();
            et_email.setError("Email Required");
            return;
        }

        if(password.isEmpty()){
            et_password.findFocus();
            et_password.setError("Password Required");
            return;
        }

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        AndroidNetworking.post(Constant.URL_LOGIN)
                .addBodyParameter("ndp",ndp)
                .addBodyParameter("password",password)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            if(response.get("status").equals("success")){
                                JSONObject data = response.getJSONObject("data");
                                SharedPreferences config = getSharedPreferences(Constant.KEY_CONFIG,0);
                                SharedPreferences.Editor editor = config.edit();
                                editor.putString(Constant.KEY_USER,data.getString("fullname"));
                                editor.putString(Constant.KEY_COURSE,data.getString("course_name"));
                                editor.putString(Constant.KEY_NDP,ndp);
                                editor.commit();

                                home();
                            }
                            else{
                                Helper.showToast(getApplicationContext(),"Login failed");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Helper.showToast(getApplicationContext(),"Error on parse status");
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Helper.error_connection(getApplicationContext(),anError,TAG);
                    }
                });

//        mAuth.createUserWithEmailAndPassword(email,password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            home();
//                        }
//                        else{
//                            if(task.getException() instanceof FirebaseAuthUserCollisionException){
//                                userLogin(email,password);
//                            }
//                            else{
//                                Helper.showToast(LoginActivity.this,task.getException().getMessage());
//                            }
//                        }
//                    }
//                });

    }

    private void home(){
        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

//    private void userLogin(String email, String password){
//        mAuth.signInWithEmailAndPassword(email,password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if(task.isSuccessful()){
//                            home();
//                        }
//                        else{
//                            Helper.showToast(LoginActivity.this,task.getException().getMessage());
//                        }
//                    }
//                });
//    }
}
