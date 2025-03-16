package net.somyk.privatehorses.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Tameable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.somyk.privatehorses.PrivateHorses.DISABLE_DAMAGE_HORSES;
import static net.somyk.privatehorses.util.Utilities.canInteract;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity implements Tameable {

    @Inject(method = "interactMob", at = @At("HEAD"), cancellable = true)
    private void checkInteract(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir){
        boolean canInteract = canInteract((AbstractHorseEntity) (Object) this, player);
        if(!canInteract) cir.setReturnValue(ActionResult.FAIL);
    }

    @Inject(method = "receiveFood", at = @At("HEAD"), cancellable = true)
    private void cancelFeeding(PlayerEntity player, ItemStack item, CallbackInfoReturnable<Boolean> cir){
        boolean canInteract = canInteract((AbstractHorseEntity) (Object) this, player);
        if(!canInteract) cir.setReturnValue(false);
    }

    @Inject(method = "beforeLeashTick", at = @At("HEAD"))
    private void detachLeash(Entity leashHolder, float distance, CallbackInfoReturnable<Boolean> cir) {
        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;
        if(!canInteract(horse, leashHolder)) {
            horse.detachLeash(true, false);
            if(leashHolder instanceof PlayerEntity player) player.giveItemStack(Items.LEAD.getDefaultStack());
        }
    }

    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void cancelDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;

        if (horse.getOwnerUuid() == null) {
            return;
        }

        Entity attacker = source.getAttacker();
        if (attacker instanceof PlayerEntity playerAttacker) {
            if (!canInteract(horse, playerAttacker)) {
                cir.setReturnValue(false);
                return;
            }
        }

        if (horse.getWorld().getGameRules().getBoolean(DISABLE_DAMAGE_HORSES)) {
            if (source.isOf(DamageTypes.FALL)) {
                return;
            }

            cir.setReturnValue(false);
        }
    }

    protected AbstractHorseEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }
}