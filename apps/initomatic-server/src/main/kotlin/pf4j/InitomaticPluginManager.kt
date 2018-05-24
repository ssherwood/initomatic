package io.undertree.initomatic.pf4j

import org.pf4j.*
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

/**
 * Custom impl of the default plugin manager to make local development setup easier
 * with less need of extra IDE configuration.
 */
class InitomaticPluginManager : DefaultPluginManager() {
    init {
        loadPlugins()
    }

    @PostConstruct
    fun start() {
        startPlugins()
    }

    @PreDestroy
    fun stop() {
        stopPlugins()
    }

    // hardcoded development mode - this should become a application property
    override fun getRuntimeMode() = RuntimeMode.DEVELOPMENT

    // customized default development plugins dir
    // find plugins in the /plugins directory where the app is run
    override fun createPluginsRoot(): Path =
            Paths.get(System.getProperty("pf4j.pluginsDir", "plugins"))

    // TODO
    // needed to customize to set the classloader to the currentThread when using Spring Boot devtools
    override fun createPluginLoader(): PluginLoader {
        return CompoundPluginLoader()
                .add(object : DefaultPluginLoader(this, pluginClasspath) {
                    override fun createPluginClassLoader(pluginPath: Path, pluginDescriptor: PluginDescriptor): PluginClassLoader {
                        return PluginClassLoader(pluginManager, pluginDescriptor, Thread.currentThread().contextClassLoader)
                    }
                })
                .add(JarPluginLoader(this))
    }

    // this is working around the "bug" in the default development plugin classpath with Gradle
    override fun createPluginClasspath(): PluginClasspath {
        return if (isDevelopment)
            GradleDevelopmentPluginClasspath()
        else
            DefaultPluginClasspath()
    }

    override fun createPluginDescriptorFinder(): CompoundPluginDescriptorFinder = CompoundPluginDescriptorFinder().add(ManifestPluginDescriptorFinder())
}

// TODO
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