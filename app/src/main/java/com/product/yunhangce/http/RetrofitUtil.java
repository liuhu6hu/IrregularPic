package com.product.yunhangce.http;

import com.product.yunhangce.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitUtil {
    private static RetrofitUtil mInstance;
    private final long DEFAULT_TIMEOUT = 10;

    //    private  final String APP_HOST="https://easy-mock.com/mock/5b8e1b8dae6b714d1bc70096/example/";
    private final String APP_HOST = "http://yunhangce.com/api/";
    public static final String APP_VERSION = "http://yunhangce.com/api/v1/ver/check";
    private final Retrofit mRetrofit;
    private final ApiService mApiService;

    /**
     *
     */
    private RetrofitUtil() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            // Log信息拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//这里可以选择拦截级别

            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }

        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(builder.build())
                .baseUrl(APP_HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mApiService = mRetrofit.create(ApiService.class);
    }

    public static RetrofitUtil getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitUtil.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitUtil();
                }
            }
        }
        return mInstance;
    }

    public void registerLicense(String deviceId, String value, Callback<Response> callback) {
        Call<Response> call = mApiService.registerLicense(deviceId, value);
        call.enqueue(callback);
    }

    public void info(String deviceId, String value, Callback<CommonResponse> callback) {
        Call<CommonResponse> call = mApiService.info(deviceId, value);
        call.enqueue(callback);
    }
}
