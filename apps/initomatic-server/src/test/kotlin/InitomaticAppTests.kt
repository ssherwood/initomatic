package io.undertree.initomatic

import io.undertree.initomatic.blueprints.EnableBlueprintComponent
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [
    EnableBlueprintComponent::class
])
class InitomaticAppTests {
    @Test
    fun contextLoads() {
    }
}
