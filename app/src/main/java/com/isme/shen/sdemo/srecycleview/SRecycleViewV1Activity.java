package com.isme.shen.sdemo.srecycleview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.isme.shen.sdemo.R;
import com.isme.shen.slibrary.recycleView.ISRecycleView;
import com.isme.shen.slibrary.recycleView.LoadMoreView;
import com.isme.shen.slibrary.recycleView.LoadMoreViewAbs;
import com.isme.shen.slibrary.recycleView.RefreshView;
import com.isme.shen.slibrary.recycleView.RefreshViewAbs;
import com.isme.shen.slibrary.recycleView.SRecycleView;
import com.isme.shen.slibrary.recycleView.SRecycleViewAdapter;
import com.isme.shen.slibrary.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class SRecycleViewV1Activity extends AppCompatActivity {

    private SRecycleView sRecycleView;
    private List list;
    private LoadMoreViewAbs loadMoreView;
    private Button btnLoad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_srecycle_view_v1);
        initView();
        initData();
    }

    private void initView() {
        sRecycleView = (SRecycleView) findViewById(R.id.s_recycleview);
        btnLoad = (Button) findViewById(R.id.btn_load);
    }

    private void initData() {
        list = new ArrayList();
        for(int i = 0;i<10;i++){
            list.add("");
        }

        final DataAdapter dataAdapter = new DataAdapter(this);
        dataAdapter.setData(list);
        sRecycleView.setLayoutManager(new LinearLayoutManager(this));
        final SRecycleViewAdapter adapter = new SRecycleViewAdapter(this,dataAdapter);
        RefreshView refreshView = new RefreshView(this);
        adapter.setHeaderView(refreshView);
        loadMoreView = new LoadMoreView(this);
        adapter.setLoadMoreView(loadMoreView);

        sRecycleView.setAdapter(adapter);

        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i = 0;i<10;i++){
                    list.add("");
                }
                dataAdapter.notifyDataSetChanged();
            }
        });

        adapter.setOnDataChangeListener(new SRecycleViewAdapter.OnDataChangeListener() {
            @Override
            public void noData() {
                btnLoad.setVisibility(View.VISIBLE);
            }

            @Override
            public void uData() {
                btnLoad.setVisibility(View.GONE);
            }
        });

        loadMoreView.setOnLoadMoreListener(new LoadMoreViewAbs.OnLoadMoreListener() {
            @Override
            public void onLoadMoreClick() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i = 0;i<5;i++){
                            list.add("");
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataAdapter.notifyDataSetChanged();
                                if(list.size() >= 100){
                                    loadMoreView.loadMoreComplete(false);
                                } else {
                                    loadMoreView.loadMoreComplete(true);
                                }
                            }
                        });
                    }
                },2000);
            }
        });

        refreshView.setOnRefreshListening(new RefreshViewAbs.OnRefreshViewListening() {
            @Override
            public void refresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                            list.clear();
                           /* for(int i = 0;i<10;i++){
                                list.add("");
                            }*/
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        SRecycleViewV1Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                dataAdapter.notifyDataSetChanged();
                                sRecycleView.refreshComplete();
                                LogUtils.d("main","sRecycleView.refreshComplete();");
                            }
                        });
                    }
                }).start();
            }
        });

        sRecycleView.setOnSRecycleViewScrollListening(new ISRecycleView.OnSRecycleViewScrollListening() {
            @Override
            public void onScrolled(int dx, int dy) {
                LogUtils.d("scroll","dx:"+dx+";dy:"+dy);
            }
        });

        refreshView.setOnRefreshViewPullDownListening(new RefreshViewAbs.OnRefreshViewPullDownListening() {
            @Override
            public void scrolled(float dy) {
                LogUtils.d("refreshView","pullDown:"+dy);
            }
        });

        dataAdapter.notifyDataSetChanged();
    }
}
