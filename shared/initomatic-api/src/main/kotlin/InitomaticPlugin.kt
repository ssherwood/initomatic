package io.undertree.initomatic.api

import org.pf4j.ExtensionPoint

interface InitomaticPlugin : ExtensionPoint {
    fun version(): String
    fun author(): String
}