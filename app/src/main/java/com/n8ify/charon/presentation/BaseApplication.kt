package com.n8ify.charon.presentation

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.crashlytics.android.Crashlytics
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
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        // Step [2] : Initial Koin module.
        val koin = startKoin {
            androidContext(this@BaseApplication.applicationContext)
            modules(appModule)
        }

        // Step [3] : Setup Crashlytics.
        Crashlytics.setUserIdentifier(Settings.Secure.getString(applicationContext.contentResolver, Settings.Secure.ANDROID_ID))

        // Step [4] : Setup uncaught exception handler.
        val defaultUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(Thread.UncaughtExceptionHandler(function = fun(
            t: Thread?,
            e: Throwable?
        ) {

            Crashlytics.logException(e)

            val errorIntent = Intent(this@BaseApplication, ErrorActivity::class.java).apply {

                this@apply.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_FROM_BACKGROUND
                this@apply.putExtra("exception", e?.toString())
            }

//            Toast.makeText(this@BaseApplication, getString(R.string.exception), Toast.LENGTH_LONG).show()

            val alarmManager = baseContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.set(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                2000,
                PendingIntent.getActivity(baseContext, 0, errorIntent, PendingIntent.FLAG_ONE_SHOT)
            )

            defaultUncaughtHandler?.uncaughtException(t, e)
                ?: run { System.exit(2) }

        }))

    }

}