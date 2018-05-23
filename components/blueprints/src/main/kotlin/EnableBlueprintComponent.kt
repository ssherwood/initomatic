package io.undertree.initomatic.blueprints

import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
@Import(BlueprintConfig::class)
annotation class EnableBlueprintComponent