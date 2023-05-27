package com.kabromtech.azulbenv3;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapterCity extends RecyclerView.Adapter<AdapterCity.AdapterCityHolder> {
    private Context mContext;
    private ArrayList<ItemCity> mCityList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdapterCity(Context context, ArrayList<ItemCity> cityList){
        mContext = context;
        mCityList = cityList;
    }

    @NonNull
    @Override
    public AdapterCityHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_city, viewGroup, false);
        return new AdapterCityHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCityHolder adapterCityHolder, int i) {
        ItemCity currentItem = mCityList.get(i);

        String name = currentItem.getName();
        String fkState = currentItem.getFkState();

        adapterCityHolder.mName.setText(name + " - " + fkState);
    }

    @Override
    public int getItemCount() {
        return mCityList.size();
    }

    public class AdapterCityHolder extends RecyclerView.ViewHolder{

        public TextView mName;

        public AdapterCityHolder(@NonNull View itemView) {
            super(itemView);

            mName = itemView.findViewById(R.id.text_view_name_city);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public static class KnowMoreSyndicateActivity extends AppCompatActivity {

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_know_more_syndicate);
        }
    }
}
