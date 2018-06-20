/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.undertree.initomatic.components.plugins

import io.undertree.initomatic.api.InitomaticPlugin
import mu.KotlinLogging
import org.pf4j.PluginManager
import org.pf4j.update.UpdateManager
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.util.DigestUtils
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

/**
 * TODO - figure out a way to do this during development so the whole project
 * doesn't need to restart when actually modifying code in the plugins...
 *
 * pluginManager.unloadPlugin("greetings-plugin")
 * pluginManager.loadPlugins()
 * pluginManager.startPlugin("greetings-plugin")
 */
@RestController
@RequestMapping("plugins")
class PluginController(private val pluginManager: PluginManager,
                       private val updateManager: UpdateManager) {

    @GetMapping
    fun findAll(): List<PluginExtension> {
        return pluginManager.plugins.map { pluginWrapper ->
            return pluginManager.getExtensions(InitomaticPlugin::class.java, pluginWrapper.pluginId)
                    .map { pluginExt ->
                        PluginExtension(pluginWrapper.pluginId, pluginExt.stage(), pluginExt.version(),
                                pluginExt.license(), pluginExt.imageContentType())
                    }
        }.toList()
    }

    @GetMapping("/{pluginId}")
    fun findById(@PathVariable("pluginId") pluginId: String): List<PluginExtension> {
        return pluginManager.getExtensions(InitomaticPlugin::class.java, pluginId).map { pluginExt ->
            PluginExtension(pluginId,
                    pluginExt.stage(), pluginExt.version(),
                    pluginExt.license(), pluginExt.imageContentType())
        }
    }

    /**
     * Return the image associated with the plugin stage.
     * This will return an HTTP Response with an ETag header generated on the
     * image bytes and should 302 if the browser already cached the image.
     *
     * This is a "shallow" ETag implementation since we actually are still
     * going through the effort to look it up and recalculate it.
     */
    @GetMapping("{pluginId}/{stage}/image")
    fun pluginImage(@PathVariable("pluginId") pluginId: String,
                    @PathVariable("stage") stage: String): ResponseEntity<ByteArray> {

        logger.info { "inside getmapping" }

        // TODO provide "broken" default image instead of error?
        val ext = pluginManager.getExtensions(InitomaticPlugin::class.java, pluginId)
                .find { it.stage() == stage } ?: return ResponseEntity.notFound().build()

        // shallow ETag will at least save on network traffic
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(30, TimeUnit.DAYS))
                .eTag(DigestUtils.md5DigestAsHex(ext.imageBytes()))
                .contentType(MediaType.parseMediaType(ext.imageContentType()))
                .body(ext.imageBytes())
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