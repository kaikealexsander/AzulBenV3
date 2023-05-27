package com.kabromtech.azulbenv3;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class AdapterWorkingArea  extends RecyclerView.Adapter<AdapterWorkingArea.AdapterWorkingAreaHolder> {
    private Context mContext;
    private ArrayList<ItemWorkingArea> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdapterWorkingArea(Context context, ArrayList<ItemWorkingArea> WorkingAreaList){
        mContext = context;
        mList = WorkingAreaList;
    }

    @NonNull
    @Override
    public AdapterWorkingAreaHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_workingarea, viewGroup, false);
        return new AdapterWorkingAreaHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterWorkingAreaHolder adapterWorkingAreaHolder, int i) {
        ItemWorkingArea currentItem = mList.get(i);

        String imageUrl = currentItem.getPicture();
        String name = currentItem.getName();

        adapterWorkingAreaHolder.mTextViewName.setText(name);
        Picasso.get().load(imageUrl).fit().centerInside().into(adapterWorkingAreaHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdapterWorkingAreaHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewName;

        public AdapterWorkingAreaHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_workingarea);
            mTextViewName = itemView.findViewById(R.id.text_view_name_workingarea);

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

}