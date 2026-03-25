// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package net.somyk.privatehorses;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.serialization.Codec;
import eu.pb4.polymer.resourcepack.api.PolymerResourcePackUtils;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.world.rule.GameRule;
import net.minecraft.world.rule.GameRuleCategory;
import net.minecraft.world.rule.GameRuleType;
import net.minecraft.world.rule.GameRuleVisitor;
import net.somyk.privatehorses.util.ModConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PrivateHorses implements ModInitializer {

  public static final String MOD_ID = "private-horses";
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
  public static final boolean polymer_loaded = FabricLoader.getInstance()
      .isModLoaded("polymer-resource-pack");

  public static final GameRule<Boolean> DISABLE_DAMAGE_HORSES =
      Registry.register(Registries.GAME_RULE, MOD_ID + ":disable_damage",
          new GameRule<>(GameRuleCategory.MOBS, GameRuleType.BOOL, BoolArgumentType.bool(),
              GameRuleVisitor::visitBoolean, Codec.BOOL, (value) -> value ? 1 : 0, false,
              FeatureSet.empty()));


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