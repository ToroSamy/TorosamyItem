package net.torosamy.torosamyItem.listener

import net.torosamy.torosamyItem.api.TorosamyItemAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class ResourcePackListener : Listener {
    @EventHandler
    public fun playerOnJoin(event: PlayerJoinEvent) {
        val player = event.player

        TorosamyItemAPI.updatePlayerResourcePack(player)
    }
}