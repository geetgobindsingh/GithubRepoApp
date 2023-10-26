package com.geetgobindingh.githubrepoapp.di.module

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.geetgobindingh.githubrepoapp.BuildConfig
import com.geetgobindingh.githubrepoapp.data.DataManager
import com.geetgobindingh.githubrepoapp.data.DataManagerImpl
import com.geetgobindingh.githubrepoapp.data.mapper.githubrepository.GithubRepositoryMapper
import com.geetgobindingh.githubrepoapp.data.network.NetworkHelper
import com.geetgobindingh.githubrepoapp.data.prefs.SharedPrefHelper
import com.geetgobindingh.githubrepoapp.repository.trending.TrendingRepository
import com.geetgobindingh.githubrepoapp.repository.trending.TrendingRepositoryImpl
import com.geetgobindingh.githubrepoapp.util.api.NetworkUtil
import com.gobasco.gobascoapp.data.db.AppDatabase
import com.gobasco.gobascoapp.data.db.DataBaseHelper
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    internal fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    fun provideAppDatabase(
        @ApplicationContext mApplicationContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            mApplicationContext,
            AppDatabase::class.java,
            "gojek_sample_db"
        ).build()
    }

    @Provides
    fun provideDataManager(
        sharedPrefHelper: SharedPrefHelper,
        dataBaseHelper: DataBaseHelper,
        networkHelper: NetworkHelper
    ): DataManager {
        return DataManagerImpl(sharedPrefHelper, dataBaseHelper, networkHelper)
    }

    @Provides
    fun provideSharedPreferences(
        @ApplicationContext mApplicationContext: Context
    ): SharedPreferences {
        return mApplicationContext.getSharedPreferences(
            "gojek_sample_pref",
            Context.MODE_PRIVATE
        )
    }

    @Provides
    fun provideRetrofit(
        @ApplicationContext context: Context,
        gson: Gson
    ): Retrofit {
        val httpCacheDirectory = File(context.cacheDir, "resources")

        val cache = Cache(httpCacheDirectory, (10 * 1024 * 1024).toLong())

        val okHttpBuilder = OkHttpClient.Builder()

        okHttpBuilder.cache(cache).connectTimeout(30, TimeUnit.SECONDS)

        okHttpBuilder.addInterceptor { chain ->

            if (NetworkUtil.isConnected) {
                val originalRequest = chain.request()
                val builder = originalRequest.newBuilder()

                chain.proceed(builder.build())
            } else {
                throw IOException("No Internet")
            }
        }.addInterceptor { chain ->
            var request = chain.request()
            val builder = request.newBuilder()

            request = builder.build()
            val response = chain.proceed(request)

            if (response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {
                //
            } else if (response.code == HttpURLConnection.HTTP_FORBIDDEN) {
                // Forbidden error
            } else if (response.code == HttpURLConnection.HTTP_NOT_FOUND) {
                // Not Found error
            } else if (response.code >= HttpURLConnection.HTTP_BAD_REQUEST && response.code < HttpURLConnection.HTTP_INTERNAL_ERROR) {
                // Between 400 and 499
            } else {
                if (response.code == HttpURLConnection.HTTP_GATEWAY_TIMEOUT) {
                    // network request error
                } else if (response.code >= HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    // network internal server error
                }
            }

            response
        }

        val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                Timber.tag("OkHttp").d(message)
            }
        })
        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }
        okHttpBuilder.addInterceptor(interceptor)

        val restAdapter = Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpBuilder.build())
            //.addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return restAdapter
    }

    @Provides
    internal fun provideTrendingRepository(
        dataManager: DataManager
    ): TrendingRepository {
        return TrendingRepositoryImpl(dataManager, GithubRepositoryMapper())
    }
}