package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.HelixMarkers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCollisionHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {

    @Inject(method = "onEntityCollision", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;tryUsePortal(Lnet/minecraft/block/Portal;Lnet/minecraft/util/math/BlockPos;)V"))
    public void onCollision(BlockState state, World world, BlockPos pos, Entity entity, EntityCollisionHandler handler, CallbackInfo ci) {
        // if there is no portal block north, move south
        if (isNotEndPortalBlock(world, pos.north())) {
            pos = pos.south();
        }
        // if there is no portal block south, move north
        if (isNotEndPortalBlock(world, pos.south())) {
            pos = pos.north();
        }
        // if there is no portal block east, move west
        if (isNotEndPortalBlock(world, pos.east())) {
            pos = pos.west();
        }
        // if there is no portal block west, move east
        if (isNotEndPortalBlock(world, pos.west())) {
            pos = pos.east();
        }
        // place marker if one does not exist yet
        if (world.getRegistryKey() == World.OVERWORLD) {
            Identifier identifier = world.getRegistryKey().getValue();
            HelixMarkers.api().addEndPortalIconMarker(identifier, pos);
        }
    }

    @Unique
    private static boolean isNotEndPortalBlock(World world, BlockPos pos) {
        return !world.getBlockState(pos).isOf(Blocks.END_PORTAL);
    }

}
