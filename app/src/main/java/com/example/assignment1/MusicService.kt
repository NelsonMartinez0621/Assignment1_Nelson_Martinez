package com.example.assignment1

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder


class MusicService : Service(){
    private var mediaPlayer: MediaPlayer? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mediaPlayer = MediaPlayer.create(this,R.raw.background_music)
        mediaPlayer?.let {
            it.isLooping = true
            it.start()
        }
        return START_STICKY
    }

    override fun onDestroy() {
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        super.onDestroy()
    }
}