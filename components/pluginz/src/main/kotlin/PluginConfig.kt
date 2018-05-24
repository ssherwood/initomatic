package io.undertree.initomatic.plugins

import io.undertree.initomatic.blueprints.PluginController
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [PluginController::class])
internal class PluginConfig