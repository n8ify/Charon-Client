package com.n8ify.charon.module

import android.content.Context
import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.n8ify.charon.data.api.CategoryAPI
import com.n8ify.charon.data.api.ItemAPI
import com.n8ify.charon.data.repository.CategoryRepository
import com.n8ify.charon.data.repository.HistoryRepository
import com.n8ify.charon.data.repository.ItemRepository
import com.n8ify.charon.data.repository.impl.CategoryRepositoryImpl
import com.n8ify.charon.data.repository.impl.HistoryRepositoryImpl
import com.n8ify.charon.data.repository.impl.ItemRepositoryImpl
import com.n8ify.charon.presentation._base.viewmodel.CategoryViewModel
import com.n8ify.charon.presentation._base.viewmodel.ItemViewModel
import com.n8ify.charon.presentation._base.viewmodel.SensorViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.util.concurrent.TimeUnit
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.security.SecureRandom


val appModule = org.koin.dsl.module {
    single { provideRetrofit(context = get()).create(CategoryAPI::class.java) }
    single { provideRetrofit(context = get()).create(ItemAPI::class.java) }
    factory<CategoryRepository> { CategoryRepositoryImpl(categoryAPI = get()) }
    factory<ItemRepository> { ItemRepositoryImpl(itemAPI = get()) }
    factory<HistoryRepository> { HistoryRepositoryImpl() }
    viewModel { CategoryViewModel(categoryRepository = get(), application = get()) }
    viewModel {
        ItemViewModel(
            itemRepository = get(),
            historyRepository = get(),
            application = get()
        )
    }
    viewModel { SensorViewModel(application = get()) }
}

fun provideOkHttp(context: Context) =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            this@apply.level = HttpLoggingInterceptor.Level.BODY
        })
        .readTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .socketFactory(getSSLContext(context).socketFactory)
        .build()

fun provideRetrofit(context: Context) =
    Retrofit.Builder()
        .client(provideOkHttp(context))
        .baseUrl(com.n8ify.charon.BuildConfig.BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create()))
        .build()

fun getSSLContext(context: Context): SSLContext {

    val certificateFactory = CertificateFactory.getInstance("X.509")
    var caInput = BufferedInputStream(context.resources.openRawResource(com.n8ify.charon.R.raw.charon_local))

    val certificate = certificateFactory.generateCertificate(caInput).also {
        caInput.close()
    }

    val keyStoreType = KeyStore.getDefaultType()
    val keyStore = KeyStore.getInstance(keyStoreType)
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", certificate);

    val trustAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
    val trustManagerFactory = TrustManagerFactory.getInstance(trustAlgorithm)
    trustManagerFactory.init(keyStore);

    return SSLContext.getInstance("TLSv1.2").also {
        it.init(null, trustManagerFactory.trustManagers, SecureRandom())
    }

}