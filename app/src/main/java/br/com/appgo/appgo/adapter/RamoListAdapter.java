package br.com.appgo.appgo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.util.Arrays;
import java.util.List;

import br.com.appgo.appgo.R;

/**
 * Created by hex on 23/02/18.
 */

public class RamoListAdapter extends BaseAdapter {

    private Context context;
    private List<String> ramo;

    public RamoListAdapter(Context context){
        this.context = context;
        ramo = Arrays.asList(context.getResources().getStringArray(R.array.service_types));
    }
    @Override
    public int getCount() {
        return ramo.size();
    }

    @Override
    public Object getItem(int position) {
        return ramo.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.adapter_ramo_list, null);
        TextView txtRamo = (TextView) view.findViewById(R.id.txt_ramo);
        txtRamo.setText(ramo.get(position));
        return view;
    }
}
