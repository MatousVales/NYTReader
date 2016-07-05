package com.matous.nytreader.activities;


import android.content.Intent;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.matous.nytreader.R;
import com.matous.nytreader.data.NYTContract;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class ArticleDetailFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int DETAIL_LOADER = 1;

    private String headline;
    private TextView perex;
    private TextView date;
    private TextView source;
    private ImageView header;
    private LinearLayout tagsLayout;
    private Typeface fontBody;
    private String link;

    public ArticleDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article_detail, container, false);

        headline = getArguments().getString("headline");
        int selectedArticle = getArguments().getInt("_ID");

        Typeface fontTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/heading.ttf");
        Typeface fontPerex = Typeface.createFromAsset(getActivity().getAssets(), "fonts/perex.ttf");
        fontBody = Typeface.createFromAsset(getActivity().getAssets(), "fonts/body.ttf");

        final TextView title = (TextView) view.findViewById(R.id.text_detail_toolbar);
        title.setTypeface(fontTitle);
        title.setText(new String(headline.toCharArray()));
        title.setVisibility(View.GONE);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(headline);
        collapsingToolbar.setExpandedTitleTypeface(fontTitle);
        collapsingToolbar.setCollapsedTitleTypeface(fontTitle);

        final FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, link);
                intent.setType("text/plain");
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.shareWith)));
            }
        });
        final FloatingActionButton fab2 = (FloatingActionButton) view.findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(link));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.openIn)));
            }
        });
        final Animation hide_fab = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_hide);
        final Animation show_fab = AnimationUtils.loadAnimation(getActivity(), R.anim.fab_show);

        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean showTitle = false;
            boolean fabsHidden = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                } else {
                    if (scrollRange + verticalOffset < scrollRange/5) {
                        if (!fabsHidden) {
                            fab.startAnimation(hide_fab);
                            fab2.startAnimation(hide_fab);
                            fabsHidden = true;
                        }
                        fab.setVisibility(View.GONE);
                        fab2.setVisibility(View.GONE);
                    } else {
                        if (fabsHidden) {
                            fab.startAnimation(show_fab);
                            fab2.startAnimation(show_fab);
                            fabsHidden = false;
                        }
                        fab.setVisibility(View.VISIBLE);
                        fab2.setVisibility(View.VISIBLE);
                    }

                    if (scrollRange + verticalOffset == 0) {
                        collapsingToolbar.setTitle(null);
                        title.setSelected(true);
                        title.setVisibility(View.VISIBLE);
                        showTitle = true;
                    } else if (showTitle) {
                        collapsingToolbar.setTitle(headline);
                        title.setVisibility(View.GONE);
                        showTitle = false;
                    }
                }
            }
        });

        perex = (TextView) view.findViewById(R.id.perex);
        perex.setTypeface(fontPerex);

        date = (TextView) view.findViewById(R.id.date);
        date.setTypeface(fontBody);

        source = (TextView) view.findViewById(R.id.source);
        source.setTypeface(fontBody);

        tagsLayout = (LinearLayout) view.findViewById(R.id.tags);
        header = (ImageView) view.findViewById(R.id.header);

        Bundle arg = new Bundle();
        arg.putInt("_ID", selectedArticle);
        getLoaderManager().initLoader(DETAIL_LOADER, arg, this);

        return view;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == DETAIL_LOADER) {
            Uri authority = Uri.parse(NYTContract.CONTENT_URI_NYTARTICLE.toString() + String.valueOf(args.getInt("_ID")));
            return new CursorLoader(getActivity(), authority, NYTContract.NYTArticlesEntry.PROJECTION_DETAIL, null, null, null);
        } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        StringBuilder sb = new StringBuilder(getActivity().getResources().getString(R.string.source));
        Date parsedDate = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        data.moveToFirst();
        link = data.getString(data.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_URL));
        perex.setText(data.getString(data.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_PEREX)));
        source.setText(sb.append(data.getString(data.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_SOURCE))).toString());
        sb.setLength(0);
        sb.append(getActivity().getResources().getString(R.string.datestring));
        sb.trimToSize();

        try {
            parsedDate = sdf.parse(data.getString(data.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_DATE)));
        } catch (ParseException e) {
            Log.e("PARSE EXCEPTION","error parsing date from db",e);
        }
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getActivity());
        date.setText(sb.append(dateFormat.format(parsedDate)).toString());

        String[] parts = data.getString(data.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_KEYWORDS)).split(Pattern.quote("|"));
        if (!TextUtils.isEmpty(parts[0])) {
            for (String s : parts) {
                LinearLayout cardViewContainer = new LinearLayout(getActivity());
                cardViewContainer.setOrientation(cardViewContainer.HORIZONTAL);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(0, 8, 0, 8);

                cardViewContainer.setLayoutParams(params);
                cardViewContainer.setPadding(10, 12, 22, 10);

                TextView textView = new TextView(new ContextThemeWrapper(getActivity(), R.style.textTagStyle));
                textView.setText(s);
                textView.setTypeface(fontBody);

                ImageView img = new ImageView(getActivity());
                img.setImageResource(R.drawable.ic_local_offer_white_18dp);
                img.setPadding(10, 4, 10, 4);
                cardViewContainer.addView(img);
                cardViewContainer.addView(textView);

                CardView cardView = new CardView(getActivity(), null, R.attr.customCardStyle);
                cardView.setLayoutParams(params);
                cardView.addView(cardViewContainer);
                tagsLayout.addView(cardView);
            }
        }
        Glide.clear(header);
        Glide
                .with(getActivity())
                .load(data.getString(data.getColumnIndex(NYTContract.NYTArticlesEntry.COLUMN_LEADIMAGE)))
                .centerCrop()
                .error(R.drawable.defaultimg)
                .crossFade(700)
                .into(header);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
