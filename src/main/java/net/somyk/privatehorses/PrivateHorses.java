// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package net.somyk.privatehorses;

import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.GameRules;
import net.somyk.privatehorses.util.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateHorses implements ModInitializer {

  public static final String MOD_ID = "private-horses";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
  public static final boolean polymer_loaded =
      FabricLoader.getInstance().isModLoaded("polymer-resource-pack");

  public static final GameRules.Key<GameRules.BooleanRule> DISABLE_DAMAGE_HORSES =
      GameRuleRegistry.register(
          MOD_ID + ":disable_damage",
          GameRules.Category.MOBS,
          GameRuleFactory.createBooleanRule(false));

  @Override
  public void onInitialize() {

    ModConfig.registerConfigs();
    if (polymer_loaded) {
      if (PolymerResourcePackUtils.addModAssets(MOD_ID)) {
        LOGGER.info("[{}]: Successfully added mod assets.", MOD_ID);
      } else {
        LOGGER.error("[{}]: Failed to add mod assets.", MOD_ID);
      }
    }
  }
}
