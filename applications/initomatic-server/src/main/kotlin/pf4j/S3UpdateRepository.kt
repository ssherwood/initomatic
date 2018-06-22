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

package io.undertree.initomatic.pf4j

import com.google.gson.GsonBuilder
import mu.KotlinLogging
import org.pf4j.update.FileDownloader
import org.pf4j.update.PluginInfo
import org.pf4j.update.UpdateRepository
import org.pf4j.update.util.LenientDateTypeAdapter
import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 * TODO still a lot of work to do here...
 *
 */
class S3UpdateRepository(private val id: String, private val url: URL, private val pluginsJsonFileName: String = "plugins.json") : UpdateRepository {

    // lazy initialize the map
    private val pluginsMap: MutableMap<String, PluginInfo> by lazy {
        initPlugins()
    }

    override fun getId() = id

    override fun getUrl() = url

    override fun getPlugins() = pluginsMap

    override fun getPlugin(id: String?): PluginInfo {
        return pluginsMap[id]!!  // <- TODO this is bad
    }

    override fun getFileDownloader(): FileDownloader {
        return S3FileDownloader()
    }

    override fun refresh() {
        initPlugins()
    }

    private fun initPlugins(): MutableMap<String, PluginInfo> {
        pluginsMap.clear()
        val gson = GsonBuilder().registerTypeAdapter(Date::class.java, LenientDateTypeAdapter()).create()
        val pluginsJsonReader: Reader = InputStreamReader(URL(getUrl(), pluginsJsonFileName).openStream())

        return gson.fromJson(pluginsJsonReader, Array<PluginInfo>::class.java)
                .associate { it.id to it }
                .toMutableMap()
    }
}