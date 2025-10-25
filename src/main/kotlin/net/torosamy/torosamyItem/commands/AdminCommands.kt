package net.torosamy.torosamyItem.commands

import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission


class AdminCommands {
    @Command("ti reload")
    @Permission("torosamyitem.reload")
    @CommandDescription("重载TorosamyItem配置文件")
    fun reloadConfig(sender: CommandSender) {
        ConfigUtil.reloadConfig()
        TorosamyItemAPI.loadItems()
        TorosamyItemAPI.loadCatalogs()
        sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.reloadMessage))
    }


    @Command("ti give <itemName> <player>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予玩家自定义物品")
    fun givePlayerItem(sender: CommandSender, @Argument("player") player: Player, @Argument("itemName") itemName: String) {
        giveMoreItem(sender, player, itemName, 1)
    }

    @Command("ti give <itemName> <player> <amount>")
    @Permission("torosamyitem.give")
    @CommandDescription("给予指定数量的自定义物品")
    fun giveMoreItem(sender: CommandSender, @Argument("player") player: Player, @Argument("itemName") itemName: String, @Argument("amount") amount: Int) {
        if (player.inventory.firstEmpty() == -1) {
            sender.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.packageOverflow))

            if (player.name.equals(sender.name)) {
                player.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.packageOverflow))
            }
        }
        val customItem = TorosamyItemAPI.getCustomItem(itemName)

        if (customItem == null) {
            sender.sendMessage(MessageUtil.format(player, ConfigUtil.langConfig.customNotFound))
            return
        }
        val item = customItem.itemStack.clone()
        item.amount = amount
        player.inventory.addItem(item)
        
        val message: String = MessageUtil.format(player, ConfigUtil.langConfig.giveSuccessful.replace("%item%", itemName))
        
        sender.sendMessage(message)
    }


    @Command("ti give <itemName>", requiredSender = Player::class)
    @Permission("torosamyitem.give")
    @CommandDescription("给予自己自定义物品")
    fun giveSelfItem(sender: CommandSender, @Argument("itemName") itemName: String) {
        giveMoreItem(sender, sender as Player, itemName, 1)
    }

    @Command("ti nbt <itemName>")
    @Permission("torosamyitem.nbt")
    @CommandDescription("显示CustomItem的NBT")
    fun showItemNBT(sender: CommandSender, @Argument("itemName") itemName: String) {
        val customItem = TorosamyItemAPI.getCustomItem(itemName) ?: return

        val item = customItem.itemStack

        sender.sendMessage("HashCode: " + TorosamyItemAPI.getHashCode(item))
        sender.sendMessage("TorosamyItem: "+ TorosamyItemAPI.getItemKey(item))
    }

    @Command("ti nbt", requiredSender = Player::class)
    @Permission("torosamyitem.nbt")
    @CommandDescription("显示手上物品的NBT")
    fun showHandNBT(sender: CommandSender) {
        val player = sender as Player
        
        val itemInMainHand: ItemStack = player.inventory.itemInMainHand
        
        if (itemInMainHand.type == Material.AIR) {
            return
        }

        sender.sendMessage("HashCode: " + TorosamyItemAPI.getHashCode(itemInMainHand))
        sender.sendMessage("TorosamyItem: "+ TorosamyItemAPI.getItemKey(itemInMainHand))

    }

}