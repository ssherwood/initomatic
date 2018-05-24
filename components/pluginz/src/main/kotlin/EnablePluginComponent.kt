package io.undertree.initomatic.plugins

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(PluginConfig::class)
annotation class EnablePluginComponent