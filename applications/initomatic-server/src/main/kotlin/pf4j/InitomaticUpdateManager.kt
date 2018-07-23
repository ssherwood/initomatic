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

import mu.KotlinLogging
import org.pf4j.update.UpdateManager
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.net.URL


private val logger = KotlinLogging.logger {}

@Component
class InitomaticUpdateManager(pluginManager: InitomaticPluginManager) : UpdateManager(pluginManager) {

    @Value("\${s3.url}")
    lateinit var url: String

    @Value("\${s3.accessKey}")
    lateinit var accessKey: String

    @Value("\${s3.secretKey}")
    lateinit var secretKey: String

    // TODO find a cleaner/clever way to initialize this
    override fun hasAvailablePlugins(): Boolean {
        if (repositories == null) {
            this.repositories = listOf(S3UpdateRepository("s3", URL(url), accessKey, secretKey))
        }

        return super.hasAvailablePlugins()
    }
}