package io.undertree.initomatic.blueprints

import io.undertree.initomatic.api.InitomaticPlugin
import org.pf4j.PluginManager
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

    @GetMapping
    fun findAll(): String {
        val plugins = pluginManager.getExtensions(InitomaticPlugin::class.java)

        plugins.forEach { plugin ->
            println(">>> ${plugin.version()} - ${plugin.author()}")
        }

        return "pluginsController ${Instant.now()}"
    }
}