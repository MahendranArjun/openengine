package com.electronslab.voip.rtc

import org.webrtc.*

interface RtcPeerAdapter:PeerConnection.Observer, SdpObserver {

    override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {
    }

    override fun onAddStream(steam: MediaStream)

    override fun onIceCandidate(candidate: IceCandidate)

    override fun onDataChannel(channel: DataChannel) {
    }

    override fun onSignalingChange(state: PeerConnection.SignalingState) {
    }

    override fun onIceConnectionReceivingChange(change: Boolean) {
    }

    override fun onRemoveStream(stream: MediaStream) {
    }

    override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
    }

    override fun onRenegotiationNeeded() {
    }

    override fun onSetFailure(error: String) {

    }

    override fun onSetSuccess() {
    }

    override fun onCreateSuccess(sdp: SessionDescription)

    override fun onCreateFailure(error: String) {
    }

}