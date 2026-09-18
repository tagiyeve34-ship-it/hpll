package com.hesabat.twopersonmessenger

import android.app.Application
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class MessengerApp: Application(), DefaultLifecycleObserver {
    override fun onCreate() {
        super<Application>.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }
    override fun onStop(owner: LifecycleOwner) {
        val p=getSharedPreferences("security",0)
        p.edit().putLong("background_at",System.currentTimeMillis()).apply()
    }
    override fun onStart(owner: LifecycleOwner) {
        val p=getSharedPreferences("security",0)
        val delay=p.getLong("auto_clear_ms",0L)
        val bg=p.getLong("background_at",0L)
        if(delay>0 && bg>0 && System.currentTimeMillis()-bg>=delay){
            val f=SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            f.timeZone=TimeZone.getTimeZone("GMT+04:00")
            p.edit().putString("cleared_before",f.format(Date())).putLong("background_at",0L).apply()
        }
    }
}