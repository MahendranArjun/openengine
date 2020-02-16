package com.electronslab

import org.jivesoftware.smack.provider.ExtensionElementProvider
import org.xmlpull.v1.XmlPullParser

class MessageContentProvider : ExtensionElementProvider<MessageContent>() {
    override fun parse(parser: XmlPullParser, initialDepth: Int): MessageContent {
        val builder = MessageContent.Builder()
        var tag: String? = null
        while (true) {

            when (parser.next()) {

                XmlPullParser.START_TAG -> tag = parser.name
                XmlPullParser.TEXT -> {
                    when (tag!!) {
                        MessageContent.TAG_NAME_MESSAGE_ID -> builder.setMessageId(parser.text)
                        MessageContent.TAG_NAME_SEND_TIMESTAMP -> builder.setSendTimestamp(parser.text)
                        MessageContent.TAG_NAME_URI -> builder.setUri(parser.text)
                        MessageContent.TAG_NAME_TYPE -> builder.setMediaType(parser.text)
                        MessageContent.TAG_NAME_TEXT -> builder.setText(parser.text)
                        MessageContent.TAG_NAME_CONTACT -> builder.setContact(parser.text)
                        MessageContent.TAG_NAME_LATITUDE -> builder.setLatitude(parser.text)
                        MessageContent.TAG_NAME_LONGITUDE -> builder.setLongitude(parser.text)
                    }
                }

                XmlPullParser.END_TAG -> when (val name = parser.name) {
                    MessageContent.TAG_NAME_TEXT,
                    MessageContent.TAG_NAME_VIDEO,
                    MessageContent.TAG_NAME_VOICE,
                    MessageContent.TAG_NAME_IMAGE,
                    MessageContent.TAG_NAME_AUDIO,
                    MessageContent.TAG_NAME_FILE,
                    MessageContent.TAG_NAME_CONTACT,
                    MessageContent.TAG_NAME_LOCATION
                    -> builder.setType(name)

                    MessageContent.ELEMENT_NAME -> return builder.build()
                }
            }

        }

    }
}