package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyCore.TorosamyCore
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.commands.AdminCommands

class CommandUtil {
    companion object {
        private var torosamyCorePlugin: TorosamyCore = TorosamyItem.plugin.server.pluginManager.getPlugin("TorosamyCore") as TorosamyCore
        fun registerCommand() {
            torosamyCorePlugin.getCommandManager().annotationParser.parse(AdminCommands())
        }
    }
}