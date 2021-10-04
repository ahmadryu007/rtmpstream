package com.cocorolive.rtmpstream.rtmpstream

import android.os.Bundle
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pedro.rtmp.utils.ConnectCheckerRtmp
import com.pedro.rtplibrary.rtmp.RtmpCamera1
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class CameraDemoActivity : AppCompatActivity(), ConnectCheckerRtmp, SurfaceHolder.Callback {

    private lateinit var rtspServerCamera1: RtmpCamera1
    private var currentDateAndTime = ""
    private lateinit var folder: File
    lateinit var surfaceView: SurfaceView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_camera_demo)
        folder = File(getExternalFilesDir(null)!!.absolutePath + "/rtmp-rtsp-stream-client-java")
        surfaceView = findViewById(R.id.surfaceView)
        rtspServerCamera1 = RtmpCamera1(surfaceView, this)
        surfaceView.holder.addCallback(this)
//        Handler(Looper.myLooper()!!).postDelayed({
//            startStreaming("rtmp://cocorolife-api-development.inagri.asia:1935/live/KfRtdu0XWLhalWlvv5xGXPDNgNERUdkOmVUYBgPty8ZU0eXe");
//        },2000)
    }

    fun startStreaming(rtmpUrl: String) {
        try {
            if (!folder.exists()) {
                folder.mkdir()
            }
            val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
            currentDateAndTime = sdf.format(Date())
            if (!rtspServerCamera1.isStreaming) {
                if (rtspServerCamera1.prepareAudio() && rtspServerCamera1.prepareVideo()) {
                    rtspServerCamera1.startRecord(folder.absolutePath + "/" + currentDateAndTime + ".mp4")
                    rtspServerCamera1.startStream(rtmpUrl);
                    Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                            this, "Error preparing stream, This device cant do it",
                            Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                rtspServerCamera1.startRecord(folder.absolutePath + "/" + currentDateAndTime + ".mp4")
                Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            rtspServerCamera1.stopRecord()
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
        }
    }

    fun stopStreaming() {
        rtspServerCamera1.stopRecord()
        rtspServerCamera1.stopStream()
        Toast.makeText(
                this, "file " + currentDateAndTime + ".mp4 saved in " + folder.absolutePath,
                Toast.LENGTH_SHORT
        ).show()
    }

    override fun onBackPressed() {
//        stopStreaming()
        super.onBackPressed()
    }


//    override fun onClick(view: View) {
//        if (!rtspServerCamera1.isRecording) {
//            try {
//                if (!folder.exists()) {
//                    folder.mkdir()
//                }
//                val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault())
//                currentDateAndTime = sdf.format(Date())
//                if (!rtspServerCamera1.isStreaming) {
//                    if (rtspServerCamera1.prepareAudio() && rtspServerCamera1.prepareVideo()) {
//                        rtspServerCamera1.startRecord(folder.absolutePath + "/" + currentDateAndTime + ".mp4")
//                        rtspServerCamera1.startStream("rtmp://cocorolife-api-development.inagri.asia:1935/live/KfRtdu0XWLhalWlvv5xGXPDNgNERUdkOmVUYBgPty8ZU0eXe");
//                        Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
//                    } else {
//                        Toast.makeText(
//                            this, "Error preparing stream, This device cant do it",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                } else {
//                    rtspServerCamera1.startRecord(folder.absolutePath + "/" + currentDateAndTime + ".mp4")
//                    Toast.makeText(this, "Recording... ", Toast.LENGTH_SHORT).show()
//                }
//            } catch (e: IOException) {
//                rtspServerCamera1.stopRecord()
//                Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
//            }
//        } else {
//            rtspServerCamera1.stopRecord()
//            rtspServerCamera1.stopStream()
//            Toast.makeText(
//                this,
//                "file " + currentDateAndTime + ".mp4 saved in " + folder.absolutePath,
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    override fun surfaceCreated(holder: SurfaceHolder) {}

    override fun surfaceChanged(surfaceHolder: SurfaceHolder, i: Int, i1: Int, i2: Int) {
        rtspServerCamera1.startPreview()
    }

    override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (rtspServerCamera1.isRecording) {
                rtspServerCamera1.stopRecord()
                Toast.makeText(
                        this,
                        "file " + currentDateAndTime + ".mp4 saved in " + folder.absolutePath,
                        Toast.LENGTH_SHORT
                ).show()
                currentDateAndTime = ""
            }
        }
        if (rtspServerCamera1.isStreaming) {
            rtspServerCamera1.stopStream()
        }
        rtspServerCamera1.stopPreview()
    }

    override fun onAuthErrorRtmp() {
        runOnUiThread {
            Toast.makeText(this@CameraDemoActivity, "Auth error", Toast.LENGTH_SHORT).show()
            rtspServerCamera1.stopStream()
        }
    }

    override fun onAuthSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@CameraDemoActivity, "Auth success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onConnectionFailedRtmp(reason: String) {
        runOnUiThread {
            Toast.makeText(
                    this@CameraDemoActivity,
                    "Connection failed. $reason",
                    Toast.LENGTH_SHORT
            )
                    .show()
            rtspServerCamera1.stopStream()
        }
    }

    override fun onConnectionStartedRtmp(rtmpUrl: String) {
    }

    override fun onConnectionSuccessRtmp() {
        runOnUiThread {
            Toast.makeText(this@CameraDemoActivity, "Connection success", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDisconnectRtmp() {
        runOnUiThread {
            Toast.makeText(this@CameraDemoActivity, "Disconnected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onNewBitrateRtmp(bitrate: Long) {
    }

}