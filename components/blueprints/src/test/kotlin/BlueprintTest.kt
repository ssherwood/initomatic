package io.undertree.initomatic.components.blueprints

import org.junit.Assert.assertTrue
import org.junit.Test

class BlueprintTest {

    @Test
    fun test() {
        val bp = Blueprint()
        assertTrue(bp.name == "Default")
    }

    @Test
    fun testVersionCompare() {
        assertTrue(Version.of("2.17.11") > Version.of("2.17.01"))
        assertTrue(Version.of("2.1.1") > Version.of("2.0.17"))
    }
}