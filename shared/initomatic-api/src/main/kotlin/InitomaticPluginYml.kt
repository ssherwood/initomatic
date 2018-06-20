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

package io.undertree.initomatic.api

import org.yaml.snakeyaml.Yaml
import java.io.Reader

/**
 *
 */
abstract class InitomaticPluginYml(pluginConfigReader: Reader) : InitomaticPlugin {

    private val pluginDefinition = Yaml().loadAs(pluginConfigReader, DefaultPluginDefinition::class.java)

    override fun version() = pluginDefinition.version
    override fun authors() = pluginDefinition.authors
    override fun license() = pluginDefinition.license
    override fun stage() = pluginDefinition.stage
    override fun imageContentType() = pluginDefinition.imageSvg
    override fun imageBytes() = pluginDefinition.imageBytes
}

// public and mutable for snakeyml...
// TODO we could probably just use a snakey map?
class DefaultPluginDefinition(
        var version: String = "0.0.0",
        var authors: List<String> = listOf("Anonymous"),
        var license: String = "Unknown",
        var stage: String = "Default",
        var imageSvg: String = "image/svg+xml",
        var imageBytes: ByteArray = InitomaticPluginYml::class.java.classLoader.getResourceAsStream("plugin.svg").readBytes()
)

/**
 * Specific trick to be able to load for each plugins classpath
 */
inline fun <reified T : Any> classpathReader(filename: String): Reader {
    val classLoader = T::class.java.classLoader
    val resourceStream = classLoader.getResourceAsStream(filename)
            ?: throw IllegalStateException("Failed to locate file '$filename' in classpath of $classLoader")
    return resourceStream.bufferedReader()
}