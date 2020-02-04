package com.electronslab.voip.rtc

import android.content.Context
import org.webrtc.MediaStream
import org.webrtc.PeerConnectionFactory

interface RtcRenderer {

    fun create(context:Context):PeerConnectionFactory

    fun setRemoteStream(stream: MediaStream)
    fun getLocalStream(): MediaStream
    fun clean()
}