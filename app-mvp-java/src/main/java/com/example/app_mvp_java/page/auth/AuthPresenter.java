package com.example.app_mvp_java.page.auth;

import com.example.app_mvp_java.base.BasePresenter;
import com.example.app_mvp_java.net.ApiService;
import com.example.app_mvp_java.net.RxNetManager;
import com.example.model.BaseResponse;
import com.example.model.Login;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author wandervogel
 * @date 2025-09-22
 * @description 注册登录 Presenter 实现
 */
public class AuthPresenter extends BasePresenter<AuthContract.View> implements AuthContract.Presenter {

    private final ApiService apiService;
    private final CompositeDisposable compositeDisposable;

    public AuthPresenter() {
        this.apiService = RxNetManager.create(ApiService.class);
        this.compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void login(String username, String password) {
        if (!isViewAttached()) return;

        getView().startLoading("登录中...");
        apiService.login(username, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Login>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponse<Login> response) {
                        if (isViewAttached()) {
                            getView().loadingFinished();
                            if (response.getErrorCode() == 0) {
                                getView().onAuthSuccess(response.getData());
                            } else {
                                getView().onAuthFailed(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().loadingFinished();
                            getView().onAuthFailed("网络错误: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void register(String username, String password, String repassword) {
        if (!isViewAttached()) return;

        getView().startLoading("注册中...");
        apiService.register(username, password, repassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseResponse<Login>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(BaseResponse<Login> response) {
                        if (isViewAttached()) {
                            getView().loadingFinished();
                            if (response.getErrorCode() == 0) {
                                getView().onAuthSuccess(response.getData());
                            } else {
                                getView().onAuthFailed(response.getErrorMsg());
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (isViewAttached()) {
                            getView().loadingFinished();
                            getView().onAuthFailed("网络错误: " + e.getMessage());
                        }
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    @Override
    public void detachView() {
        super.detachView();
        compositeDisposable.clear();
    }
}
