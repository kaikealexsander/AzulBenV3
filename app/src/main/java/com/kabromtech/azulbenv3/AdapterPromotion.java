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

public class AdapterPromotion extends RecyclerView.Adapter<AdapterPromotion.AdapterPromotionHolder> {
    private Context mContext;
    private ArrayList<ItemPromotion> mList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdapterPromotion(Context context, ArrayList<ItemPromotion> PromotionList){
        mContext = context;
        mList = PromotionList;
    }

    @NonNull
    @Override
    public AdapterPromotionHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_promotion, viewGroup, false);
        return new AdapterPromotionHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPromotionHolder adapterPromotionHolder, int i) {
        ItemPromotion currentItem = mList.get(i);

        String imageUrl = currentItem.getPicture();
        String name = currentItem.getName();

        adapterPromotionHolder.mTextViewName.setText(name);
        Picasso.get().load(imageUrl).fit().centerInside().into(adapterPromotionHolder.mImageView);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class AdapterPromotionHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextViewName;

        public AdapterPromotionHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_promotion);
            mTextViewName = itemView.findViewById(R.id.text_view_name_promotion);

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
