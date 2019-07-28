package com.example.android.raghu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ViewHolder extends RecyclerView.ViewHolder {

    View mView;
    public ViewHolder(@NonNull View itemView) {
        super(itemView);

        mView = itemView;
    }

    public void setDetails(Context context, String title, String description, String image)
    {
        TextView mTextTv   = mView.findViewById(R.id.rTitleTv);
        TextView mDetailTv = mView.findViewById(R.id.rDescriptionTv);
        ImageView mImageIv = mView.findViewById(R.id.rImageView);
      //  Button mButtonTV = mView.findViewById(R.id.likebtn);
        mTextTv.setText(title);
        mDetailTv.setText(description);
        Picasso.get().load(image).into(mImageIv);
      //  mButtonTV.setText(likes);
    }
}

