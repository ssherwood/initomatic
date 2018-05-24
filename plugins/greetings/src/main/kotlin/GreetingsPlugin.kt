package io.undertree.initomatic.plugins

import io.undertree.initomatic.api.InitomaticPlugin
import org.apache.commons.lang3.StringUtils
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper

/**
 *
 */
class GreetingsPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {

    override fun start() {
        println("KotlinPlugin.start()")
        println(StringUtils.upperCase("KotlinPlugin using StringUtils"))
    }

    override fun stop() {
        println("KotlinPlugin.stop()")
    }
}

@Extension
class KotlinGreeting : InitomaticPlugin {
    override fun author() = "Ted"
    override fun version(): String = StringUtils.capitalize("0.0.1")
}
