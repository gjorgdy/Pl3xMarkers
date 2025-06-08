package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.HelixMarkers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public class BlockMixin {

    @Inject(method = "onPlaced", at = @At("HEAD"))
    public void onPlace(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {
        // area markers
        if (world instanceof ServerWorld serverWorld && world.getBlockState(pos.down()).isOf(Blocks.LODESTONE)) {
            var blockEntity = serverWorld.getBlockEntity(pos);
            if (blockEntity instanceof BannerBlockEntity banner) {
                HelixMarkers.api().addAreaPoint(
                    serverWorld.getRegistryKey().getValue(),
                    banner.getName().getLiteralString(),
                    banner.getColorForState().getEntityColor(),
                    pos
                );
            }
        }
    }

    @Inject(method = "onBreak", at = @At("HEAD"))
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        // beacon markers
        if (state.isOf(Blocks.BEACON)) {
            HelixMarkers.api().removeSimpleMarker(world.getRegistryKey().getValue(), "beacons", pos);
        }
        // area markers
        if (world instanceof ServerWorld serverWorld) {
            var blockEntity = serverWorld.getBlockEntity(
                state.isOf(Blocks.LODESTONE) ? pos.up() : pos
            );
            if (blockEntity instanceof BannerBlockEntity banner) {
                HelixMarkers.api().removeAreaPoint(
                        serverWorld.getRegistryKey().getValue(),
                        banner.getName().getLiteralString(),
                        banner.getColorForState().getEntityColor(),
                        pos
                );
            }
        }
    }

}
