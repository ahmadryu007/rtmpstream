package com.cocorolive.rtmpstream.rtmpstream

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import com.pedro.rtplibrary.view.OpenGlView
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class RtmpServer : ConnectCheckerRtmp, SurfaceHolder.Callback {

    var activity: Activity
    lateinit var openGlView: OpenGlView
    lateinit var surfaceView: SurfaceView
    private var currentDateAndTime = ""
    private lateinit var folder: File
    private lateinit var rtspServerCamera1: RtmpCamera1

//    constructor(activity: Activity, openGlView: OpenGlView) {
//        this.openGlView = openGlView
//        this.activity = activity
//    }

    constructor(activity: Activity, surfaceView: SurfaceView) {
        this.activity = activity
        this.surfaceView = surfaceView
    }


    fun initOpenGL(context: Context) {
        folder =
                File(context.getExternalFilesDir(null)!!.absolutePath + "/rtmp-rtsp-stream-client-java")
        rtspServerCamera1 = RtmpCamera1(openGlView, this)
        rtspServerCamera1.switchCamera()
    }

    fun initSurfaceView(context: Context) {
        Log.d("STREAMING", "initSurfaceView")
        folder = File(context.getExternalFilesDir(null)!!.absolutePath + "/rtmp-rtsp-stream-client-java")
        rtspServerCamera1 = RtmpCamera1(surfaceView, this)
        rtspServerCamera1.switchCamera()
    }


    fun gantiKamera(){
        rtspServerCamera1.switchCamera()
    }

    fun startStreaming(rtmpUrl: String) {
        Handler(Looper.myLooper()!!).postDelayed({
            try {
                if (!folder.exists()) {
                    folder.mkdir()
                    Log.d("STREAMING", "folder dibuat")
                }

                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
                currentDateAndTime = sdf.format(Date())
                if (!rtspServerCamera1.isStreaming) {
                    Log.d("STREAMING", "start Streaming server")
                    if (rtspServerCamera1.prepareAudio() && rtspServerCamera1.prepareVideo()) {
                        rtspServerCamera1.startRecord(folder.absolutePath + "/" + currentDateAndTime + ".mp4")
                        rtspServerCamera1.startStream(rtmpUrl);
                    }
                } else {
                    Log.d("STREAMING", "start Streaming")
                    rtspServerCamera1.startRecord(folder.absolutePath + "/" + currentDateAndTime + ".mp4")
                }
            } catch (e: IOException) {
                rtspServerCamera1.stopRecord()
            }
        },2000)
    }

    fun stopStreaming() {
        Log.d("STREAMING", "Stop Streaming")
        rtspServerCamera1.stopRecord()
        rtspServerCamera1.stopStream()
    }

    override fun onAuthErrorRtmp() {
        Log.d("STREAMING", "onAuthErrorRtmp")
        activity.runOnUiThread {
            rtspServerCamera1.stopStream()
        }
    }

    override fun onAuthSuccessRtmp() {
        Log.d("STREAMING", "onAuthSuccessRtmp")
        activity.runOnUiThread {
        }
    }

    override fun onConnectionFailedRtmp(reason: String) {
        Log.d("STREAMING", "onConnectionFailedRtmp")
        activity.runOnUiThread {
            rtspServerCamera1.stopStream()
        }
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {
        Log.d("STREAMING", "onConnectionStartedRtmp")
    }

    override fun onConnectionSuccessRtmp() {
        Log.d("STREAMING", "onConnectionSuccessRtmp")
    }

    override fun onDisconnectRtmp() {
        Log.d("STREAMING", "onDisconnectRtmp")
        activity.runOnUiThread {
        }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
        Log.d("STREAMING", "onNewBitrateRtmp")
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d("STREAMING", "onConnectionSuccessRtmp")
    }


    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        Log.d("STREAMING", "surfaceChanged")
        rtspServerCamera1.startPreview()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        Log.d("STREAMING", "destroy")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (rtspServerCamera1.isRecording) {
                rtspServerCamera1.stopRecord()
                currentDateAndTime = ""
            }
        }
        if (rtspServerCamera1.isStreaming) {
            rtspServerCamera1.stopStream()
        }
    }
}