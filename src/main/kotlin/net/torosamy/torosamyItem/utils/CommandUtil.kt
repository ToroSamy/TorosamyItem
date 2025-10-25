package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyCore.commands.CommandManager
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.commands.AdminCommands
import net.torosamy.torosamyItem.commands.PlayerCommands

class CommandUtil {
    companion object {
        private val commanderManager: CommandManager = CommandManager(TorosamyItem.plugin)
        
        public val ADMIN_COMMANDS: AdminCommands = AdminCommands()
        public val PLAYER_COMMANDS: PlayerCommands = PlayerCommands()
        
        fun registerCommand() {
            commanderManager.annotationParser.parse(ADMIN_COMMANDS)
            commanderManager.annotationParser.parse(PLAYER_COMMANDS)
        }
    }
}