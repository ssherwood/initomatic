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

package io.undertree.initomatic.blueprints

import io.undertree.initomatic.api.InitomaticPlugin
import org.pf4j.PluginManager
import org.springframework.web.bind.annotation.*
import java.time.Instant

/**
 *
 */
@RestController
@RequestMapping("blueprints")
class BlueprintController(private val pluginManager: PluginManager) {

    @GetMapping
    fun findAll(): String {

        val plugins = pluginManager.getExtensions(InitomaticPlugin::class.java)

        // force plugin to reload
        // TODO - figure out a way to do this during development so the whole project
        // doesn't need to restart when actually modifying code in the plugins...
        //pluginManager.unloadPlugin("greetings-plugin")
        //pluginManager.loadPlugins()
        //pluginManager.startPlugin("greetings-plugin")

        plugins.forEach { plugin ->
            // val loader = pluginManager.getPluginClassLoader("greetings-plugin")
            println(">>> ${plugin.version()} - ${plugin.authors()}")
        }

        return "blueprintController ${Instant.now()}"
    }
}