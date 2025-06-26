package net.somyk.privatehorses.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.somyk.privatehorses.util.Utilities.*;

// This code was written in Transferable Pets mod with CC BY-NC 4.0 license by WinterWolfSV and adapted by myself for AbstractHorseEntity
// CC BY-NC 4.0 license: https://github.com/WinterWolfSV/Transferable_Pets/blob/master/LICENSE
@Mixin(value = PlayerEntity.class, priority = 999)
public class PlayerEntityMixin {

    @Inject(method = "interact", at = @At("TAIL"))
    private void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!(entity.isPlayer() && hand.equals(Hand.MAIN_HAND))) return;
        PlayerEntity targetPlayer = (PlayerEntity) entity;
        PlayerEntity player = (PlayerEntity) (Object) this;
        if (!(player.isSneaking() && player instanceof ServerPlayerEntity)) return;
        ServerWorld world = (ServerWorld) player.getWorld();

        for (Entity localEntities : world.getOtherEntities(player, Box.of(player.getPos(), 12, 12, 12), EntityPredicates.VALID_ENTITY)) {
            if (!(localEntities instanceof AnimalEntity animal)) continue;
            if (!(animal.isLeashed() && animal.getLeashHolder() == player)) continue;
            // Next 'if' statement was changed
            if (!(animal instanceof AbstractHorseEntity horse && horse.getOwner() != null && horse.getOwner().getUuid().equals(player.getUuid())) ) continue;

            horse.setOwner(targetPlayer); // changed from ((TameableEntity) animal).setOwner(targetPlayer);
            animal.detachLeashWithoutDrop();
            animal.attachLeash(targetPlayer, true);
            showParticles(world, animal, ParticleTypes.HEART);
            showParticles(world, targetPlayer, ParticleTypes.HEART);

            succeedTransferMessage(targetPlayer, player, animal); // replaced with my custom function
        }
    }

}
