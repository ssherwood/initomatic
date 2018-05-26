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

import org.pf4j.*
import org.springframework.stereotype.Component
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Custom impl of the default plugin manager to make local development setup easier
 * with less need of extra IDE configuration.
 *
 * I would have liked to make the configuration Spring managed but the pf4j framework
 * initializes itself earlier than the configuration bean is available (so NPEs).  This
 * is my current compromise.
 */
@Component
class InitomaticPluginManager : DefaultPluginManager() {

    init {
        super.loadPlugins()
    }

    @PostConstruct
    override fun startPlugins() = super.startPlugins()

    @PreDestroy
    override fun stopPlugins() = super.stopPlugins()

    // prefer default to be in development mode
    override fun getRuntimeMode(): RuntimeMode =
            RuntimeMode.byName(System.getProperty("pf4j.mode", RuntimeMode.DEVELOPMENT.toString()))

    // customized default development plugins dir
    // find plugins in the /plugins directory where the app itself is run
    override fun createPluginsRoot(): Path =
            Paths.get(System.getProperty("pf4j.pluginsDir", "plugins"))

    // TODO
    // needed to customize to set the classloader to currentThread when using Spring Boot devtools
    override fun createPluginLoader(): PluginLoader =
            CompoundPluginLoader()
                    .add(object : DefaultPluginLoader(this, pluginClasspath) {
                        override fun createPluginClassLoader(pluginPath: Path, pluginDescriptor: PluginDescriptor): PluginClassLoader =
                                PluginClassLoader(pluginManager, pluginDescriptor, Thread.currentThread().contextClassLoader)
                    })
                    .add(JarPluginLoader(this))

    // don't bother with the default properties descriptor
    override fun createPluginDescriptorFinder(): CompoundPluginDescriptorFinder =
            CompoundPluginDescriptorFinder()
                    .add(ManifestPluginDescriptorFinder())

    // this is working around the "bug" in the default development plugin classpath with Gradle
    override fun createPluginClasspath(): PluginClasspath {
        return if (isDevelopment)
            GradleDevelopmentPluginClasspath()
        else
            DefaultPluginClasspath()
    }
}

// TODO remove with pf4j 2.3.0 is available
// working around maven specific paths in the default Development classloader
class GradleDevelopmentPluginClasspath : DevelopmentPluginClasspath() {
    init {
        addClassesDirectories(
                "build/classes/java/main",
                "build/classes/kotlin/main",
                "build/resources/main",
                "build/tmp/kapt3/classes/main")

        // not actually sure what this should be???
        addLibDirectories("target/lib")
    }
}