package io.undertree.initomatic.plugins

import io.undertree.initomatic.api.InitomaticPlugin
import org.apache.commons.lang3.StringUtils
import org.pf4j.Extension

@Extension
class OtherGreeting : InitomaticPlugin {
    override fun author() = "Shawn"
    override fun version(): String = StringUtils.capitalize("0.1.1")
}