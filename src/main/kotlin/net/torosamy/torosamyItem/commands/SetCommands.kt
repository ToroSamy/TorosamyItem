package net.torosamy.torosamyItem.commands

import net.torosamy.torosamyCore.item.meta.TrimData
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyCore.utils.NbtUtil
import net.torosamy.torosamyItem.api.TorosamyItemAPI
import net.torosamy.torosamyItem.utils.ConfigUtil
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.incendo.cloud.annotations.Argument
import org.incendo.cloud.annotations.Command
import net.torosamy.torosamyCore.config.ConfigUtil.MAIN_CONFIG
import net.torosamy.torosamyCore.item.data.ToolData
import org.incendo.cloud.annotations.CommandDescription
import org.incendo.cloud.annotations.Permission

class SetCommands {
    @Command("ti nbt string <player> <key> <value>")
    @Permission("torosamyitem.nbt")
    @CommandDescription("给手上的物品赋NBT属性")
    fun setNbtString(sender: CommandSender, @Argument("player") player: Player, @Argument("key") key: String, @Argument("value") value: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val itemInMainHand: ItemStack = player.inventory.itemInMainHand

        if (itemInMainHand.type == Material.AIR) {
            return
        }


        NbtUtil.setString(itemInMainHand, key, value)
    }

    @Command("ti nbt int <player> <key> <value>")
    @Permission("torosamyitem.nbt")
    @CommandDescription("给手上的物品赋NBT属性")
    fun setNbtInt(sender: CommandSender, @Argument("player") player: Player, @Argument("key") key: String, @Argument("value") value: Int) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.component(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val itemInMainHand: ItemStack = player.inventory.itemInMainHand

        if (itemInMainHand.type == Material.AIR) {
            return
        }

        NbtUtil.setInteger(itemInMainHand, key, value)
    }

    @Command(value = "ti set tool <player> <rules>")
    @Permission("torosamyitem.set.tool")
    @CommandDescription("给手上物品设置工具属性")
    fun setTool(sender: CommandSender, @Argument("player") player: Player, @Argument("rules") rulesInput: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val item = player.inventory.itemInMainHand
        if (item.type.isAir) {
            return
        }
        
        val rules = rulesInput.split(",").toList()
        
        val config = YamlConfiguration()

        config.set(MAIN_CONFIG.itemAttributeKeys.tool, rules)
        
        ToolData.getInstance().setItem(item, config)
    }

    @Command(value = "ti custom-model-data <player> <value>")
    @Permission("torosamyitem.custom-model-data")
    @CommandDescription("给手上物品设置CustomModelData")
    fun customModelData(sender: CommandSender, @Argument("player") player: Player, @Argument("value") value: Int) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val equipment = player.equipment

        if (equipment == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val item = equipment.itemInMainHand

        val meta = item.itemMeta

        if (meta == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        meta.setCustomModelData(value)

        item.setItemMeta(meta)
    }

    @Command(value = "ti display <player> <value>")
    @Permission("torosamyitem.material")
    @CommandDescription("给手上物品设置display")
    fun display(sender: CommandSender, @Argument("player") player: Player, @Argument("value") value: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val equipment = player.equipment

        if (equipment == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val meta = equipment.itemInMainHand.itemMeta

        if (meta == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        meta.displayName(MessageUtil.component(value))

        equipment.itemInMainHand.setItemMeta(meta)
    }


    @Command(value = "ti trim <player> <value>")
    @Permission("torosamyitem.trim")
    @CommandDescription("给手上物品设置trim")
    fun trim(sender: CommandSender, @Argument("player") player: Player, @Argument("value") value: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val item = player.inventory.itemInMainHand

        val meta = item.itemMeta

        if (meta == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }
        
        TrimData.getInstance().setMeta(player, meta, value)
        
        item.setItemMeta(meta)
    }

    @Command(value = "ti key <player> <value>")
    @Permission("torosamyitem.key")
    @CommandDescription("给手上物品设置Key")
    fun key(sender: CommandSender, @Argument("player") player: Player, @Argument("value") value: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val equipment = player.equipment

        if (equipment == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val item = equipment.itemInMainHand

        val customItem = TorosamyItemAPI.getCustomItem(value) ?: return

        NbtUtil.setString(item, TorosamyItemAPI.GET_TOROSAMY_ITEM_KEY(), customItem.getKey())
    }

    @Command(value = "ti material <player> <value>")
    @Permission("torosamyitem.material")
    @CommandDescription("给手上物品设置材质")
    fun material(sender: CommandSender, @Argument("player") player: Player, @Argument("value") value: String) {
        if (!player.isOnline) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val equipment = player.equipment

        if (equipment == null) {
            sender.sendMessage(MessageUtil.format(ConfigUtil.langConfig.playerNotFound))
            return
        }

        val item = equipment.itemInMainHand

        val material = Material.getMaterial(value.uppercase()) ?: return

        item.type = material
    }
}