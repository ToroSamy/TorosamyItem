package net.torosamy.torosamyItem.commands

import net.torosamy.torosamyCore.api.TorosamyCoreAPI
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission

class LookCommands {
    @Command("ti look nbt string <key> <player>")
    @Permission("torosamyitem.look")
    @CommandDescription("查看手上的物品的NBT")
    fun lookNbtString(sender: CommandSender, @Argument("player") player: Player, @Argument("key") key: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val itemInMainHand: ItemStack = player.inventory.itemInMainHand

        if (itemInMainHand.type == Material.AIR) {
            return
        }

        val value = NbtUtil.getString(itemInMainHand, key) ?: return

        sender.sendMessage(value)
    }

    @Command("ti look nbt int <key> <player>")
    @Permission("torosamyitem.look")
    @CommandDescription("查看手上的物品的NBT")
    fun lookNbtInt(sender: CommandSender, @Argument("player") player: Player, @Argument("key") key: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val itemInMainHand: ItemStack = player.inventory.itemInMainHand

        if (itemInMainHand.type == Material.AIR) {
            return
        }

        val value = NbtUtil.getInteger(itemInMainHand, key)

        sender.sendMessage(value.toString())
    }

    @Command("ti look base <player>")
    @Permission("torosamyitem.look")
    @CommandDescription("查看手上的物品的基础信息")
    fun lookCustomModelData(sender: CommandSender, @Argument("player") player: Player) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val itemInMainHand: ItemStack = player.inventory.itemInMainHand
        
        val config = YamlConfiguration()
        
        config.set("Info", TorosamyCoreAPI.getConfig(itemInMainHand))
        
        sender.sendMessage(config.saveToString())
    }
}