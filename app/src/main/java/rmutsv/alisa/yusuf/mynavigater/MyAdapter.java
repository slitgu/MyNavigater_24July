package rmutsv.alisa.yusuf.mynavigater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by masterung on 7/23/2017 AD.
 */

public class MyAdapter extends BaseAdapter {

    private Context context;
    private String[] nameStrings, dateStrings, distanceStrings;
    private TextView nameTextView, dateTextView, distanceTextView;

    public MyAdapter(Context context,
                     String[] nameStrings,
                     String[] dateStrings,
                     String[] distanceStrings) {
        this.context = context;
        this.nameStrings = nameStrings;
        this.dateStrings = dateStrings;
        this.distanceStrings = distanceStrings;
    }

    @Override
    public int getCount() {
        return nameStrings.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view1 = layoutInflater.inflate(R.layout.my_listview, viewGroup, false);

        nameTextView = (TextView) view1.findViewById(R.id.txtShowName);
        dateTextView = (TextView) view1.findViewById(R.id.txtShowDate);
        distanceTextView = (TextView) view1.findViewById(R.id.txtShowDistance);

        nameTextView.setText(nameStrings[i]);
        dateTextView.setText(dateStrings[i]);
        distanceTextView.setText(String.format("%.2f", Double.parseDouble(distanceStrings[i])));


        return view1;
    }
}   // Main Class
