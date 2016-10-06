package org.suinot.test_sql;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by remi on 03/10/16.
 */

class CustomAdapter extends BaseAdapter {
    // store the context (as an inflated layout)
    private LayoutInflater inflater;
    // store the resource (typically list_item.xml)
    private int resource;
    // store (a reference to) the data
    private ArrayList<ListItem> data;

    /**
     * Default constructor. Creates the new Adaptor object to
     * provide a ListView with data.
     *
     * @param context
     * @param resource
     * @param data
     */
    CustomAdapter(Context context, int resource, ArrayList<ListItem> data) {
        this.inflater = (LayoutInflater) context.getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        this.resource = resource;
        this.data = data;
    }


    @Override
    public int getCount() {
        return this.data.size ();
    }

    @Override
    public Object getItem(int position) {
        return this.data.get (position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        // reuse a given view, or inflate a new one from the xml
        View view;

        if (convertView == null) {
            view = this.inflater.inflate (resource, parent, false);
        } else {
            view = convertView;
        }

        // bind the data to the view object
        return this.bindData (view, position);
    }

    /**
     * Bind the provided data to the view.
     * This is the only method not required by base adapter.
     */
    View bindData(View view, int position) {
        // make sure it's worth drawing the view
        if (this.data.get (position) == null) {
            return view;
        }

        // pull out the object
        ListItem item = this.data.get (position);

        // extract the view object
        View viewElement = view.findViewById (R.id.Template_Medic);
        // cast to the correct type
        TextView tv = (TextView) viewElement;
        // set the value
        tv.setText (item.medicament);

        viewElement = view.findViewById (R.id.Template_Dose);
        tv = (TextView) viewElement;
        tv.setText (item.dosage);

        // return the final view object
        return view;
    }

}
