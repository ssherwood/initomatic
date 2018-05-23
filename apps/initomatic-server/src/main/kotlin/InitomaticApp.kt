package io.undertree.initomatic

import io.undertree.initomatic.api.InitomaticPlugin
import io.undertree.initomatic.blueprints.EnableBlueprintComponent
import org.apache.commons.lang3.StringUtils
import org.pf4j.*
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import java.nio.file.Path
import java.nio.file.Paths
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy


@SpringBootApplication
@EnableBlueprintComponent
class InitomaticApp {

    companion object {
        private val logger = LoggerFactory.getLogger(InitomaticApp::class.java)
    }

    fun printLogo() {
        logger.info(StringUtils.repeat("#", 40))
        logger.info(StringUtils.center("Init-o-matic DEMO", 40))
        logger.info(StringUtils.repeat("#", 40))
    }

    @Bean
    fun pluginManager(): PluginManager {
        return InitomaticPluginManager()
    }

    @Bean
    fun startup(pluginManager: PluginManager) = CommandLineRunner {
        printLogo()
        logger.info("Resolved pluginsRoot: ${System.getProperty("user.dir")}/${pluginManager.pluginsRoot}")

        // retrieves the extensions for InitomaticPlugin extension point
        val plugins = pluginManager.getExtensions(InitomaticPlugin::class.java)
        logger.info("Found ${plugins.size} extensions for extension point '${InitomaticPlugin::class.java.name}'")

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

fun main(args: Array<String>) {
    runApplication<InitomaticApp>(*args)
}

/**
 * Custom impl of the default plugin manager to make local development setup easier
 * with less need of extra IDE configuration.
 */
class InitomaticPluginManager : DefaultPluginManager() {
    @PostConstruct
    fun init() {
        loadPlugins()
        // enable a disabled plugin
        // initomaticPluginManager.enablePlugin("greetings-plugin")
        startPlugins()
    }

    @PreDestroy
    fun destroy() {
        stopPlugins()
    }

    // hardcoded development mode - this should become a application property
    override fun getRuntimeMode() = RuntimeMode.DEVELOPMENT

    // customized default development plugins dir
    // find plugins in the /plugins directory where the app is run
    override fun createPluginsRoot(): Path =
            Paths.get(System.getProperty("pf4j.pluginsDir", "plugins"))


    //
    override fun createPluginLoader(): PluginLoader {
        return CompoundPluginLoader()
                .add(object : DefaultPluginLoader(this, pluginClasspath) {
                    override fun createPluginClassLoader(pluginPath: Path, pluginDescriptor: PluginDescriptor): PluginClassLoader {
                        return PluginClassLoader(pluginManager, pluginDescriptor, Thread.currentThread().contextClassLoader)
                                //javaClass.classLoader)
                    }
                })
                .add(JarPluginLoader(this))
        //return super.createPluginLoader()
    }

    // this is working around the "bug" in the default development plugin classpath
    override fun createPluginClasspath(): PluginClasspath {
        return if (isDevelopment)
            GradleDevelopmentPluginClasspath()
        else
            DefaultPluginClasspath()
    }

    override fun createPluginDescriptorFinder(): CompoundPluginDescriptorFinder {
        return CompoundPluginDescriptorFinder()
                // Demo is using the Manifest file
                // PropertiesPluginDescriptorFinder is commented out just to avoid error log
                //.add(PropertiesPluginDescriptorFinder())
                .add(ManifestPluginDescriptorFinder())
    }
}

// TODO - working around maven specific paths in the default Development classloader
class GradleDevelopmentPluginClasspath : DevelopmentPluginClasspath() {
    init {
        addClassesDirectories("build/classes/kotlin/main",
                "build/resources/main",
                "build/tmp/kapt3/classes/main")
        addLibDirectories("target/lib")
    }
}