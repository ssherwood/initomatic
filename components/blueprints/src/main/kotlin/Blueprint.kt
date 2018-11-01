/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.undertree.initomatic.components.blueprints

import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

/**
 *
 */
@Document
data class Blueprint(
        @Id val id: UUID = UUID.randomUUID(),
        val name: String = "Default",
        val description: String = "$name description",
        @JsonSerialize(using = ToStringSerializer::class)
        val version: Version = Version.of("0.0.1")
        //,
        //val tags: List<String> = listOf()
)

// TODO this should be moved into a shared lib
class Version(val major: Int = 0, val minor: Int = 0, val patch: Int = 1) : Comparable<Version> {

    override fun compareTo(other: Version): Int {
        return when {
            this.major > other.major -> 1
            this.major < other.major -> -1
            this.minor > other.minor -> 1
            this.minor < other.minor -> -1
            this.patch > other.patch -> 1
            this.patch < other.patch -> -1
            else -> 0
        }
    }

    override fun toString(): String {
        return "$major.$minor.$patch"
    }

    companion object {
        fun of(semver: String): Version {
            val (major, minor, patch) = semver.split(".").map { it.toInt() }
            return Version(major, minor, patch)
        }
    }
}