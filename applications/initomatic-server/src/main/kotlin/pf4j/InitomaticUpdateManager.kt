package io.undertree.initomatic.pf4j

import org.pf4j.update.UpdateManager
import org.springframework.stereotype.Component

@Component
class InitomaticUpdateManager(pluginManager: InitomaticPluginManager) : UpdateManager(pluginManager)