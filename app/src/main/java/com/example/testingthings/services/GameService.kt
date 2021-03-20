package com.example.testingthings.services

import android.app.Service
import android.content.Intent
import android.os.*
import android.util.Log
import android.widget.Chronometer
import java.lang.ref.WeakReference

class GameService: Service() {
    private val LOG_TAG = GameService::class.java.simpleName
    private var mChronometer: Chronometer? = null

    companion object {
        val MSG_GET_TIMESTAMP = 1000
        val MSG_RESET_TIMESTAMP = 1001
    }

    internal class BoundServiceHandler(service: GameService) : Handler(Looper.getMainLooper()) {
        private val mService: WeakReference<GameService>
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                MSG_GET_TIMESTAMP -> {
                    val elapsedMillis = (SystemClock.elapsedRealtime()
                            - mService.get()!!.mChronometer!!.base)
                    val hours = (elapsedMillis / 3600000).toInt()
                    val minutes = (elapsedMillis - hours * 3600000).toInt() / 60000
                    val seconds = (elapsedMillis - hours * 3600000 - (minutes
                            * 60000)).toInt() / 1000
                    val millis = (elapsedMillis - hours * 3600000 - (minutes
                            * 60000) - seconds * 1000).toInt()
                    val activityMessenger = msg.replyTo
                    val b = Bundle()
                    b.putString(
                            "timestampString", hours.toString() + ":" + minutes + ":" + seconds
                            + ":" + millis
                    )
                    b.putLong(
                            "timestampLong", elapsedMillis
                    )
                    val replyMsg: Message = Message.obtain(null, MSG_GET_TIMESTAMP)
                    replyMsg.data = b
                    try {
                        activityMessenger.send(replyMsg)
                    } catch (e: RemoteException) {
                        e.printStackTrace()
                    }
                }
                MSG_RESET_TIMESTAMP -> {
                    mService.get()!!.mChronometer!!.base = SystemClock.elapsedRealtime()
                    mService.get()!!.mChronometer!!.start()
                }
                else -> super.handleMessage(msg)
            }
        }

        init {
            mService = WeakReference(service)
        }
    }

    val mMessenger = Messenger(BoundServiceHandler(this))

    override fun onCreate() {
        super.onCreate()
        Log.v(LOG_TAG, "in onCreate")
        mChronometer = Chronometer(this)
        mChronometer!!.base = SystemClock.elapsedRealtime()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.v(LOG_TAG, "in onBind")
        return mMessenger.binder
    }

    override fun onRebind(intent: Intent?) {
        Log.v(LOG_TAG, "in onRebind")
        super.onRebind(intent)
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.v(LOG_TAG, "in onUnbind")
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.v(LOG_TAG, "in onDestroy")
        mChronometer!!.stop()
    }
}