package com.matous.nytreader.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import com.matous.nytreader.R;
import com.matous.nytreader.activities.ArticleDetailFragment;
import com.matous.nytreader.activities.MasterActivity;
import com.matous.nytreader.data.NYTContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Matous on 28.06.2016.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {
    public Cursor mCursor;
    private Context mContext;
    StringBuilder sb;
    DateFormat dateFormat;
    SimpleDateFormat sdf;
    Date date = null;
    String datestring;

    public ArticleAdapter(Context c){
        this.mContext = c;
        sb = new StringBuilder(c.getResources().getString(R.string.datestring));
        dateFormat = android.text.format.DateFormat.getDateFormat(c);
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        datestring = mContext.getResources().getString(R.string.datestring);
    }

    public static class ArticleViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView thumb;
        public TextView text1;
        public TextView text2;

        ArticleViewHolder(View itemView){
            super(itemView);

            itemView.setClickable(true);
            Typeface fontHeadline = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/heading.ttf");
            Typeface fontBody = Typeface.createFromAsset(itemView.getContext().getAssets(),"fonts/body.ttf");
            thumb = (CircleImageView) itemView.findViewById(R.id.thumbnail);
            text1 = (TextView) itemView.findViewById(R.id.headline);
            text1.setTypeface(fontHeadline);
            text2 = (TextView) itemView.findViewById(R.id.time);
            text2.setTypeface(fontBody);
        }
    }

    @Override
    public int getItemCount(){
        if(mCursor == null){
            return 0;
        } else {
            return mCursor.getCount();
        }
    }

    @Override
    public ArticleViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        return new ArticleViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.articlecard, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ArticleViewHolder holder, final int position) {
        mCursor.moveToPosition(position);
        final String headline = mCursor.getString(mCursor.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_HEADLINE));
        final int id = mCursor.getInt(mCursor.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_ID));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentJump(headline,id);
            }
        });
        holder.text1.setText(headline);
        try {
            date = sdf.parse(mCursor.getString(mCursor.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_DATE)));
        } catch (ParseException e) {
        }
        holder.text2.setText(sb.append(dateFormat.format(date)));

        sb.setLength(0);
        sb.trimToSize();
        sb.append(datestring);

        Glide.clear(holder.thumb);
        Glide
                .with(mContext)
                .load(mCursor.getString(mCursor.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_THUMB)))
                .centerCrop()
                .error(R.drawable.icon)
                .crossFade(700)
                .into(holder.thumb);
    }

    private void fragmentJump(String h,int id) {
        ArticleDetailFragment mFragment = new ArticleDetailFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("headline", h + " ");
        mBundle.putInt("_ID",id);
        mFragment.setArguments(mBundle);
        MasterActivity actv = (MasterActivity) mContext;
        actv.switchContent(mFragment);
    }
}
