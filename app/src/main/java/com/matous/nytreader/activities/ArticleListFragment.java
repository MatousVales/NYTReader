package com.matous.nytreader.activities;

import android.database.Cursor;

import android.graphics.Typeface;
import android.os.Bundle;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.matous.nytreader.R;
import com.matous.nytreader.adapters.ArticleAdapter;
import com.matous.nytreader.bus.BusProvider;
import com.matous.nytreader.data.NYTContract;
import com.matous.nytreader.events.GetArticlesEvent;
import com.matous.nytreader.events.SendArticlesEvent;

import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.HashMap;
import java.util.Map;

public class ArticleListFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final int PREVIEW_LOADER = 0;

    private ArticleAdapter mAdapter;
    private final Map<String, String> apiParams = new HashMap<>();
    private SwipeRefreshLayout mRefreshLayout;
    private Bus mBus = BusProvider.getInstance();
    private boolean showLoading;
    private boolean firstStart = true;

    public ArticleListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiParams.put("api-key", "sample-key");
        apiParams.put("sort", "newest");
        apiParams.put("q", "new+york+times");
        showLoading = true;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_article_list, container, false);

        Typeface fontTitle = Typeface.createFromAsset(getActivity().getAssets(), "fonts/appname.ttf");

        TextView title = (TextView) view.findViewById(R.id.text_list_toolbar);
        title.setTypeface(fontTitle);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.list_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle(null);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_layout);
        mRefreshLayout.setColorSchemeResources(R.color.colorAccent,R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBus.post(new GetArticlesEvent(apiParams));
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.master_list);
        RecyclerView.LayoutManager ARLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(ARLayoutManager);
        mAdapter = new ArticleAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        if (showLoading) {
            mRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    mRefreshLayout.setRefreshing(true);
                    mBus.post(new GetArticlesEvent(apiParams));
                    showLoading = false;
                }
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onResume();
        mBus.unregister(this);
    }

    @Subscribe
    public void onSendArticlesEvent(SendArticlesEvent sendArticlesEvent) {
        int result = sendArticlesEvent.getResult();

        if (result == 420){
            Snackbar snackbar = Snackbar
                    .make(getView(),getResources().getString(R.string.noInternet), Snackbar.LENGTH_SHORT);
            snackbar.show();
        }

        if(result != 0){
            if(firstStart){
                getLoaderManager().initLoader(PREVIEW_LOADER,null,this);
                firstStart = false;
            } else {
                getLoaderManager().restartLoader(PREVIEW_LOADER,null,this);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == PREVIEW_LOADER){
            return new CursorLoader(getActivity(),NYTContract.CONTENT_URI_NYTARTICLE,NYTContract.NYTArticlesEntry.PROJECTION_PREVIEW,null,null,NYTContract.NYTArticlesEntry.SORT_ORDER_DEFAULT);
           } else {
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.mCursor = data;
        mRefreshLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }

}
