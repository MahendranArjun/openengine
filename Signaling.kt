package com.electronslab

import org.jivesoftware.smack.packet.ExtensionElement
import org.jivesoftware.smack.util.XmlStringBuilder

data class Signaling(
    val sdp:String,
    val sdpMid:String?,
    val sdpMLineIndex:Int?,
    val serverUrl:String?,
    val adapterType:String?,
    val type:Type
): ExtensionElement {


    override fun toXML(enclosingNamespace: String?): CharSequence {
        return XmlStringBuilder(this).rightAngleBracket()
            .element(TAG_NAME_SDP, sdp)
            .element(TAG_NAME_TYPE, type)
            .optElement(TAG_NAME_SDP_MID, sdpMid)
            .optElement(TAG_NAME_SDP_M_LINE_INDEX, sdpMLineIndex?.toString())
            .optElement(TAG_NAME_SERVER_URL, serverUrl)
            .optElement(TAG_NAME_ADAPTER_TYPE, adapterType)
            .closeElement(this)
    }

    override fun getNamespace() = ELEMENT_NAME

    override fun getElementName() = NAMESPACE


    enum class Type{
        Answer, Offer, Candidate
    }

    companion object {
        const val ELEMENT_NAME = "signaling"
        const val NAMESPACE = "jabber:client"
        const val TAG_NAME_SDP = "sdp"
        const val TAG_NAME_SDP_MID = "sdpMid"
        const val TAG_NAME_SDP_M_LINE_INDEX = "sdpMLineIndex"
        const val TAG_NAME_SERVER_URL = "serverUrl"
        const val TAG_NAME_ADAPTER_TYPE = "adapterType"
        const val TAG_NAME_TYPE = "type"
    }

    class Builder {
        private var sdp: String? = null
        private var sdpMid: String? = null
        private var sdpMLineIndex: Int? = null
        private var serverUrl: String? = null
        private var adapterType: String? = null
        private var type: Type? = null


        fun setSdp(sdp:String):Builder{
            this.sdp = sdp
            return this
        }

        fun setSdpMid(sdpMid:String):Builder{
            this.sdpMid = sdpMid
            return this
        }

        fun setSdpMLineIndex(sdpMLineIndex:String):Builder{
            this.sdpMLineIndex = sdpMLineIndex.toInt()
            return this
        }

        fun setServerUrl(serverUrl:String):Builder{
            this.serverUrl = serverUrl
            return this
        }

        fun setAdapterType(adapterType:String):Builder{
            this.adapterType = adapterType
            return this
        }

        fun setType(type:String):Builder{
            this.type = Type.valueOf(type)
            return this
        }


        fun build():Signaling = Signaling(sdp!!, sdpMid, sdpMLineIndex, serverUrl, adapterType, type!!)
    }
}