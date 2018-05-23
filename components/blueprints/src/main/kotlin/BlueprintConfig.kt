package io.undertree.initomatic.blueprints

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackageClasses = [BlueprintController::class])
internal class BlueprintConfig