package io.undertree.initomatic.blueprints

import io.undertree.initomatic.api.InitomaticPlugin
import org.pf4j.PluginManager
import org.springframework.web.bind.annotation.*

/**
 *
 */
@RestController
@RequestMapping("blueprints")
class BlueprintController(private val pluginManager: PluginManager) {

    @GetMapping
    fun findAll(): String {
        // invoke the plugin
        val plugins = pluginManager.getExtensions(InitomaticPlugin::class.java)

        // force plugin to reload
        // TODO - figure out a way to do this during development so the whole project
        // doesn't need to restart when actually modifying code in the plugins...
        //pluginManager.unloadPlugin("greetings-plugin")
        //pluginManager.loadPlugins()
        //pluginManager.startPlugin("greetings-plugin")

        plugins.forEach { plugin ->
            // val loader = pluginManager.getPluginClassLoader("greetings-plugin")
            println(">>> ${plugin.version()} - ${plugin.author()}")
        }

        return "Hello23456"
    }

}