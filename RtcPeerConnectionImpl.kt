package com.electronslab.voip.rtc

import org.webrtc.*

internal class RtcPeerConnectionImpl(factory: PeerConnectionFactory, mediaConstraints: MediaConstraints, private val adapter: RtcPeerAdapter):RtcPeerConnection, RtcPeerAdapter {

    private val peerConnection = factory.createPeerConnection(
        createIceServer(), mediaConstraints, this
    )

    override fun createOffer() {
        peerConnection.createOffer(this, MediaConstraints())
    }

    override fun acceptOffer(sdp: SessionDescription) {
        peerConnection.setRemoteDescription(this, sdp)
    }

    override fun createAnswer() {
        peerConnection.createAnswer(this, MediaConstraints())
    }

    override fun acceptAnswer(sdp: SessionDescription) {
        peerConnection.setRemoteDescription(this, sdp)
    }

    override fun addCandidate(candidate: IceCandidate) {
        peerConnection.addIceCandidate(candidate)
    }

    override fun addStream(stream: MediaStream) {
        peerConnection.addStream(stream)

    }

    override fun close() {
        peerConnection.close()
    }

    private fun createIceServer() = listOf(
        PeerConnection.IceServer("stun.l.google.com:19302")
    )


    override fun onIceGatheringChange(state: PeerConnection.IceGatheringState) {
        adapter.onIceGatheringChange(state)
    }

    override fun onAddStream(steam: MediaStream) {
        adapter.onAddStream(steam)
    }

    override fun onIceCandidate(candidate: IceCandidate) {
        adapter.onIceCandidate(candidate)

    }

    override fun onDataChannel(channel: DataChannel) {
        adapter.onDataChannel(channel)
    }

    override fun onSignalingChange(state: PeerConnection.SignalingState) {
        adapter.onSignalingChange(state)
    }

    override fun onIceConnectionReceivingChange(change: Boolean) {
        adapter.onIceConnectionReceivingChange(change)
    }

    override fun onRemoveStream(stream: MediaStream) {
        adapter.onRemoveStream(stream)
    }

    override fun onIceConnectionChange(state: PeerConnection.IceConnectionState) {
        adapter.onIceConnectionChange(state)
    }

    override fun onRenegotiationNeeded() {
        adapter.onRenegotiationNeeded()
    }

    override fun onSetFailure(error: String) {
        adapter.onSetFailure(error)
    }

    override fun onSetSuccess() {
        adapter.onSetSuccess()
    }

    override fun onCreateSuccess(sdp: SessionDescription) {
        peerConnection.setLocalDescription(this, sdp)
        adapter.onCreateSuccess(sdp)
    }

    override fun onCreateFailure(error: String) {
        adapter.onCreateFailure(error)
    }


}