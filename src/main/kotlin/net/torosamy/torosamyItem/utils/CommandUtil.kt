package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyCore.TorosamyCore
import net.torosamy.torosamyItem.commands.AdminCommands

class CommandUtil {
    companion object {
        fun registerCommand() {
            TorosamyCore.commanderManager.annotationParser.parse(AdminCommands())
        }
    }
}