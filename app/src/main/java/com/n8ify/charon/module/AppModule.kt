package com.n8ify.charon.module

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
import java.util.concurrent.TimeUnit

val appModule = org.koin.dsl.module {
    single { provideRetrofit().create(CategoryAPI::class.java) }
    single { provideRetrofit().create(ItemAPI::class.java) }
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

fun provideOkHttp() =
    OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply { this@apply.level = HttpLoggingInterceptor.Level.BODY })
        .readTimeout(1, TimeUnit.MINUTES)
        .connectTimeout(1, TimeUnit.MINUTES)
        .build()

fun provideRetrofit() =
    Retrofit.Builder()
        .client(provideOkHttp())
        .baseUrl(com.n8ify.charon.BuildConfig.BASE_URL)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat("dd/MM/yyyy HH:mm:ss").create()))
        .build()
