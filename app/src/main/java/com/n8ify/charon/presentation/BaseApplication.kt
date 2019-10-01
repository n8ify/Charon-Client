package com.n8ify.charon.presentation

import android.app.Application
import android.content.Intent
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.n8ify.charon.BuildConfig
import com.n8ify.charon.R
import com.n8ify.charon.module.appModule
import com.n8ify.charon.presentation._base.activity.ErrorActivity
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // Step [1] : Plant timber debug tree if on debug mode.
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        // Step [2] : Initial Koin module.
        val koin = startKoin {
            androidContext(this@BaseApplication.applicationContext)
            modules(appModule)
        }

        // Step [3] : Setup uncaught exception handler.
        Thread.UncaughtExceptionHandler(function = fun(t: Thread?, e: Throwable?){
            val errorIntent = Intent(this@BaseApplication, ErrorActivity::class.java).apply {
                this@apply.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                this@apply.putExtra("exception", e?.message)
            }
            startActivity(errorIntent)
            Toast.makeText(this@BaseApplication, getString(R.string.exception), Toast.LENGTH_LONG).show()
        } )

    }

}