package com.example.visuallyimpairedproject.interfaces;

import com.example.visuallyimpairedproject.bean.BaseBean;
import com.example.visuallyimpairedproject.bean.News;

import java.util.List;

import io.reactivex.rxjava3.core.Flowable;

public interface NewsOfMCContract {
    interface INewsOfMCView {
        void getNewsHistorySuccess(List<News> news); //得到新闻浏览历史成功
        void getNewsHistoryFailed(Throwable throwable); //得到新闻浏览历史失败
    }
    interface INewsOfMCPresenter {
        void getNewsHistory(Integer pageNo,Integer pageSize,String token); //得到新闻浏览历史
    }
    interface INewsOfMCModel {
        Flowable<BaseBean<List<News>>> getNewsHistory(Integer pageNo, Integer pageSize, String token); //得到新闻浏览历史
    }
}
