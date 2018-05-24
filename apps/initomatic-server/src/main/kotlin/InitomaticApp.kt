package io.undertree.initomatic

import io.undertree.initomatic.api.InitomaticPlugin
import io.undertree.initomatic.blueprints.EnableBlueprintComponent
import io.undertree.initomatic.pf4j.InitomaticPluginManager
import io.undertree.initomatic.plugins.EnablePluginComponent
import org.apache.commons.lang3.StringUtils
import org.pf4j.PluginManager
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
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

    fun printLogo() {
        logger.info(StringUtils.repeat("#", 40))
        logger.info(StringUtils.center("Init-o-matic DEMO", 40))
        logger.info(StringUtils.repeat("#", 40))
    }

    @Bean
    fun pluginManager(): PluginManager {
        return InitomaticPluginManager()
    }

    @Bean
    fun startup(pluginManager: PluginManager) = CommandLineRunner {
        printLogo()

        logger.info("Resolved pluginsRoot: ${System.getProperty("user.dir")}/${pluginManager.pluginsRoot}")

        // retrieves the extensions for InitomaticPlugin extension point
        val plugins = pluginManager.getExtensions(InitomaticPlugin::class.java)
        logger.info("Found ${plugins.size} extensions for plugin class '${InitomaticPlugin::class.java.name}'")

        // print extensions for each started plugin
        pluginManager.startedPlugins.forEach { plugin ->
            val pluginId = plugin.descriptor.pluginId
            logger.info("Extensions added by plugin '$pluginId'")

            val extensionClassNames = pluginManager.getExtensionClassNames(pluginId)
            for (extension in extensionClassNames) {
                logger.info("   $extension")
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<InitomaticApp>(*args)
}