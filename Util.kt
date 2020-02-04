package com.electronslab.voip.rtc

import org.webrtc.MediaConstraints






fun createVideoConstraints():MediaConstraints{
    val constraints = MediaConstraints()
    constraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveAudio", "true"))
    constraints.mandatory.add(MediaConstraints.KeyValuePair("offerToReceiveVideo", "true"))
    return constraints
}