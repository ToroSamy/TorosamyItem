package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityResurrectEvent

class PlayAnimationListener : Listener {
    @EventHandler
    fun deathProtect(event: EntityResurrectEvent) {
        if (event.entity !is Player) {
            return
        }

        val hand = event.hand ?: return

        val player = event.entity as Player

        val equipment = player.equipment ?: return

        val item = equipment.getItem(hand)

        if (!TorosamyItemAPI.isTorosamyItem(item)) {
            return
        }

        event.isCancelled = true
    }
    

}