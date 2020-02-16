package com.electronslab

import org.jivesoftware.smack.provider.ExtensionElementProvider
import org.xmlpull.v1.XmlPullParser

class SignalingProvider : ExtensionElementProvider<Signaling>() {



    override fun parse(parser: XmlPullParser, initialDepth: Int): Signaling {

        val builder = Signaling.Builder()
        var tag:String? = null

        while (true) {
            when (parser.next()) {

                XmlPullParser.START_TAG -> tag = parser.name
                XmlPullParser.TEXT -> when (tag!!) {
                    Signaling.TAG_NAME_SDP->builder.setSdp(parser.text)
                    Signaling.TAG_NAME_SDP_MID->builder.setSdpMid(parser.text)
                    Signaling.TAG_NAME_SDP_M_LINE_INDEX->builder.setSdpMLineIndex(parser.text)
                    Signaling.TAG_NAME_SERVER_URL->builder.setServerUrl(parser.text)
                    Signaling.TAG_NAME_ADAPTER_TYPE->builder.setAdapterType(parser.text)
                    Signaling.TAG_NAME_TYPE->builder.setType(parser.text)
                }
                XmlPullParser.END_TAG-> when(parser.name){
                    Signaling.ELEMENT_NAME-> return builder.build()
                }
            }
        }

    }
}