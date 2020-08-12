package org.edx.mobile.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.edx.mobile.R;
import org.edx.mobile.base.RuntimeApplication;
import org.edx.mobile.view.custom.MyListData;

import java.util.ArrayList;
import java.util.List;

public class MpesaAdapter extends RecyclerView.Adapter<MpesaAdapter.ViewHolder> {
    private List<MyListData> listdata;
    private Context context;
    List<MyListData> list_of_courses = new ArrayList<MyListData>();
    RuntimeApplication app;


    // RecyclerView recyclerView;
    public MpesaAdapter(List<MyListData> listdata, Context context) {
        this.listdata = listdata;
        this.context = context;
        app = (RuntimeApplication) this.context.getApplicationContext();

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.activity_mpesa_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txt_price.setText(listdata.get(position).getPrice());
        holder.txt_name.setText(listdata.get(position).getName());
        Glide.with(context).load(listdata.get(position).getImage()).into(holder.imageView);
        list_of_courses.clear();

        holder.checkBox.setChecked(false);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(context, "" + (position), Toast.LENGTH_SHORT).show();

                if (listdata.size() != position) {
                    if (isChecked) {

                        list_of_courses.add(new MyListData(listdata.get(position).getName(), listdata.get(position).getPrice(), listdata.get(position).getImage(), listdata.get(position).getJina()));

                    } else {
//                        listFertilityList.get(position).getId();
//                        viewHolder. llRowHealth.setBackgroundColor(Color.WHITE);  // normal color
                        holder.checkBox.setChecked(false);
                        if (list_of_courses.size() != 0) {

                            list_of_courses.remove(list_of_courses.size()-1);
                            Log.e("List", String.valueOf(list_of_courses.size()));
                        }
                    }


                }


                app.setList_of_animals(list_of_courses);
                Log.e("List", String.valueOf(list_of_courses.size()));

            }
        });

    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView txt_name, txt_price;
        CircularImageView imageView;
        public RelativeLayout relativeLayout;
        private CheckBox checkBox;


        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_name = itemView.findViewById(R.id.txt_name);
            this.txt_price = itemView.findViewById(R.id.txt_price);
            checkBox = itemView.findViewById(R.id.checkBox);
            imageView = itemView.findViewById(R.id.imageView);

            relativeLayout = itemView.findViewById(R.id.relativeLayout);
        }
    }
}