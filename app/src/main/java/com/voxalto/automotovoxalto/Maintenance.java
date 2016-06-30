package com.voxalto.automotovoxalto;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

/**
 * Created by Sabah on 5/2/2016.
 */
public class Maintenance extends Fragment {

    private TextView TV_last, TV_miles, TV_note, TV_next, TV_or;

    public Maintenance() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentHandle = inflater.inflate(R.layout.maintenance, container, false);

        TV_last  = (TextView) fragmentHandle.findViewById(R.id.V_last);
        TV_miles = (TextView) fragmentHandle.findViewById(R.id.V_miles);
        TV_note  = (TextView) fragmentHandle.findViewById(R.id.V_note);
        TV_next  = (TextView) fragmentHandle.findViewById(R.id.V_next);
        TV_or    = (TextView) fragmentHandle.findViewById(R.id.V_or);

        TV_last.setText(" 11/01/2015 ");
        TV_miles.setText(" 65,000");
        TV_note.setText(" Oil Change");
        TV_next.setText(" 3/23/2016 ");
        TV_or.setText(" 70,000");

        TableLayout tl = (TableLayout) fragmentHandle.findViewById(R.id.schedule_status_table);

        TableLayout.LayoutParams params = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        //.setMargins(left, top, right, bottom);
        params.setMargins(5, 1, 1, 1);
        tl.setLayoutParams(params);

        for (int i = 1; i < 5; i++) {
        TableRow row = new TableRow(getActivity());
        TextView tv = new TextView(getActivity());
            tv.setText(" " + i * 10000 + " miles");
            tv.setGravity(Gravity.CENTER);
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setTextSize(20);

        TextView tv_status = new TextView(getActivity());
            tv_status.setGravity(Gravity.CENTER);
            if(i == 4){
                tv_status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.redcross, 0);
            } else {
                tv_status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.greentickicon, 0);
            }
            tv_status.setTextSize(20);
            row.setBackgroundResource(R.drawable.cell_shape);
        row.addView(tv);
        row.addView(tv_status);
            tl.addView(row);
        }
        return fragmentHandle;
    }
}
