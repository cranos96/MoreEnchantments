package cn.feng.enchant;

import cn.feng.enchant.enchantment.*;
import cn.feng.enchant.handler.AttackEntityHandler;
import cn.feng.enchant.util.ItemUtil;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreEnchantments implements ModInitializer {
	public static final EquipmentSlot[] ALL_ARMOR = new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
    public static final Logger LOGGER = LoggerFactory.getLogger("more_enchantments");

	// Extra Enchantments
	public static final Enchantment LIGHTNING_GOD = regEnchant("lightning_god", new LightningGodEnchantment());
	public static final Enchantment HOT_POTATO = regEnchant("hot_potato",  new HotPotatoEnchantment());
	public static final Enchantment SCHRODINGER_CURSE = regEnchant("schrodinger_curse", new SchrodingerCurseEnchantment());
	public static final Enchantment AIR_JUMP = regEnchant("air_jump", new AirJumpEnchantment());
	public static final Enchantment SCUD = regEnchant("scud", new ScudEnchantment());
	public static final Enchantment LIGHT_KUNGFU = regEnchant("light_kungfu", new LightKongFuEnchantment());
	public static final Enchantment UNSELECTABLE = regEnchant("unselectable", new UnselectableEnchantment());
	public static final Enchantment BABY = regEnchant("baby", new BabyEnchantment());
	public static final Enchantment LIFE_SHORTENING = regEnchant("life_shortening", new LifeShorteningEnchantment());

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
	private static Enchantment regEnchant(String name, Enchantment enchantment) {
		return Registry.register(Registries.ENCHANTMENT, new Identifier("more_enchantments", name), enchantment);
	}
}