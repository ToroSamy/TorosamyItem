package net.torosamy.torosamyItem.utils


import com.google.common.collect.ImmutableMultimap
import me.clip.placeholderapi.PlaceholderAPI
import de.tr7zw.changeme.nbtapi.NBT
import net.torosamy.torosamyCore.utils.MessageUtil
import net.torosamy.torosamyItem.pojo.CustomItem
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.LeatherArmorMeta


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
            //TODO 隐藏属性
            if (customItem.itemFlagList != null) {
                for (itemFlagStr in customItem.itemFlagList!!) {
                    val itemFlag = ItemFlag.valueOf(itemFlagStr)
                    itemMeta.addItemFlags(itemFlag)
                    if(itemFlag == ItemFlag.HIDE_ATTRIBUTES) itemMeta.attributeModifiers = ImmutableMultimap.of()
                }
            }


            if (customItem.clearAttribute != null && customItem.clearAttribute!!) {
                itemMeta.attributeModifiers = ImmutableMultimap.of()
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