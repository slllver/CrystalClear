package gg.slvr.crystals.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
abstract class MixinEndCrystal extends Entity {

	public MixinEndCrystal(EntityType<?> type, World world) {
		super(type, world);
	}

	@Inject(at = @At("HEAD"), method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", cancellable = true)
	private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
		if (source.isExplosive()) {
			if (!this.removed && !this.world.isClient) {
				this.remove();
				this.world.createExplosion(null, this.getX(), this.getY(), this.getZ(), 6.0F, Explosion.DestructionType.DESTROY);
				this.crystalDestroyed(source);
				cir.setReturnValue(true);
			}
		}
	}

	@Shadow
	private void crystalDestroyed(DamageSource source) {
		throw new IllegalStateException("Mixin failed to shadow crystalDestroyed(DamageSource source)");
	}

}
