// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package net.somyk.privatehorses.util;

import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.LeashKnotEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerConfigEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.NameToIdCache;
import net.somyk.privatehorses.PrivateHorses;

import java.util.Optional;
import java.util.Random;

import static net.somyk.privatehorses.PrivateHorses.polymer_loaded;
import static net.somyk.privatehorses.util.ModConfig.getStringValue;

public class Utilities {

    public static boolean canInteract(AbstractHorseEntity horse, Entity entity) {
        if(horse.getOwnerReference() == null) {
            return true;
        }
        else {
            return horse.getOwnerReference().getUuid().equals(entity.getUuid())
                    || entity instanceof LeashKnotEntity
                    || Permissions.check(entity, PrivateHorses.MOD_ID + ".interact", PermissionLevel.ADMINS); // Взаємодія дозволена
        }
    }

    public static void sendOwnershipNotification(AbstractHorseEntity horse, Entity entity) {
        // Перевірки на null і тип гравця, як в оригінальному else блоці
        MinecraftServer server = horse.getEntityWorld().getServer();
        if (entity instanceof ServerPlayerEntity player && horse.getOwnerReference() != null && server != null && server.getApiServices().nameToIdCache() instanceof NameToIdCache cache) {
            Optional<PlayerConfigEntry> horseOwner = cache.getByUuid(horse.getOwnerReference().getUuid());
            String horseOwnerName;

            String horseName = "§b" + horse.getName().getString() + "§f";
            Text message;

            horseOwnerName = horseOwner.map(playerConfigEntry -> "§e" + playerConfigEntry.name()).orElse("§7§kunknown");

            if (polymer_loaded && !ModConfig.getBooleanValue("ignore_polymer"))
                message = Text.translatable("message.private-horses.owned_by", horseName, horseOwnerName);
            else message = Text.literal(getStringValue("message.owned_by").formatted(horseName, horseOwnerName));

            player.sendMessage(message, true);
        }
    }

    public static void succeedTransferMessage(PlayerEntity newOwner, PlayerEntity oldOwner, AnimalEntity animal) {
        String oldOwnerName = "§b"+oldOwner.getName().getString()+"§f";
        String newOwnerName = "§b"+newOwner.getName().getString()+"§f";
        String animalName = "§e"+animal.getName().getString()+"§f";

        Text oldOwnerMessage;
        Text newOwnerMessage;


        if(polymer_loaded && !ModConfig.getBooleanValue("ignore_polymer")) {
            oldOwnerMessage = Text.translatable("message.private-horses.new_owner", newOwnerName, animalName);
            newOwnerMessage = Text.translatable("message.private-horses.transfer", oldOwnerName, animalName);
        } else {
            oldOwnerMessage = Text.literal(getStringValue("message.new_owner").formatted(newOwnerName, animalName));
            newOwnerMessage = Text.literal(getStringValue("message.transfer").formatted(oldOwnerName, animalName));
        }

        oldOwner.sendMessage(oldOwnerMessage, true);
        newOwner.sendMessage(newOwnerMessage, true);
    }

    public static void showParticles(ServerWorld world, Entity entity, ParticleEffect particleEffect) {
        // This code was written in Transferable Pets mod with CC BY-NC 4.0 license by WinterWolfSV
        // CC BY-NC 4.0 license: https://github.com/WinterWolfSV/Transferable_Pets/blob/master/LICENSE
        Random random = new Random();

        for (int i = 0; i < 7; i++) {
            double d = random.nextGaussian() * 0.02;
            double e = random.nextGaussian() * 0.02;
            double f = random.nextGaussian() * 0.02;
            world.spawnParticles(particleEffect, entity.getParticleX(1.0), entity.getRandomBodyY() + 0.5, entity.getParticleZ(1.0), 1, d, e, f, 1);
        }

    }
}
