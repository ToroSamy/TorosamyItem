package net.torosamy.torosamyItem.pojo

import net.torosamy.torosamyItem.scheduler.CooldownTask
import org.bukkit.entity.Player

class ItemCommand(var key: String, var command: String) {
    var cooldown: Int = 0
    var leftClick: Boolean = false
    var sneak: Boolean = false
    var permission: String = ""
    var isConsole: Boolean = true
    var playerCooldown = HashMap<Player, CooldownTask>()
}