package com.electronslab.voip.rtc

import org.webrtc.IceCandidate
import org.webrtc.MediaStream
import org.webrtc.SessionDescription

interface RtcPeerConnection {

    fun createOffer()
    fun acceptOffer(sdp:SessionDescription)

    fun createAnswer()
    fun acceptAnswer(sdp:SessionDescription)

    fun addCandidate(candidate: IceCandidate)
    fun addStream(stream:MediaStream)

    fun close()

}