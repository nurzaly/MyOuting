package my.gov.ilpsdk.apps.myouting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        home();
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
}
