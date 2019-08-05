package co.selim.starter

import co.selim.starter.api.BookingApi
import co.selim.starter.api.BookingService
import co.selim.starter.booking.BookingViewModel
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

private const val TEN_MEBIBYTES = 10 * 1024 * 1024

val bookingAppModule = module {
    single {
        val cacheSize = TEN_MEBIBYTES
        Cache(androidContext().cacheDir, cacheSize.toLong())
    }

    single {
        val cache = get<Cache>()

        val builder = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor { Timber.d(it) }
            logging.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(logging)
        }

        builder.cache(cache)
            .build()
    }

    single {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val client = get<OkHttpClient>()
        Retrofit.Builder()
            .baseUrl("http://raspberrypi.fritz.box:8080")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(client)
            .build()
    }

    single {
        get<Retrofit>()
            .create(BookingApi::class.java)
    }

    single {
        BookingService()
    }

    viewModel {
        BookingViewModel()
    }
}
