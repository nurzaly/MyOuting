package my.gov.ilpsdk.apps.myouting;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import my.gov.ilpsdk.apps.myouting.Utils.DateUtils;
import my.gov.ilpsdk.apps.myouting.Utils.DividerItemDecoration;
import my.gov.ilpsdk.apps.myouting.Utils.Helper;
import my.gov.ilpsdk.apps.myouting.Utils.Tools;
import my.gov.ilpsdk.apps.myouting.adapter.OutingAdapter;
import my.gov.ilpsdk.apps.myouting.data.Constant;
import my.gov.ilpsdk.apps.myouting.model.Outing;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";
    private RecyclerView recyclerView;
    private List<Outing> outings;
    private OutingAdapter mAdapter;
    private Dialog dialog;
    private String date_out,date_return,time_out,time_return;
    private String ndp,tujuan;
    private Outing outing;
    private SharedPreferences config;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        recyclerView = findViewById(R.id.rvstatus);
        config = getSharedPreferences(Constant.KEY_CONFIG,0);
        initToolbar();
        initComponent();


        date_out = DateUtils.getCurrentDate("YYYY-MM-dd");
        time_out = DateUtils.getCurrentDate("HH:mm:ss");
        date_return = DateUtils.getCurrentDate("YYYY-MM-dd");
        time_return = DateUtils.getCurrentDate("HH:mm:ss");
        ndp = config.getString(Constant.KEY_NDP,null);

        tujuan = "";

        ///outing = new Outing(ndp,date_out + " " + time_out,date_return + " " + time_return,"");
        // Log.d(TAG, "onCreate: " + time_out);
        //dialogDatePickerDark();
        //showCustomDialog();
        get_outing();

        ((TextView)findViewById(R.id.tv_user_name)).setText(config.getString(Constant.KEY_USER,null));
        ((TextView)findViewById(R.id.tv_user_position)).setText(config.getString(Constant.KEY_COURSE,null));
    }

    private void get_outing(){
        AndroidNetworking.get(Constant.URL_GET_USER_OUTING)
                .addQueryParameter("ndp",ndp)
                .build()
                .getAsObjectList(Outing.class, new ParsedRequestListener<List<Outing>>() {
                    @Override
                    public void onResponse(List<Outing> response) {
                        outings = response;
                        display_outing();
                    }
                    @Override
                    public void onError(ANError anError) {
                        Helper.error_connection(getApplicationContext(),anError,TAG);
                        Log.d(TAG, "onError: " + Constant.URL_GET_USER_OUTING);
                    }
                });
    }

    private void setup_datetime(int vid,long date){
        switch (vid){
            case R.id.out_date:
                date_out = Tools.getFormattedDateTime(date,"YYYY-MM-dd");
                break;
            case R.id.out_time:
                time_out = Tools.getFormattedDateTime(date,"HH:mm:ss");
                break;
            case R.id.return_date:
                date_return = Tools.getFormattedDateTime(date,"YYYY-MM-dd");
                break;
            case R.id.return_time:
                time_return = Tools.getFormattedDateTime(date,"HH:mm:ss");
                break;

        }
    }

    private void save_to_database(){

        tujuan = ((EditText)dialog.findViewById(R.id.etDescription)).getText().toString();
        if(tujuan.isEmpty()){
            Helper.showToastLong(this,"Sila nyatakan tujuan keluar");
            return;
        }

        outing = new Outing(ndp,date_out + " " + time_out,date_return + " " + time_return,tujuan);


        try {
            Date outdate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(outing.getOut_time());
            Date returndate = new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(outing.getReturn_time());
            if(returndate.compareTo(outdate) <= 0){
                Helper.showToastLong(this,"Pilihan masa keluar dan masa masuk tidak lengkap");
            }
            else{
                save_outing_data();
                dialog.dismiss();
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Helper.showToast(this,"Error on date selection");
        }

    }

    private void save_outing_data() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        AndroidNetworking.post(Constant.URL_SAVE_NEW_USER_OUTING)
                .setTag(this)
                .setPriority(Priority.HIGH)
                .addBodyParameter(outing)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();

                        try {
                            Log.d(TAG, "onResponse: " + response.getJSONObject("status"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        get_outing();


//                        try {
//                            new MaterialDialog.Builder(AssetsDetailsActivity.this)
//                                    .title("Makluman")
//                                    .content(response.getString("message"))
//                                    .positiveText("OK")
//                                    .show();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        progressDialog.dismiss();
                        Helper.error_connection(HomeActivity.this,anError,TAG);
                    }
                });
    }

    private void dialogDatePicker(final int vid) {
        Calendar cur_calender = Calendar.getInstance();
        DatePickerDialog datePicker = DatePickerDialog.newInstance(
                new DatePickerDialog.OnDateSetListener() {
                    @SuppressLint("WrongViewCast")
                    @Override
                    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        long date_ship_millis = calendar.getTimeInMillis();
                        //(AppCompatEditText) view.find(R.id.result)).setText(Tools.getFormattedDateSimple(date_ship_millis));
                        setup_datetime(vid,date_ship_millis);
                        ((EditText)dialog.findViewById(vid)).setText(Tools.getFormattedDateTime(date_ship_millis,"EEE, dd-MMM-YYYY"));
                    }
                },
                cur_calender.get(Calendar.YEAR),
                cur_calender.get(Calendar.MONTH),
                cur_calender.get(Calendar.DAY_OF_MONTH)
        );
        //set dark theme
        //datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.setMinDate(cur_calender);
        datePicker.show(getFragmentManager(), "Datepickerdialog");
    }

    private void dialogTimePicker(final int vid) {
        Calendar cur_calender = Calendar.getInstance();
        TimePickerDialog datePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                long time_ship_millis = calendar.getTimeInMillis();
                setup_datetime(vid,time_ship_millis);
                ((TextView) dialog.findViewById(vid)).setText(Tools.getFormattedTimeEvent(time_ship_millis));
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), false);
        //set dark light
        datePicker.setThemeDark(false);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }

    @SuppressLint("WrongViewCast")
    private void showCustomDialog() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_review);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        EditText et_out_date = dialog.findViewById(R.id.out_date);
        EditText et_out_time = dialog.findViewById(R.id.out_time);
        EditText et_return_date = dialog.findViewById(R.id.return_date);
        EditText et_return_time = dialog.findViewById(R.id.return_time);
        Button btn_submit = dialog.findViewById(R.id.bt_submit);

        final EditText et_post = (EditText) dialog.findViewById(R.id.etDescription);
        //final AppCompatRatingBar rating_bar = (AppCompatRatingBar) dialog.findViewById(R.id.rating_bar);
        ((AppCompatButton) dialog.findViewById(R.id.bt_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_submit)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review = et_post.getText().toString().trim();
                if (review.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill review text", Toast.LENGTH_SHORT).show();
                } else {
                    //items.add("(" + rating_bar.getRating() + ") " + review);
                    //adapter.notifyDataSetChanged();
                }

                dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Submitted", Toast.LENGTH_SHORT).show();
            }
        });

        init_date_form(et_out_date,"EEE, dd-MMM-YYYY",date_out + " " + time_out);
        init_date_form(et_return_date,"EEE, dd-MMM-YYYY",date_return + " " + time_return);
        init_date_form(et_out_time,"hh:mm a",date_out + " " + time_out);
        init_date_form(et_return_time,"hh:mm a",date_return + " " + time_return);


        et_out_date.setOnClickListener(clickListener);
        et_out_time.setOnClickListener(clickListener);
        et_return_date.setOnClickListener(clickListener);
        et_return_time.setOnClickListener(clickListener);
        btn_submit.setOnClickListener(clickListener);



        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    private void init_date_form(EditText et,String datetimeformat,String val){
        try {
            et.setText(DateUtils.formatDateFromDateString2(datetimeformat,val));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }


    private View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.out_date:
                    dialogDatePicker(view.getId());
                    break;
                case R.id.return_date:
                    dialogDatePicker(view.getId());
                    break;
                case R.id.out_time:
                    dialogTimePicker(view.getId());
                    break;
                case R.id.return_time:
                    dialogTimePicker(view.getId());
                    break;
                case R.id.bt_submit:
                    save_to_database();
                    break;

            }
        }
    };

    private void display_outing(){

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        // specify an adapter (see also next example)
        mAdapter = new OutingAdapter(this, outings);
        recyclerView.setAdapter(mAdapter);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("MyOuting");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this, R.color.pink_700);
    }

    private void initComponent() {
        ((FloatingActionButton) findViewById(R.id.fab_add)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCustomDialog();
            }
        });
    }

    private void showDialogAbout() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_about);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ((TextView) dialog.findViewById(R.id.tv_version)).setText("Version " + BuildConfig.VERSION_NAME);

        ((View) dialog.findViewById(R.id.bt_getcode)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://codecanyon.net/user/dream_space/portfolio"));
                startActivity(i);
            }
        });

        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

//        ((Button) dialog.findViewById(R.id.bt_rate)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Tools.rateAction(AboutDialogMainAction.this);
//            }
//        });
//
//        ((Button) dialog.findViewById(R.id.bt_portfolio)).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Tools.openInAppBrowser(AboutDialogMainAction.this, "http://portfolio.dream-space.web.id/", false);
//            }
//        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);
    }

    public void logout(){
        SharedPreferences.Editor editor = config.edit();
        editor.remove(Constant.KEY_USER);
        editor.remove(Constant.KEY_NDP);
        editor.remove(Constant.KEY_COURSE);
        editor.commit();
        login();
    }

    private void login(){
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_about) {
            showDialogAbout();
        }
        else if(item.getItemId() == R.id.action_logout){
            logout();
        }
        else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}