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

package io.undertree.initomatic.config

import io.undertree.initomatic.blueprints.EnableBlueprintComponent
import io.undertree.initomatic.plugins.EnablePluginComponent
import org.springframework.context.annotation.Configuration

/**
 * Primary configuration class for enabling application components.  This
 * essentially activates the underlying component's configuration class(es)
 * which will then make its individual beans available to the main application
 * at runtime.
 *
 * This approach simplifies component scanning by delegating it to the
 * component itself leaving the main application(s) unaware of the underlying
 * details.
 *
 * NOTE: This approach means that as new components are added to the project
 * their specific @Enable* should be added here to make them available.
 */
@Configuration
@EnableBlueprintComponent
@EnablePluginComponent
class ComponentConfig