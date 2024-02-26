package cn.feng.enchant;

import cn.feng.enchant.enchantment.HotPotatoEnchantment;
import cn.feng.enchant.enchantment.LightningGodEnchantment;
import cn.feng.enchant.handler.AttackEntityHandler;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MoreEnchantments implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("more_enchantments");

	// Extra Enchantments
	public static final Enchantment LIGHTNING_GOD = register("lightning_god", new LightningGodEnchantment());
	public static final Enchantment HOT_POTATO = register("hot_potato",  new HotPotatoEnchantment());

	@Override
	public void onInitialize() {
		LOGGER.info("Hello Fabric world!");

		// Register handlers
		AttackEntityCallback.EVENT.register(new AttackEntityHandler());
	}

	/**
		Register extra enchantments.
	 */
	private static Enchantment register(String name, Enchantment enchantment) {
		return Registry.register(Registries.ENCHANTMENT, new Identifier("more_enchantments", name), enchantment);
	}
}