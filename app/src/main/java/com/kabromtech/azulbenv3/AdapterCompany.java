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

public class AdapterCompany extends RecyclerView.Adapter<AdapterCompany.AdapterCompanyHolder> {
    private Context mContext;
    private ArrayList<ItemCompany> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdapterCompany(Context context, ArrayList<ItemCompany> CompanyList){
        mContext = context;
        mList = CompanyList;
    }

    @NonNull
    @Override
    public AdapterCompanyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_company, viewGroup, false);
        return new AdapterCompanyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCompanyHolder adapterCompanyHolder, int i) {
        ItemCompany currentItem = mList.get(i);

        String imageUrl = currentItem.getPicture();
        String name = currentItem.getName();
        String description = currentItem.getDescription();

        adapterCompanyHolder.mTextViewName.setText(name);
        adapterCompanyHolder.mTextViewDescription.setText(description);
        Picasso.get().load(imageUrl).fit().centerInside().into(adapterCompanyHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdapterCompanyHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewName;
        public TextView mTextViewDescription;

        public AdapterCompanyHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_company);
            mTextViewName = itemView.findViewById(R.id.text_view_name_company);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description_company);

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
