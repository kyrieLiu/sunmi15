package ys.app.pad.http;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import ys.app.pad.AppApplication;
import ys.app.pad.BuildConfig;
import ys.app.pad.Constants;
import ys.app.pad.utils.AppInfoUtils;
import ys.app.pad.utils.Logger;

/**
 * the API handler for the http(s) request.
 * Created by dennis on 2016/12/15 19:43
 * for ebrun
 * you can contact me at : pengjianjun@ebrun.com
 * or see link for more details
 *
 * @version 1.0.0
 */
public class ApiRequest<T> {

    public static int DEFAULT_TIMEOUT = 20;

    private static final AtomicReference<ApiRequest> INSTANCE = new AtomicReference<>();

    private final static AtomicReference<ApiRequest> JSON_INSTANCE = new AtomicReference<>();

    public List<String> servers;

    private String baseUrl;
    private Context context;
    private RespDataType respDataType;
    private String cacheControl;
    private String userAgent;
    private int sslCertFileResId;
    private int retryCount;

    public enum RespDataType {RESPONSE_XML, RESPONSE_JSON}

    /**
     * <p>the 'apiService' property-value is {@link ApiService}</p>
     * <p>the 'respDataType' property-value is {@link RespDataType} - <code>RESPONSE_XML</code></p>
     * @return
     */
    public static ApiRequest getDefaultInstance()
    {
        while (true)
        {
            ApiRequest current = INSTANCE.get();
            if (current != null)
            {
                return current;
            }
            current = new ApiRequest();
            if (INSTANCE.compareAndSet(null, current))
            {
                return current;
            }
        }
    }

    /**
     * <p>the 'apiService' property-value is {@link ApiService}</p>
     * <p>the 'respDataType' property-value is {@link RespDataType} - <code>RESPONSE_JSON</code></p>
     * @return
     */
    public static ApiRequest getDefaultJsonInstance()
    {
        while (true)
        {
            ApiRequest current = JSON_INSTANCE.get();
            if (current != null)
            {
                return current;
            }
            current = new ApiRequest.Builder(AppApplication.getAppContext()).respDataType(ApiRequest.RespDataType.RESPONSE_JSON).build();
            if (JSON_INSTANCE.compareAndSet(null, current))
            {
                return current;
            }
        }
    }

    private T mApiService;

    private ApiRequest(){
        this(new Builder(AppApplication.getAppContext()));
    }

    private ApiRequest(Builder builder) {
        this.context = builder.context;
        this.baseUrl = builder.baseUrl;
        this.respDataType = builder.respDataType;
        this.cacheControl = builder.cacheControl;
        this.userAgent = builder.userAgent;
        this.sslCertFileResId = builder.sslCertFileResId;
        this.retryCount = builder.retryCount;
        this.mApiService = createApiService((Class<T>) builder.apiServiceCls);
    }

    private T createApiService(Class<T> cls) {
        OkHttpClient.Builder okBuilder = new OkHttpClient.Builder();
        okBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                 .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                 .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        if (retryCount > 0) {
            okBuilder.retryOnConnectionFailure(true)
                     .addInterceptor(new RetryAndChangeIpInterceptor(baseUrl, servers, retryCount));
        }
//        okBuilder.addNetworkInterceptor(new CacheInterceptor())
//                 .cache(new CacheProvide(context).provideCache());
        if (sslCertFileResId > 0) {
            try {
                InputStream inputStream = context.getResources().openRawResource(sslCertFileResId);
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

                CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
                Certificate certificate = certificateFactory.generateCertificate(inputStream);
                KeyStore keyStore = KeyStore.getInstance("PKCS12", "BC");
                keyStore.load(null, null);
                keyStore.setCertificateEntry("trust", certificate);

                trustManagerFactory.init(keyStore);
                TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
                if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                    throw new IllegalStateException("Unexpected default trust managers:"
                            + Arrays.toString(trustManagers));
                }
                X509TrustManager trustManager = (X509TrustManager) trustManagers[0];

                SSLContext sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, new TrustManager[]{trustManager}, null);
                SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
                okBuilder.sslSocketFactory(sslSocketFactory, trustManager);
            }catch (Exception e){
                Logger.w("Failure to create SSLSocketFactory.", e);
            }
        }


        if (BuildConfig.DEBUG) {
            okhttp3.logging.HttpLoggingInterceptor loggingInterceptor = new okhttp3.logging.HttpLoggingInterceptor();
            loggingInterceptor.setLevel(okhttp3.logging.HttpLoggingInterceptor.Level.BODY);
            okBuilder.addInterceptor(loggingInterceptor);
        }

        OkHttpClient client = okBuilder.build();

        Retrofit.Builder builder = new Retrofit.Builder();
        if (!TextUtils.isEmpty(baseUrl)) {
            builder.baseUrl(baseUrl);
        }
        if (respDataType == RespDataType.RESPONSE_JSON) {
            builder.addConverterFactory(GsonConverterFactory.create());
        }
        Retrofit retrofit = builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                                   .client(client)
                                   .build();
        T Service = retrofit.create(cls);
        return Service;
    }

    /**
     * send the api request, and get the response which was convert the 'Observable' object.
     * @param methodName the request parameters.
     * @return Observable
     */
    public <R extends Response> Observable<R> reqApi(@NonNull String methodName, Map<String, String> params) {
        try {
            Method m = mApiService.getClass().getDeclaredMethod(methodName, Map.class, String.class, String.class);
            if (m != null)
            {
//                return (Observable<R>) m.invoke(mApiService, checkParams(params), cacheControl, userAgent);
                return (Observable<R>) m.invoke(mApiService, params, cacheControl, userAgent);
            }
        } catch (NoSuchMethodException e) {
            Logger.e("Occure error when execute the api request[methodName:"+methodName+"] in the ApiService..", e);
        } catch (InvocationTargetException e) {
            Logger.e("Occure when the method[methodName:"+methodName+"] was invoked in the ApiService.", e);
        } catch (IllegalAccessException e) {
            Logger.e("Occure when the method[methodName:"+methodName+"] was invoked in the ApiService.", e);
        }
        return null;
    }

    /**
     * send the api request, and get the response which was convert the 'Observable' object.
     * @param methodName the request parameters.
     * @return Observable
     */
    public <R extends Response> Observable<R> reqUploadApi(@NonNull String methodName,  MultipartBody.Part part) {
        try {
            Method m = mApiService.getClass().getDeclaredMethod(methodName, MultipartBody.Part.class);
            if (m != null)
            {
//                return (Observable<R>) m.invoke(mApiService, checkParams(params), cacheControl, userAgent);
                return (Observable<R>) m.invoke(mApiService, part);
            }
        } catch (NoSuchMethodException e) {
            Logger.e("Occure error when execute the api request[methodName:"+methodName+"] in the ApiService..", e);
        } catch (InvocationTargetException e) {
            Logger.e("Occure when the method[methodName:"+methodName+"] was invoked in the ApiService.", e);
        } catch (IllegalAccessException e) {
            Logger.e("Occure when the method[methodName:"+methodName+"] was invoked in the ApiService.", e);
        }
        return null;
    }

    /**
     * set switch the 'sBaseUrl' when the request retry.
     *
     * @param servers
     */
    public void setServers(List<String> servers) {
        servers = servers;
    }

    private Map<String, String> checkParams(Map<String, String> params) {
        if (params == null) {
            params = new HashMap<>();
        }

        if (!params.containsKey("deviceId")) {
            String deviceId = AppInfoUtils.getDeviceId(context);
            params.put("deviceId", deviceId);
        }

        String channel = AppInfoUtils.getMetaChannel(context);
        if (!TextUtils.isEmpty(channel)) {
            params.put("appChannel", channel);
        }
        params.put("versionCode", AppInfoUtils.getVersionCode(context));
        params.put("versionName", AppInfoUtils.getVersionName(context));

        // param handle:  null value - > blank value.
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue() == null) {
                params.put(entry.getKey(), "");
            }
        }
        return params;
    }

    public final static class Builder {

        private Context context;
        private String baseUrl;
        private String cacheControl;
        private String userAgent;
        private RespDataType respDataType;
        private int sslCertFileResId;  // -1：not exists
        private int retryCount;
        private Class<?> apiServiceCls;

        /**
         * ApiRequest Builder.
         * the default base url is
         * @param context
         */
        public Builder(@NonNull Context context)
        {
            this(context, Constants.base_url);
        }

        /**
         * ApiRequest Builder.
         * @param context
         * @param baseUrl  the default base url.
         */
        public Builder(@NonNull Context context, String baseUrl) {
            Context applicationContext;
            if ( context instanceof Application) {
                applicationContext = context;
            } else {
                applicationContext = context.getApplicationContext();
            }
            this.context = applicationContext;
            this.baseUrl = baseUrl;
            this.cacheControl = "no-cache";
            this.userAgent = AppApplication.getInstance().userAgent();
            this.respDataType = RespDataType.RESPONSE_XML;
            this.sslCertFileResId = -1;
            this.retryCount = -1; //if it fail for the http request, don't retry.
            this.apiServiceCls = ApiService.class;
        }

        /**
         * get the base url.
         * @return
         */
        public String getBaseUrl() {
            return baseUrl;
        }

        /**
         * set the base url.
         * @param baseUrl
         */
        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }

        /**
         * set the response data type (xml 、 json、 inputstream)
         * @param respDataType
         * @return
         */
        public Builder respDataType(@NonNull RespDataType respDataType)
        {
            this.respDataType = respDataType;
            return this;
        }

        /**
         * set the request header (Cache-Control)
         * @param cache    such as : "no-cache"   、  "max-age:900" (eq: 15 minutes)
         * @return
         */
        public Builder cacheControl(@NonNull String cache) {
            this.cacheControl = cache;
            return this;
        }
        /**
         * set the request header (User-Agent)
         * @param userAgent
         * @return
         */
        public Builder userAgent(@NonNull String userAgent)
        {
            this.userAgent = userAgent;
            return this;
        }

        /**
         *  cert file id in the directory( res/raw/xxx.keystore), such as: R.raw.usercert.
         *  default -1: not exists
         * @param sslCertFileResId
         * @return
         */
        public Builder sslCertFileResId(int sslCertFileResId)
        {
            this.sslCertFileResId = sslCertFileResId;
            return this;
        }

        /**
         * set the retry count.  default is -1(don't retry)
         * @param retryCount  retry count.
         * @return
         */
        public Builder setRetryCount(int retryCount)
        {
            this.retryCount = retryCount;
            return this;
        }


        public Builder apiService(Class<?> apiServiceCls){
            this.apiServiceCls = apiServiceCls;
            return this;
        }
        public ApiRequest build()
        {
            return new ApiRequest(this);
        }

    }
}
