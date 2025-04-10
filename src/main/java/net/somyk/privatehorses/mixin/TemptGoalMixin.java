package net.somyk.privatehorses.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import static net.somyk.privatehorses.util.Utilities.canInteract;

@Mixin(TemptGoal.class)
public class TemptGoalMixin {

    @WrapOperation(method = "canStart", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getClosestPlayer(Lnet/minecraft/entity/ai/TargetPredicate;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/entity/player/PlayerEntity;"))
    private PlayerEntity cancelTempt(World instance, TargetPredicate targetPredicate, LivingEntity livingEntity, Operation<PlayerEntity> original){
        PlayerEntity playerEntity = original.call(instance, targetPredicate, livingEntity);
        if(livingEntity instanceof AbstractHorseEntity horse && playerEntity != null){
            if(!canInteract(horse, playerEntity)) return null;
        }
        return playerEntity;
    }
}
