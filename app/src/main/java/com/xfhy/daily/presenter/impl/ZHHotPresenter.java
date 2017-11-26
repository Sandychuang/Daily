package com.xfhy.daily.presenter.impl;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.xfhy.androidbasiclibs.basekit.presenter.AbstractPresenter;
import com.xfhy.androidbasiclibs.common.db.CacheBean;
import com.xfhy.androidbasiclibs.common.db.CacheDao;
import com.xfhy.androidbasiclibs.common.db.DBConstants;
import com.xfhy.androidbasiclibs.common.util.Constants;
import com.xfhy.androidbasiclibs.common.util.DevicesUtils;
import com.xfhy.androidbasiclibs.common.util.LogUtils;
import com.xfhy.androidbasiclibs.common.util.StringUtils;
import com.xfhy.daily.NewsApplication;
import com.xfhy.daily.R;
import com.xfhy.daily.network.RetrofitHelper;
import com.xfhy.daily.network.entity.zhihu.HotDailyBean;
import com.xfhy.daily.presenter.ZHHotContract;

import java.util.List;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * @author xfhy
 *         create at 2017/11/26 16:11
 *         description：知乎热门文章Presenter
 */
public class ZHHotPresenter extends AbstractPresenter<ZHHotContract.View> implements
        ZHHotContract.Presenter {
    private RetrofitHelper mRetrofitHelper;
    private List<HotDailyBean.RecentBean> mData;
    private int mStep;

    public ZHHotPresenter(Context context) {
        super(context);
        mRetrofitHelper = RetrofitHelper.getInstance();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromNet() {
        view.onLoading();
        mStep = Constants.STATE_LOADING;
        if (DevicesUtils.hasNetworkConnected(mContext)) {
            mRetrofitHelper.getZhiHuApi().getHotDailyList()
                    .compose(view.bindLifecycle())
                    .map(new Function<HotDailyBean, List<HotDailyBean.RecentBean>>() {
                        @Override
                        public List<HotDailyBean.RecentBean> apply(HotDailyBean hotDailyBean)
                                throws Exception {
                            return hotDailyBean.getRecent();
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<HotDailyBean.RecentBean>>() {
                        @Override
                        public void accept(List<HotDailyBean.RecentBean> recentBeans)
                                throws Exception {
                            if (recentBeans != null) {
                                view.loadSuccess(recentBeans);
                                mData = recentBeans;
                                saveDataToDB(recentBeans);
                            } else {
                                view.showErrorMsg("热门列表加载失败....");
                                view.showEmptyView();
                            }
                            mStep = Constants.STATE_NORMAL;
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            LogUtils.e("热门列表加载失败 错误:" + throwable.getLocalizedMessage());
                            view.showEmptyView();
                        }
                    });
        } else {
            reqDataFromDB();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void reqDataFromDB() {
        Flowable.create(new FlowableOnSubscribe<CacheBean>() {
            @Override
            public void subscribe(FlowableEmitter<CacheBean> e) throws
                    Exception {
                List<CacheBean> cacheBeans = CacheDao.queryCacheByKey(NewsApplication
                        .getDaoSession(), DBConstants.ZHIHU_HOT_LIST_KEY);
                if (cacheBeans != null && cacheBeans.size() > 0 && cacheBeans.get(0) != null) {
                    CacheBean cacheBean = cacheBeans.get(0);  //读取出来的值
                    e.onNext(cacheBean);
                } else {
                    e.onError(new Exception(StringUtils.getStringByResId(mContext, R.string
                            .devices_offline)));
                }
            }
        }, BackpressureStrategy.BUFFER)
                .compose(view.bindLifecycle())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<CacheBean>() {
                    @Override
                    public void accept(CacheBean cacheBean) throws
                            Exception {
                        //解析json数据
                        mData = JSON.parseArray(cacheBean.getJson(), HotDailyBean.RecentBean
                                .class);
                        //判断数据是否为空
                        if (mData != null) {
                            view.showContent();

                            //刷新界面
                            view.loadSuccess(mData);
                            mStep = Constants.STATE_NORMAL;
                        } else {
                            //无数据   显示空布局
                            view.showEmptyView();
                        }
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        String localizedMessage = throwable.getLocalizedMessage();
                        LogUtils.e(localizedMessage);

                        if (StringUtils.getStringByResId(mContext, R.string.devices_offline)
                                .equals(localizedMessage)) {
                            view.showOffline();
                            mStep = Constants.STATE_ERROR;
                        } else {
                            view.showErrorMsg(localizedMessage);
                        }
                    }
                });
    }

    @Override
    public void saveDataToDB(List<HotDailyBean.RecentBean> dataBeans) {
        CacheDao.saveTextToDB(DBConstants.ZHIHU_HOT_LIST_KEY, NewsApplication.getDaoSession(),
                JSON.toJSONString(dataBeans));
    }

    @Override
    public List<HotDailyBean.RecentBean> getData() {
        return mData;
    }

    @Override
    public void refreshData() {
        if (mStep == Constants.STATE_LOADING) {
            return;
        }
        reqDataFromNet();
    }

    @Override
    public int getDailyId(int position) {
        if (mData != null && mData.size() > position) {
            return mData.get(position).getNewsId();
        }
        return 0;
    }
}
