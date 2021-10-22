package com.cocorolive.rtmpstream.rtmpstream

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.SurfaceView
import android.view.View
import android.widget.TextView
import io.flutter.plugin.platform.PlatformView
import android.content.ContextWrapper
import android.util.Log
import com.pedro.rtplibrary.view.OpenGlView


internal class NativeView(context: Context, id: Int, creationParams: Map<String?, Any?>?) : PlatformView {
    private var view: View = LayoutInflater.from(context).inflate(R.layout.activity_camera_demo, null)
    var openGLSurfaceView: OpenGlView
    var rtmpServer: RtmpServer

    override fun getView(): View {
        return view
    }


    override fun dispose() {
        rtmpServer.stopStreaming()
        Log.d("STREAMING", "dispose NativeView")
    }

    init {
        Log.d("STREAMING", "init NativeView")
        openGLSurfaceView = view.findViewById<OpenGlView>(R.id.surfaceView)
        rtmpServer = RtmpServer(Activity(), openGLSurfaceView)
        rtmpServer.initOpenGL(context.applicationContext)
        rtmpServer.startStreaming(creationParams?.get("rtmpUrl") as String)

    }
}
