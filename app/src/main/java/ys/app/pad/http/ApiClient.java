package ys.app.pad.http;


import android.support.annotation.NonNull;

import java.util.Map;

import okhttp3.MultipartBody;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import ys.app.pad.AppApplication;
import ys.app.pad.utils.Logger;

/**
 * <p>the Client for the data API handler</p>
 * <p>Usage:</p>
 * <p>
 * Example 1:
 * <code>
 * new Client<T>().reqApi(apiName, params)
 * .saveToDB(Callback<T>)
 * .updateUI(Callback<T>);
 * </code>
 * </p>
 * <p>Note: call the method must be end with <code>updateUI</code> or <code>updateNoUI</code></p>
 * <p>
 * Created by dennis on 2017/1/4 16:43
 * for ebrun
 * you can contact me at : pengjianjun@ebrun.com
 * or see link for more details
 *
 * @version 1.0.0
 */
public class ApiClient<T> {

    private Subscription mSubscription;

    private Observable<T> mResponse;

    public ApiClient() {
    }

//    /**
//     * request the api.
//     * the dataType of the response is 'XML'
//     * @param apiMethodName  the method-name of the api in the the api service - see {@link ApiService}
//     * @param params   the params of the request.
//     * @return
//     */
//    public ApiClient<T> reqApi(@NonNull String apiMethodName, Map<String, String> params) {
//        return reqApi(apiMethodName, params, ApiRequest.RespDataType.RESPONSE_XML);
//    }

    /**
     * request the api.
     *
     * @param apiMethodName the method-name of the api in the the api service - see {@link ApiService}
     * @param params        the params of the request.
     * @param dataType      the dataType of the response , 'XML' or 'JSON'.
     * @return
     */
    public ApiClient<T> reqApi(@NonNull String apiMethodName, Map<String, String> params, @NonNull ApiRequest.RespDataType dataType) {
        ApiRequest<ApiService> apiRequest;
        if (dataType == ApiRequest.RespDataType.RESPONSE_XML) {
            apiRequest = ApiRequest.getDefaultInstance();
        } else if (dataType == ApiRequest.RespDataType.RESPONSE_JSON) {
            apiRequest = ApiRequest.getDefaultJsonInstance();
        } else {
            Logger.w("Failure to create the 'ApiRequest'.");
            return this;
        }
        return reqApi(apiMethodName, params, apiRequest);
    }
    /**
     * 延长请求时间
     */
    public ApiClient<T> reqLongTimeApi(@NonNull String apiMethodName, Map<String, String> params, @NonNull ApiRequest.RespDataType dataType) {
        ApiRequest.DEFAULT_TIMEOUT=600;
        ApiRequest<ApiService> apiRequest;
        if (dataType == ApiRequest.RespDataType.RESPONSE_XML) {
            apiRequest = ApiRequest.getDefaultInstance();
        } else if (dataType == ApiRequest.RespDataType.RESPONSE_JSON) {
            apiRequest = new ApiRequest.Builder(AppApplication.getAppContext()).respDataType(ApiRequest.RespDataType.RESPONSE_JSON).build();
        } else {
            Logger.w("Failure to create the 'ApiRequest'.");
            return this;
        }
        return reqApi(apiMethodName, params, apiRequest);
    }

    /**
     *负载均衡
     */
    public ApiClient<T> reqOtherURL(@NonNull String apiMethodName, Map<String, String> params, @NonNull ApiRequest.RespDataType dataType,String baseURL) {
        ApiRequest.DEFAULT_TIMEOUT=600;
        ApiRequest<ApiService> apiRequest;
        if (dataType == ApiRequest.RespDataType.RESPONSE_XML) {
            apiRequest = ApiRequest.getDefaultInstance();
        } else if (dataType == ApiRequest.RespDataType.RESPONSE_JSON) {
            apiRequest = new ApiRequest.Builder(AppApplication.getAppContext(), baseURL).respDataType(ApiRequest.RespDataType.RESPONSE_JSON).build();
        } else {
            Logger.w("Failure to create the 'ApiRequest'.");
            return this;
        }
        return reqApi(apiMethodName, params, apiRequest);
    }


    public ApiClient<T> reqUploadApi(@NonNull String apiMethodName, MultipartBody.Part part, @NonNull ApiRequest.RespDataType dataType) {
        ApiRequest<ApiService> apiRequest;
        if (dataType == ApiRequest.RespDataType.RESPONSE_XML) {
            apiRequest = ApiRequest.getDefaultInstance();
        } else if (dataType == ApiRequest.RespDataType.RESPONSE_JSON) {
            apiRequest = ApiRequest.getDefaultJsonInstance();
        } else {
            Logger.w("Failure to create the 'ApiRequest'.");
            return this;
        }
        return reqUploadApi(apiMethodName, part, apiRequest);
    }


    /**
     * request the api.
     *
     * @param apiMethodName the method-name of the api in the the api service which see the third param - <code>builder</code> .
     * @param apiRequest    the {@link ApiRequest}.
     * @return
     */
    public <R> ApiClient<T> reqUploadApi(@NonNull final String apiMethodName,MultipartBody.Part part, @NonNull ApiRequest<R> apiRequest) {
//        mResponse = (Observable<T>) apiRequest.reqApi(apiMethodName, params)
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Logger.e(apiMethodName+"--->"+throwable.getMessage().toString());
//                    }
//                })
//                .subscribeOn(AppApplication.getInstance().subscribeScheduler())
//                .unsubscribeOn(AppApplication.getInstance().subscribeScheduler());

        mResponse = (Observable<T>)apiRequest.reqUploadApi(apiMethodName, part);
        if (mResponse != null) {
            mResponse = mResponse.subscribeOn(AppApplication.getInstance().subscribeScheduler())
                    .unsubscribeOn(AppApplication.getInstance().subscribeScheduler());
        }

        reset();
        return this;
    }

    /**
     * request the api.
     *
     * @param apiMethodName the method-name of the api in the the api service which see the third param - <code>builder</code> .
     * @param params        the params of the request.
     * @return
     */
    public ApiClient<T> reqApi(@NonNull String apiMethodName, Map<String, String> params, @NonNull ApiRequest.Builder builder) {
        ApiRequest apiRequest = builder.build();
        return reqApi(apiMethodName, params, apiRequest);
    }

    /**
     * request the api.
     *
     * @param apiMethodName the method-name of the api in the the api service which see the third param - <code>builder</code> .
     * @param params        the params of the request.
     * @param apiRequest    the {@link ApiRequest}.
     * @return
     */
    public <R> ApiClient<T> reqApi(@NonNull final String apiMethodName, Map<String, String> params, @NonNull ApiRequest<R> apiRequest) {
//        mResponse = (Observable<T>) apiRequest.reqApi(apiMethodName, params)
//                .doOnError(new Action1<Throwable>() {
//                    @Override
//                    public void call(Throwable throwable) {
//                        Logger.e(apiMethodName+"--->"+throwable.getMessage().toString());
//                    }
//                })
//                .subscribeOn(AppApplication.getInstance().subscribeScheduler())
//                .unsubscribeOn(AppApplication.getInstance().subscribeScheduler());

        mResponse = (Observable<T>)apiRequest.reqApi(apiMethodName, params);
        if (mResponse != null) {
            mResponse = mResponse.subscribeOn(AppApplication.getInstance().subscribeScheduler())
                    .unsubscribeOn(AppApplication.getInstance().subscribeScheduler());
        }

        reset();
        return this;
    }

    /**
     * Save the response data to database.
     *
     * @param cb the callback
     * @return the <code>ApiClient</code> object.
     */
    public ApiClient<T> saveData(final Callback<T> cb) {
        if (mResponse == null) {
            Logger.w("Cannot find the response object, please make sure  the 'reqApi' method was called before to call the 'saveData' method.");
            return this;
        }
        mResponse = mResponse.doOnNext(new Action1<T>() {
            @Override
            public void call(T t) {
                if (cb != null) {
                    cb.onSuccess(t);
                }
            }
        });
        return this;
    }

    /**
     * Be used for logic operation, convert the data to the target type.
     *
     * @return
     */
    public <R> ApiClient<R> lift(final Convert<T, R> convert) {
        ApiClient<R> client = new ApiClient<>();
        if (mResponse == null) {
            Logger.w("Cannot find the response object, please make sure  the 'reqApi' method was called before to call the 'lift' method.");
            return client;
        }
        client.mResponse = mResponse.observeOn(Schedulers.computation())
                .map(new Func1<T, R>() {
                    @Override
                    public R call(T t) {
                        if (convert != null) {
                            return convert.call(t);
                        }
                        return null;
                    }
                });
        return client;
    }

    /**
     * Not udpate the UI for the response.
     * Only be used for the data request in the background.
     */
    public void updateNoUI() {
        updateUI(null);
    }

    /**
     * Update the UI.
     *
     * @param cb the callback for handle the response.
     */
    public void updateUI(final Callback<T> cb) {
        if (mResponse == null) {
            Logger.w("Cannot find the response object, please make sure the 'reqApi' method was called before to call the 'updateUI' method.");
            return;
        }
        if (cb != null) {
            mSubscription = mResponse.observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<T>() {
                        @Override
                        public void onCompleted() {
                            cb.onCompleted();
                        }

                        @Override
                        public void onError(Throwable e) {
                            cb.onError(e);
                        }

                        @Override
                        public void onNext(T t) {
                            cb.onSuccess(t);
                        }
                    });
        } else {
            mSubscription = mResponse.observeOn(Schedulers.computation())
                    .subscribe();
        }
        mResponse = null;
    }

    /**
     * Unsubsribe the api task.
     */
    public void reset() {
        if (mSubscription != null && !mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
        mSubscription = null;
    }

}
