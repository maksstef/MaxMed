package com.example.diploma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DoctorListAdapter extends ArrayAdapter<User> implements Filterable {

    private LayoutInflater inflater;
    private int layout;
    //private Context mContext;
    private ArrayList<User> doctors;
    private ArrayList<User> filterDoctors;
    private User user = null;

    public DoctorListAdapter(Context context, int resource, ArrayList<User> doctors) {
        super(context, resource, doctors);
        this.inflater = LayoutInflater.from(context);
        this.layout = resource;
        this.doctors = doctors;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView DateOfBirth = (TextView) view.findViewById(R.id.j_DateOfBirth);
        TextView Name = (TextView) view.findViewById(R.id.j_Name);

        user = doctors.get(position);

        DateOfBirth.setText(user.Doctors.get(0).Department);
        Name.setText(user.FullName);
        return view;
    }

    Filter myFilter = new Filter() {

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence contraint, FilterResults results) {
            filterDoctors = (ArrayList<User>) results.values;
            doctors = filterDoctors;
            notifyDataSetChanged();

        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            ArrayList<User> tempList = new ArrayList<>();
            //constraint is the result from text you want to filter against.
            //objects is your data set you will filter from
            if(constraint != null && doctors != null) {
                int length=doctors.size();
                int i=0;
                while(i<length){
                    User item = doctors.get(i);
                    //do whatever you wanna do here
                    //adding result set output array
                    if(item.FullName.toLowerCase().startsWith(constraint.toString())) {
                        tempList.add(item);
                    }

                    i++;
                }

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
        return doctors.size();
    }

    @Nullable
    @Override
    public User getItem(int position) {
        return doctors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setListDoctors( ArrayList<User> doctors){
        this.doctors = doctors;
    }
}
