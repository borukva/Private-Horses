package net.somyk.privatehorses.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.somyk.privatehorses.util.Utilities.canInteract;

@Mixin(Leashable.class)
public interface LeashableMixin {

  @Inject(method = "canBeLeashedTo", at = @At("HEAD"), cancellable = true)
  private void canBeLeashedTo(Entity entity, CallbackInfoReturnable<Boolean> cir) {
    if (this instanceof AbstractHorseEntity horse && entity instanceof PlayerEntity player) {
      if (!canInteract(horse, player)) {
        cir.setReturnValue(false);
      }
    }
  }
}
