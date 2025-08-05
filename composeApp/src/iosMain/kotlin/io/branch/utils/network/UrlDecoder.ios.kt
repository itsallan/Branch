package io.branch.utils.network

import platform.Foundation.NSString
import platform.Foundation.create
import platform.Foundation.stringByRemovingPercentEncoding

actual object UrlDecoder {
    actual fun decode(value: String, charset: String): String {
        // iOS-specific URL decoding
        return (NSString.create(string = value) as NSString)
            .stringByRemovingPercentEncoding() ?: value
    }
}