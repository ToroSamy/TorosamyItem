package net.torosamy.torosamyItem.utils

import io.papermc.paper.command.brigadier.argument.ArgumentTypes.itemStack
import me.clip.placeholderapi.PlaceholderAPI
import net.torosamy.torosamyCore.nbtapi.NBT
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.TorosamyItem
import net.torosamy.torosamyItem.pojo.CustomItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.EquipmentSlotGroup
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta
import java.util.*


class ItemUtil {
    companion object {
        fun getItem(customItem: CustomItem,player: Player): ItemStack {
            val material: Material = Material.getMaterial(customItem.material ?: "APPLE")!!
            val item = ItemStack(material)

            val itemMeta = item.itemMeta
            if (customItem.displayName != null) itemMeta.setDisplayName(MessageUtil.text(PlaceholderAPI.setPlaceholders(player,customItem.displayName!!)))

            if (customItem.lore != null) { itemMeta.lore = customItem.lore!!.map { MessageUtil.text(PlaceholderAPI.setPlaceholders(player,it)) } }

            if (customItem.unbreakable != null) { itemMeta.isUnbreakable = customItem.unbreakable!! }

            if (customItem.enchantList != null) {
                for (enchantmentStr in customItem.enchantList!!) {
                    val enchantSplit = enchantmentStr.split(":")
                    val enchantmentType = Enchantment.getByName(enchantSplit[0])
                    val enchantmentLevel = enchantSplit[1].toInt()
                    //附魔种类 附魔等级 忽略附魔等级限制
                    if(enchantmentType != null && enchantmentLevel != 0) {itemMeta.addEnchant(enchantmentType, enchantmentLevel, true) }
                }
            }

            if (customItem.itemFlagList != null) {
                for (itemFlagStr in customItem.itemFlagList!!) {
                    for (itemFlag in ItemFlag.entries) {
                        if (itemFlagStr == itemFlag.name) {
                            itemMeta.addItemFlags(itemFlag)
                            break
                        }
                    }
                }
            }

            if (customItem.clearAttribute != null && customItem.clearAttribute!!) {
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES)
                val attribute = Attribute.GENERIC_ATTACK_DAMAGE


                itemMeta.addAttributeModifier(attribute, AttributeModifier(
                    NamespacedKey(TorosamyItem.plugin, TorosamyItem.plugin.name),
                    0.0,
                    AttributeModifier.Operation.entries[0],
                    EquipmentSlotGroup.ANY
                ))
            }

            if(customItem.color != null && itemMeta is LeatherArmorMeta) {
                (itemMeta as LeatherArmorMeta).setColor(Color.fromRGB(customItem.color!!.toInt(16)))
            }

            item.setItemMeta(itemMeta)

            NBT.modify(item) { nbt ->
                nbt.setInteger("HashCode", customItem.hashCode)
                nbt.setString("TorosamyItem",customItem.key)
            }

            return item
        }

    }
}