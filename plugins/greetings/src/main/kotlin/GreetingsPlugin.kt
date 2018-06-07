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

package io.undertree.initomatic.plugins

import io.undertree.initomatic.api.InitomaticPlugin
import org.apache.commons.lang3.StringUtils
import org.pf4j.Extension
import org.pf4j.Plugin
import org.pf4j.PluginWrapper
import org.slf4j.LoggerFactory

//private val logger = KotlinLogging.logger {}
// ^ couldn't get this to work in deployment mode (classloader issues)

/**
 *
 */
class GreetingsPlugin(wrapper: PluginWrapper) : Plugin(wrapper) {

    companion object {
        private val logger = LoggerFactory.getLogger(GreetingsPlugin::class.java.name)
    }

    override fun start() {
        logger.info("KotlinPlugin.start()")
        // just to demonstrate that we can use a external library
        logger.info(StringUtils.upperCase("KotlinPlugin using StringUtils"))
    }

    override fun stop() {
        logger.info("KotlinPlugin.stop()")
    }
}

@Extension
class KotlinGreeting : InitomaticPlugin {
    override fun authors() = listOf("Ted")
    override fun version(): String = StringUtils.capitalize("0.0.1")
    override fun license() = "Apache 2.0"
    override fun stage() = "Greet"
}
