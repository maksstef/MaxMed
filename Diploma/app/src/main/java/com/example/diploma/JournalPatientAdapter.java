package com.example.diploma;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

//public class JournalPatientAdapter extends BaseAdapter implements Filterable {
//
//    private ArrayList<JournalPatients> mOriginalValues; // Original Values
//    private ArrayList<JournalPatients> mDisplayedValues;    // Values to be displayed
//    LayoutInflater inflater;
//
//    public JournalPatientAdapter(Context context, ArrayList<JournalPatients> mProductArrayList) {
//        this.mOriginalValues = mProductArrayList;
//        this.mDisplayedValues = mProductArrayList;
//        inflater = LayoutInflater.from(context);
//    }
//
//    @Override
//    public int getCount() {
//        return mDisplayedValues.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return position;
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    private class ViewHolder {
//        LinearLayout llContainer;
//        TextView Name,DateOfBirth;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//
//        ViewHolder holder = null;
//
//        if (convertView == null) {
//
//            holder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.row, null);
//            holder.llContainer = (LinearLayout)convertView.findViewById(R.id.llContainer);
//            holder.Name = (TextView) convertView.findViewById(R.id.j_Name);
//            holder.DateOfBirth = (TextView) convertView.findViewById(R.id.j_DateOfBirth);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        holder.Name.setText(mDisplayedValues.get(position).getPatientName());
//        holder.DateOfBirth.setText(mDisplayedValues.get(position).getDateOfBirth()+"");
//
//
//        return convertView;
//    }
//
//    @Override
//    public Filter getFilter() {
//        Filter filter = new Filter() {
//
//            @SuppressWarnings("unchecked")
//            @Override
//            protected void publishResults(CharSequence constraint,FilterResults results) {
//
//                mDisplayedValues = (ArrayList<JournalPatients>) results.values; // has the filtered values
//                notifyDataSetChanged();  // notifies the data with new filtered values
//            }
//
//            @Override
//            protected FilterResults performFiltering(CharSequence constraint) {
//                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
//                ArrayList<JournalPatients> FilteredArrList = new ArrayList<JournalPatients>();
//
//                if (mOriginalValues == null) {
//                    mOriginalValues = new ArrayList<JournalPatients>(mDisplayedValues); // saves the original data in mOriginalValues
//                }
//
//                if (constraint == null || constraint.length() == 0) {
//
//                    // set the Original result to return
//                    results.count = mOriginalValues.size();
//                    results.values = mOriginalValues;
//                } else {
//                    constraint = constraint.toString().toLowerCase();
//                    for (int i = 0; i < mOriginalValues.size(); i++) {
//                        String data = mOriginalValues.get(i).getPatientName();
//                        if (data.toLowerCase().startsWith(constraint.toString())) {
//                            FilteredArrList.add(new JournalPatients(mOriginalValues.get(i).getPatientId(),mOriginalValues.get(i).getPatientName(),mOriginalValues.get(i).getDateOfBirth()));
//                        }
//                    }
//                    // set the Filtered result to return
//                    results.count = FilteredArrList.size();
//                    results.values = FilteredArrList;
//                }
//                return results;
//            }
//        };
//        return filter;
//    }
//
//}

public class JournalPatientAdapter extends ArrayAdapter<JournalPatients> implements Filterable{

    private LayoutInflater inflater;
    private int layout;
    //private Context mContext;
    private ArrayList<JournalPatients> patients;
    private ArrayList<JournalPatients> filterPatients;
    private JournalPatients patient = null;


    public JournalPatientAdapter(Context context, int resource, ArrayList<JournalPatients> patients){
        super(context, resource, patients);
        this.patients = patients;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView DateOfBirth = (TextView) view.findViewById(R.id.j_DateOfBirth);
        TextView Name = (TextView) view.findViewById(R.id.j_Name);

        patient = patients.get(position);

        DateOfBirth.setText(patient.getDateOfBirth());
        Name.setText(patient.getPatientName());
        return view;
    }

    Filter myFilter = new Filter() {

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            filterPatients = (ArrayList<JournalPatients>) results.values;
//            if (results.count > 0) {
//                notifyDataSetChanged();
//            } else {
//                notifyDataSetInvalidated();
//            }
            patients = filterPatients;
            notifyDataSetChanged();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<JournalPatients> tempList = new ArrayList<>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(constraint != null && patients != null) {
                int length=patients.size();
                int i=0;
                while(i<length){
                    JournalPatients item = patients.get(i);
                    //do whatever you wanna do here
                    //adding result set output array
                    if(item.getPatientName().toLowerCase().startsWith(constraint.toString())) {
                        tempList.add(item);
                    }

                    i++;
                }
                //following two lines is very important
                //as publish result can only take FilterResults objects
                filterResults.values = tempList;
                filterResults.count = tempList.size();
            }
            return filterResults;
        }

    };


    @Override
    public Filter getFilter() {
        return myFilter;
    }

    @Override
    public int getCount() {
        return patients.size();
    }

    @Nullable
    @Override
    public JournalPatients getItem(int position) {
        return patients.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setListPatients( ArrayList<JournalPatients> patients){
        this.patients = patients;
    }
}

