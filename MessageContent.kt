package com.electronslab

import org.jivesoftware.smack.packet.ExtensionElement
import org.jivesoftware.smack.util.XmlStringBuilder

sealed class MessageContent: ExtensionElement {

    abstract val messageId: String
    abstract val sendTimestamp: Long


    override fun getElementName() = ELEMENT_NAME

    override fun getNamespace() = NAMESPACE


    override fun toXML(enclosingNamespace: String?): CharSequence {
        return XmlStringBuilder(this)
            .rightAngleBracket()
            .element(TAG_NAME_MESSAGE_ID, messageId)
            .element(TAG_NAME_SEND_TIMESTAMP, "$sendTimestamp")
            .contentElement()
            .closeElement(this)
    }


    protected abstract fun XmlStringBuilder.contentElement():XmlStringBuilder


    data class Text(
        override val messageId: String,
        val text: String,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder = element(TAG_NAME_TEXT, text)

    }


    data class Image(
        override val messageId: String,
        val uri: String,
        val type:String,
        val text: String?,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder =
            openElement(TAG_NAME_IMAGE)
                .rightAngleBracket()
                .element(TAG_NAME_URI, uri)
                .element(TAG_NAME_TYPE, type)
                .optElement(TAG_NAME_TEXT, text)
                .closeElement(TAG_NAME_IMAGE)

    }

    data class Audio(
        override val messageId: String,
        val uri: String,
        val type:String,
        val text: String?,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder =
            openElement(TAG_NAME_AUDIO)
                .rightAngleBracket()
                .element(TAG_NAME_URI, uri)
                .element(TAG_NAME_TYPE, type)
                .optElement(TAG_NAME_TEXT, text)
                .closeElement(TAG_NAME_IMAGE)

    }


    data class Video(
        override val messageId: String,
        val uri: String,
        val type:String,
        val text: String?,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder =
            openElement(TAG_NAME_VIDEO)
                .rightAngleBracket()
                .element(TAG_NAME_URI, uri)
                .element(TAG_NAME_TYPE, type)
                .optElement(TAG_NAME_TEXT, text)
                .closeElement(TAG_NAME_IMAGE)

    }

    data class Voice(
        override val messageId: String,
        val uri: String,
        val type:String,
        val text: String?,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder =
            openElement(TAG_NAME_VOICE)
                .rightAngleBracket()
                .element(TAG_NAME_URI, uri)
                .element(TAG_NAME_TYPE, type)
                .optElement(TAG_NAME_TEXT, text)
                .closeElement(TAG_NAME_IMAGE)

    }


    data class File(
        override val messageId: String,
        val uri: String,
        val type:String,
        val text: String?,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder =
            openElement(TAG_NAME_FILE)
                .rightAngleBracket()
                .element(TAG_NAME_URI, uri)
                .element(TAG_NAME_TYPE, type)
                .optElement(TAG_NAME_TEXT, text)
                .closeElement(TAG_NAME_IMAGE)

    }

    data class Contact(
        override val messageId: String,
        val body:String,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder =
            element(TAG_NAME_CONTACT, body)

    }

    data class Location(
        override val messageId: String,
        val latitude:String,
        val longitude:String,
        override val sendTimestamp: Long

    ) : MessageContent() {

        override fun XmlStringBuilder.contentElement(): XmlStringBuilder =
                openElement(TAG_NAME_LOCATION)
                    .rightAngleBracket()
                    .element(TAG_NAME_LATITUDE, latitude)
                    .element(TAG_NAME_LONGITUDE, longitude)
                    .closeElement(TAG_NAME_LOCATION)

    }




    companion object {
        const val ELEMENT_NAME = "content"
        const val NAMESPACE = "jabber:client"
        const val TAG_NAME_SEND_TIMESTAMP = "sendTimestamp"
        const val TAG_NAME_MESSAGE_ID = "messageId"
        const val TAG_NAME_URI = "uri"
        const val TAG_NAME_TYPE = "type"

        const val TAG_NAME_TEXT = "text"
        const val TAG_NAME_VIDEO = "video"
        const val TAG_NAME_VOICE = "voice"
        const val TAG_NAME_IMAGE = "image"
        const val TAG_NAME_AUDIO = "audio"
        const val TAG_NAME_FILE = "file"
        const val TAG_NAME_CONTACT = "contact"
        const val TAG_NAME_LOCATION = "location"

        const val TAG_NAME_LATITUDE = "latitude"
        const val TAG_NAME_LONGITUDE = "longitude"

    }

    class Builder {


        private var messageId: String? = null
        private var sendTimestamp: Long? = null
        private var text: String? = null
        private var uri: String? = null
        private var mediaType: String? = null
        private var latitude: String? = null
        private var longitude: String? = null
        private var contact: String? = null

        private var type: String? = null


        fun setMessageId(messageId: String): Builder {
            this.messageId = messageId
            return this
        }


        fun setSendTimestamp(sendTimestamp: String): Builder {
            this.sendTimestamp = sendTimestamp.toLong()
            return this
        }


        fun setText(text: String): Builder {
            this.text = text
            return this
        }


        fun setUri(uri: String): Builder {
            this.uri = uri
            return this
        }


        fun setMediaType(mediaType: String): Builder {
            this.mediaType = mediaType
            return this
        }

        fun setLatitude(latitude: String): Builder {
            this.latitude = latitude
            return this
        }

        fun setLongitude(longitude: String): Builder {
            this.longitude = longitude
            return this
        }

        fun setContact(contact: String): Builder {
            this.contact = contact
            return this
        }

        fun setType(type: String): Builder {
            this.type = type
            return this
        }


        fun build(): MessageContent = when (type) {
            MessageContent.TAG_NAME_TEXT -> MessageContent.Text(messageId!!, text!!, sendTimestamp!!)
            MessageContent.TAG_NAME_IMAGE -> MessageContent.Image(
                messageId!!,
                uri!!,
                mediaType!!,
                text,
                sendTimestamp!!
            )
            MessageContent.TAG_NAME_VIDEO -> MessageContent.Video(
                messageId!!,
                uri!!,
                mediaType!!,
                text,
                sendTimestamp!!
            )
            MessageContent.TAG_NAME_VOICE -> MessageContent.Voice(
                messageId!!,
                uri!!,
                mediaType!!,
                text,
                sendTimestamp!!
            )
            MessageContent.TAG_NAME_AUDIO -> MessageContent.Audio(
                messageId!!,
                uri!!,
                mediaType!!,
                text,
                sendTimestamp!!
            )
            MessageContent.TAG_NAME_FILE -> MessageContent.File(
                messageId!!,
                uri!!,
                mediaType!!,
                text,
                sendTimestamp!!
            )
            MessageContent.TAG_NAME_CONTACT -> MessageContent.Contact(
                messageId!!,
                contact!!,
                sendTimestamp!!
            )
            MessageContent.TAG_NAME_LOCATION -> MessageContent.Location(
                messageId!!,
                latitude!!,
                longitude!!,
                sendTimestamp!!
            )
            else -> throw IllegalArgumentException("Illegal content type")
        }


    }




}