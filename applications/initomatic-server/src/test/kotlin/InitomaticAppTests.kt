package io.undertree.initomatic

import io.undertree.initomatic.components.blueprints.EnableBlueprintComponent
import io.undertree.initomatic.components.plugins.EnablePluginComponent
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [
    EnableBlueprintComponent::class,
    EnablePluginComponent::class
])
class InitomaticAppTests {
    @Test
    fun contextLoads() {
    }
}
