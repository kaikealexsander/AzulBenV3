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

public class AdapterSyndicate extends RecyclerView.Adapter<AdapterSyndicate.AdapterSyndicateHolder> {
    private Context mContext;
    private ArrayList<ItemSyndicate> mSyndicateList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdapterSyndicate(Context context, ArrayList<ItemSyndicate> syndicateList){
        mContext = context;
        mSyndicateList = syndicateList;
    }

    @NonNull
    @Override
    public AdapterSyndicateHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_syndicate, viewGroup, false);
        return new AdapterSyndicateHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterSyndicateHolder adapterSyndicateHolder, int i) {
        ItemSyndicate currentItem = mSyndicateList.get(i);

        String imageUrl = currentItem.getPicture();
        String name = currentItem.getName();
        String description = currentItem.getDescription();

        //adapterSyndicateHolder.mName.setText(name + " - " + description);
        adapterSyndicateHolder.mTextViewName.setText(name);
        adapterSyndicateHolder.mTextViewDescription.setText(description);
        Picasso.get().load(imageUrl).fit().centerInside().into(adapterSyndicateHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mSyndicateList.size();
    }

    public class AdapterSyndicateHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewName;
        public TextView mTextViewDescription;

        public AdapterSyndicateHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_syndicate);
            mTextViewName = itemView.findViewById(R.id.text_view_name_syndicate);
            mTextViewDescription = itemView.findViewById(R.id.text_view_description_syndicate);

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
