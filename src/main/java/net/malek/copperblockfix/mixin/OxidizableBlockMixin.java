package net.malek.copperblockfix.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Oxidizable;
import net.minecraft.block.OxidizableBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Property;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(OxidizableBlock.class)
public class OxidizableBlockMixin {


	@Inject(at = @At("HEAD"), method = "randomTick", cancellable = true)
	private void randomTickMixin(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo info) {
		for(Direction direction : Direction.values()) {
			if(world.getBlockState(pos.offset(direction)).isAir()) {
				if (random.nextFloat() < 0.05688889F) {
					if(Oxidizable.getIncreasedOxidationBlock(state.getBlock()).isPresent()) {
						BlockState state1 = Oxidizable.getIncreasedOxidationBlock(state.getBlock()).get().getDefaultState();
						for(Property property : state.getProperties())
						{
							state1 = state1.with(property, state.get(property));
						}
						world.setBlockState(pos, state1);
					}
				}
				return;
			}
		}
		info.cancel();
	}
}
