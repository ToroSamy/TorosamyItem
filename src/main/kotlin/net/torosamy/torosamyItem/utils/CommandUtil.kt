package net.torosamy.torosamyItem.utils

import net.torosamy.torosamyCore.commands.CommandManager
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.commands.*

object CommandUtil {
    private val commanderManager: CommandManager = CommandManager(TorosamyItem.plugin)

    public val ADMIN_COMMANDS = AdminCommands()
    public val LOOK_COMMANDS = LookCommands()
    public val SET_COMMANDS = SetCommands()
    public val PLAYER_COMMANDS = PlayerCommands()
    public val REMOVE_COMMANDS = RemoveCommands()

    fun registerCommand() {
        commanderManager.annotationParser.parse(ADMIN_COMMANDS)
        commanderManager.annotationParser.parse(PLAYER_COMMANDS)
        commanderManager.annotationParser.parse(LOOK_COMMANDS)
        commanderManager.annotationParser.parse(SET_COMMANDS)
        commanderManager.annotationParser.parse(REMOVE_COMMANDS)
    }
}