package eu.hexasis.helixmarkers.mixin;

import eu.hexasis.helixmarkers.HelixMarkers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.intellij.lang.annotations.Language;
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
                @Language("HTML") var name = banner.getName().getLiteralString();
                if (name == null) return;
                HelixMarkers.api().addAreaPoint(
                    serverWorld.getRegistryKey().getValue(),
                    name,
                    banner.getColorForState().getEntityColor(),
                    pos
                );
            }
            if (state.isIn(BlockTags.RAILS)) {
                // loop to find next the lodestone rail
                BlockPos _pos = pos.north();
                for (int i = 1; i < 64; i++) {
                    if (!world.getBlockState(_pos).isIn(BlockTags.RAILS)) break;
                    if (world.getBlockState(_pos.down()).isOf(Blocks.LODESTONE)) {
                        System.out.println("found lodestone at " + _pos);
                        break;
                    }
                    _pos = _pos.north();
                }
            }
        }
    }

    @Inject(method = "onBreak", at = @At("HEAD"))
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        // beacon markers
        if (state.isOf(Blocks.BEACON)) {
            HelixMarkers.api().removeIconMarker(world.getRegistryKey().getValue(), "beacons", pos);
        }
        // area markers
        if (world instanceof ServerWorld serverWorld) {
            var blockEntity = serverWorld.getBlockEntity(
                state.isOf(Blocks.LODESTONE) ? pos.up() : pos
            );
            if (blockEntity instanceof BannerBlockEntity banner) {
                @Language("HTML") var name = banner.getName().getLiteralString();
                if (name == null) return;
                HelixMarkers.api().removeAreaPoint(
                        serverWorld.getRegistryKey().getValue(),
                        name,
                        banner.getColorForState().getEntityColor(),
                        pos
                );
            }
        }
    }

}
