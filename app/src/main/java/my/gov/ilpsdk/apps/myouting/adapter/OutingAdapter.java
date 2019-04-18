package my.gov.ilpsdk.apps.myouting.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import my.gov.ilpsdk.apps.myouting.R;
import my.gov.ilpsdk.apps.myouting.Utils.DateUtils;
import my.gov.ilpsdk.apps.myouting.model.Outing;

public class OutingAdapter extends RecyclerView.Adapter<OutingAdapter.ViewHolder> implements Filterable {

    private Context ctx;
    private List<Outing> filter_items, original_items;

    public OutingAdapter(Context context, List<Outing> items){
        filter_items = items;
        original_items = items;
        ctx = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView description,out_date,return_date;
        public ImageView iv_status;

        public ViewHolder(@NonNull View v) {
            super(v);
            description = v.findViewById(R.id.description);
            out_date = v.findViewById(R.id.out_date);
            return_date = v.findViewById(R.id.return_date);
            iv_status = v.findViewById(R.id.iv_status);
        }
    }

    @NonNull
    @Override
    public OutingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_outing,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull OutingAdapter.ViewHolder h, final int position) {
        final Outing c = filter_items.get(position);
         h.description.setText(c.getDescription());
        try {
            h.out_date.setText(DateUtils.formatDateFromDateString2("EEE, dd-MMM-yy hh:mm a",c.getOut_time()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            h.return_date.setText(DateUtils.formatDateFromDateString2("EEE, dd-MMM-yy hh:mm a",c.getReturn_time()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if(c.getStatus() == 1){
            h.iv_status.setImageResource(R.drawable.ic_checked);
        }
        else if(c.getStatus() == 2){
            h.iv_status.setImageResource(R.drawable.ic_cancel);
        }
        else{
            h.iv_status.setImageResource(R.drawable.ic_refresh);
        }


    }

    @Override
    public int getItemCount() {
        return filter_items.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }


}
