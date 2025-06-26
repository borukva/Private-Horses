package net.somyk.privatehorses.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.Leashable;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.somyk.privatehorses.util.Utilities.canInteract;

@Mixin(Leashable.class)
public interface LeashableMixin {

    @Inject(method = "attachLeash(Lnet/minecraft/entity/Entity;Lnet/minecraft/entity/Entity;Z)V", at = @At("HEAD"), cancellable = true)
    private static <E extends Entity & Leashable> void cancelAttachment(E entity, Entity leashHolder, boolean sendPacket, CallbackInfo ci){
        if(entity instanceof AbstractHorseEntity horse) {
            if (!canInteract(horse, leashHolder)) {
                // horse.detachLeashWithoutDrop();
                if (leashHolder instanceof PlayerEntity player) player.giveItemStack(Items.LEAD.getDefaultStack());
                ci.cancel();
            }
        }
    }
}
