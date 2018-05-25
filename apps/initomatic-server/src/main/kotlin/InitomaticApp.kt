package io.undertree.initomatic

import io.undertree.initomatic.blueprints.EnableBlueprintComponent
import io.undertree.initomatic.pf4j.InitomaticPluginManager
import io.undertree.initomatic.plugins.EnablePluginComponent
import org.apache.commons.lang3.StringUtils
import org.pf4j.PluginManager
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
@EnableBlueprintComponent
@EnablePluginComponent
class InitomaticApp {

    companion object {
        private val logger = LoggerFactory.getLogger(InitomaticApp::class.java)
    }

    @Bean
    fun pluginManager(): PluginManager {
        return InitomaticPluginManager()
    }
}

fun main(args: Array<String>) {
    runApplication<InitomaticApp>(*args)
}