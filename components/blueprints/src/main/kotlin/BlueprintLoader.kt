package io.undertree.initomatic.components.blueprints

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import javax.annotation.PostConstruct

@Component
@ConditionalOnProperty("loaders.blueprints.enabled", havingValue = "true")
class BlueprintLoader(private val blueprintRepository: BlueprintRepository) {

    @PostConstruct
    fun loadData() {
        blueprintRepository.deleteAll()
        blueprintRepository.saveAll(listOf("Spring Boot Microservice", "NodeJS Express App", "Go Command Line").map { Blueprint(name = it) })
    }
}
