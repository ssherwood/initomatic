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

import mu.KotlinLogging
import org.springframework.data.domain.Pageable
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

private val logger = KotlinLogging.logger {}

/**
 *
 */
@RestController
@RequestMapping("blueprints")
class BlueprintController(private val blueprintRepository: BlueprintRepository) {

    @GetMapping
    fun findAll(pageable: Pageable) = blueprintRepository.findAll(pageable)

    @GetMapping("{id}")
    fun findById(@PathVariable("id") id: UUID): Blueprint {
        // TODO have this throw a proper http exception
        return blueprintRepository.findById(id)
                .orElseThrow { IllegalArgumentException() }
    }
}