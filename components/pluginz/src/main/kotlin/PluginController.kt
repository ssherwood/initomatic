package io.undertree.initomatic.blueprints

import io.undertree.initomatic.api.InitomaticPlugin
import org.pf4j.PluginManager
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.Instant

/**
 *
 */
@RestController
@RequestMapping("plugins")
class PluginController(private val pluginManager: PluginManager) {

    companion object {
        private val logger = LoggerFactory.getLogger(PluginController::class.java)
    }

    @GetMapping
    fun findAll(): String {
        val plugins = pluginManager.getExtensions(InitomaticPlugin::class.java)

        plugins.forEach { plugin ->
            println(">>> ${plugin.version()} - ${plugin.author()}")
        }

        return "pluginsController ${Instant.now()}"
    }

    @GetMapping("/summary")
    fun summary() {
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