package net.somyk.privatehorses.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.BlockAttachedEntity;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.somyk.privatehorses.util.Utilities;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockAttachedEntity.class)
@Debug(export = true)
public class BlockAttachedEntityMixin {

  @WrapOperation(
      method = "damage",
      at =
          @At(
              value = "INVOKE",
              target = "Lnet/minecraft/entity/decoration/BlockAttachedEntity;isRemoved()Z"))
  private boolean onDamage(
      BlockAttachedEntity instance,
      Operation<Boolean> original,
      ServerWorld world,
      DamageSource source,
      float amount) {
    boolean keepLeash = false;
    if (source.getAttacker() instanceof ServerPlayerEntity serverPlayerEntity) {
      for (Leashable leashable : Leashable.collectLeashablesHeldBy(instance)) {
        if (leashable instanceof AbstractHorseEntity horse) {
          if (Utilities.canInteract(horse, serverPlayerEntity)) leashable.detachLeash();
          else keepLeash = true;
        }
      }
    }
    return original.call(instance) || keepLeash;
  }
}
