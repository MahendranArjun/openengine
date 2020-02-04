package com.electronslab.voip.rtc

import android.content.Context
import android.os.Handler
import android.os.Looper
import org.webrtc.*
import kotlin.concurrent.thread


class RtcVideoRenderer(private val localVideoView:SurfaceViewRenderer?, private val remoteVideoView:SurfaceViewRenderer?) :RtcRenderer {

    private var videoSource:VideoSource? = null
    private var audioSource:AudioSource? = null

    private var localVideoTrack:VideoTrack? = null
    private var localAudioTrack:AudioTrack? = null

    private var localRenderer:VideoRenderer? = null
    private var remoteRenderer:VideoRenderer? = null
    private var factory:PeerConnectionFactory? = null

    private val handler: Handler? = Handler(Looper.getMainLooper())
    private var rootEglBase: EglBase? = null
    private var videoCapturer :VideoCapturerAndroid? = null

    override fun create(context: Context): PeerConnectionFactory {

        rootEglBase = EglBase.create()
        localVideoView?.init(rootEglBase!!.eglBaseContext, null)
        remoteVideoView?.init(rootEglBase!!.eglBaseContext, null)

        PeerConnectionFactory.initializeAndroidGlobals(context, true, true, true)

        val options = PeerConnectionFactory.Options()
        val peerConnectionFactory = PeerConnectionFactory()
        peerConnectionFactory.setOptions(options)
        factory = peerConnectionFactory

        thread {

            handler?.post {

                videoCapturer =
                    VideoCapturerAndroid.create(CameraEnumerationAndroid.getNameOfBackFacingDevice()) as VideoCapturerAndroid

                val audioConstraints = MediaConstraints()
                val videoConstraints = MediaConstraints()

                videoSource =
                    peerConnectionFactory.createVideoSource(videoCapturer, videoConstraints)
                localVideoTrack = peerConnectionFactory.createVideoTrack(VIDEO_ID, videoSource)

                audioSource = peerConnectionFactory.createAudioSource(audioConstraints)
                localAudioTrack = peerConnectionFactory.createAudioTrack(AUDIO_ID, audioSource)

                if (localVideoView != null) localRenderer = VideoRenderer(localVideoView)
                localVideoTrack?.addRenderer(localRenderer)
            }

        }


        return peerConnectionFactory
    }



    fun change(){

        videoCapturer?.switchCamera(object : VideoCapturerAndroid.CameraSwitchHandler {
            override fun onCameraSwitchDone(p0: Boolean) {
                println("RtcVideoRenderer.onCameraSwitchDone")
                println("p0 = [${p0}]")
            }

            override fun onCameraSwitchError(p0: String?) {
                println("RtcVideoRenderer.onCameraSwitchError")
                println("p0 = [${p0}]")
            }
        })
    }


    override fun setRemoteStream(stream: MediaStream) {
        val videoTrack = stream.videoTracks.first
        handler?.post {
            remoteRenderer = VideoRenderer(remoteVideoView)
            videoTrack.addRenderer(remoteRenderer)
        }

    }

    override fun getLocalStream(): MediaStream {
        val stream = factory?.createLocalMediaStream(MEDIA_ID)?:throw IllegalAccessException("Factory not created")
        stream.addTrack(localAudioTrack)
        stream.addTrack(localVideoTrack)
        return stream
    }

    override fun clean() {
        videoSource?.dispose()
        audioSource?.dispose()

        localVideoTrack?.dispose()
        localAudioTrack?.dispose()

        localRenderer?.dispose()
        factory?.dispose()

        localVideoView?.release()
        remoteVideoView?.release()
    }

    private fun getVideoCapturer(): VideoCapturerAndroid {

        for (name in arrayOf(FRONT_CAMERA, BACK_CAMERA)) {
            val camera = VideoCapturerAndroid.create(name)
            if (camera != null) return camera as VideoCapturerAndroid
        }
        throw RuntimeException("Failed to open capture")
    }

    companion object{
        private val FRONT_CAMERA = CameraEnumerationAndroid.getNameOfFrontFacingDevice()
        private val BACK_CAMERA = CameraEnumerationAndroid.getNameOfBackFacingDevice()

        private const val VIDEO_ID = "100"
        private const val AUDIO_ID = "101"
        private const val MEDIA_ID = "102"
    }
}