package cn.feng.enchant;

import cn.feng.enchant.enchantment.*;
import cn.feng.enchant.handler.AttackEntityHandler;
import cn.feng.enchant.util.ItemUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreEnchantments implements ModInitializer {
	public static final EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final Logger LOGGER = LoggerFactory.getLogger("more_enchantments");

	// Extra Enchantments
	public static final Enchantment LIGHTNING_GOD = register("lightning_god", new LightningGodEnchantment());
	public static final Enchantment HOT_POTATO = register("hot_potato",  new HotPotatoEnchantment());
	public static final Enchantment SCHRODINGER_CURSE = register("schrodinger_curse", new SchrodingerCurseEnchantment());
	public static final Enchantment AIR_JUMP = register("air_jump", new AirJumpEnchantment());
	public static final Enchantment SCUD = register("scud", new ScudEnchantment());
	public static final Enchantment LIGHT_KUNGFU = register("light_kungfu", new LightKongFuEnchantment());

	@Override
	public void onInitialize() {
		// Register handlers
		AttackEntityCallback.EVENT.register(new AttackEntityHandler());

		// Init utils
		ItemUtil.init();
	}

	/**
		Register extra enchantments.
	 */
	private static Enchantment register(String name, Enchantment enchantment) {
		return Registry.register(Registries.ENCHANTMENT, new Identifier("more_enchantments", name), enchantment);
	}
}