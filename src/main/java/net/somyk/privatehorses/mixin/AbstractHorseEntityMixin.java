// This Source Code Form is subject to the terms of the Mozilla Public
// License, v. 2.0. If a copy of the MPL was not distributed with this
// file, You can obtain one at http://mozilla.org/MPL/2.0/.

package net.somyk.privatehorses.mixin;

import static net.somyk.privatehorses.PrivateHorses.DISABLE_DAMAGE_HORSES;
import static net.somyk.privatehorses.util.Utilities.*;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Objects;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.entity.UniquelyIdentifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity implements Tameable {

  @Unique private boolean publicHorse;

  @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
  private void checkInteract(
      PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
    AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
    boolean canInteract = canInteract(horse, player) || publicHorse;
    if (!canInteract) {
      cir.setReturnValue(ActionResult.FAIL);
      sendOwnershipNotification(horse, player);
      return;
    }

    ItemStack handStack = player.getMainHandStack();
    Boolean newPublicHorseValue = null;

    if (handStack.isOf(Items.SUGAR)) {
      newPublicHorseValue = true;
    } else if (handStack.isOf(Items.WHEAT)) {
      newPublicHorseValue = false;
    }

    if (newPublicHorseValue != null && player.isSneaking() && canInteract(horse, player)) {
      if (this.publicHorse != newPublicHorseValue) {
        this.publicHorse = newPublicHorseValue;
        handStack.decrementUnlessCreative(1, player);
        if (this.getEntityWorld() instanceof ServerWorld world) {
          showParticles(
              world, this, newPublicHorseValue ? ParticleTypes.WAX_OFF : ParticleTypes.WAX_ON);
        }
        horse
            .getEntityWorld()
            .playSound(
                null,
                this.getX(),
                this.getY(),
                this.getZ(),
                SoundEvents.ENTITY_HORSE_EAT,
                this.getSoundCategory(),
                1.0F,
                1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F);
        cir.setReturnValue(ActionResult.SUCCESS);
      } else {
        cir.setReturnValue(ActionResult.FAIL);
      }
    }
  }

  @Inject(method = "receiveFood", at = @At("HEAD"), cancellable = true)
  private void cancelFeeding(
      PlayerEntity player, ItemStack item, CallbackInfoReturnable<Boolean> cir) {
    boolean canInteract = canInteract((AbstractHorseEntity) (Object) this, player);
    if (!canInteract) cir.setReturnValue(false);
  }

  @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
  private void cancelDamage(
      ServerWorld world, DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
    AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;

    if (horse.getOwner() == null) {
      return;
    }

    Entity attacker = source.getAttacker();
    if (attacker instanceof PlayerEntity playerAttacker) {
      if (!canInteract(horse, playerAttacker)) {
        cir.setReturnValue(false);
        return;
      }
    }

    GameRules gameRules = Objects.requireNonNull(horse.getEntityWorld().getServer()).getGameRules();
    if (gameRules != null && gameRules.getBoolean(DISABLE_DAMAGE_HORSES)) {
      if (source.isOf(DamageTypes.GENERIC_KILL)) {
        return;
      }

      if (source.isOf(DamageTypes.FALL)) {
        return;
      }

      if (attacker != null && canInteract(horse, attacker)) {
        return;
      }

      cir.setReturnValue(false);
    }
  }

  @WrapOperation(
      method = "writeCustomData",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LazyEntityReference;writeData(Lnet/minecraft/entity/LazyEntityReference;Lnet/minecraft/storage/WriteView;Ljava/lang/String;)V"))
  private void writePublicHorse(
      LazyEntityReference<?> entityRef, WriteView view, String key, Operation<Void> original) {
    view.putBoolean("Public", this.publicHorse);
    original.call(entityRef, view, key);
  }

  @WrapOperation(
      method = "readCustomData",
      at =
          @At(
              value = "INVOKE",
              target =
                  "Lnet/minecraft/entity/LazyEntityReference;fromDataOrPlayerName(Lnet/minecraft/storage/ReadView;Ljava/lang/String;Lnet/minecraft/world/World;)Lnet/minecraft/entity/LazyEntityReference;"))
  private <StoredEntityType extends UniquelyIdentifiable>
      LazyEntityReference<StoredEntityType> readPublicHorse(
          ReadView view,
          String key,
          World world,
          Operation<LazyEntityReference<StoredEntityType>> original) {
    this.publicHorse = view.getBoolean("Public", false);
    return original.call(view, key, world);
  }

  protected AbstractHorseEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
    super(entityType, world);
  }
}
