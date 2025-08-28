package nl.gjorgdy.pl3xmarkers.mixin;

import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
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
        var blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof BannerBlockEntity banner && world.getBlockState(pos.down()).isOf(Blocks.LODESTONE)) {
            @Language("HTML") var name = banner.getName().getLiteralString();
            if (name == null) return;
            Pl3xMarkers.api().addAreaPoint(
                world.getRegistryKey().getValue(),
                name,
                banner.getColorForState().getEntityColor(),
                pos
            );
        }
    }

    @Inject(method = "onBreak", at = @At("HEAD"))
    public void onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfoReturnable<BlockState> cir) {
        // beacon markers
        if (state.isOf(Blocks.BEACON)) {
            Pl3xMarkers.api().removeBeaconIconMarker(world.getRegistryKey().getValue(), pos);
        }
        // area markers
        if (world instanceof ServerWorld serverWorld) {
            var blockEntity = serverWorld.getBlockEntity(
                state.isOf(Blocks.LODESTONE) ? pos.up() : pos
            );
            if (blockEntity instanceof BannerBlockEntity banner) {
                @Language("HTML") var name = banner.getName().getLiteralString();
                if (name == null) return;
                Pl3xMarkers.api().removeAreaPoint(
                        serverWorld.getRegistryKey().getValue(),
                        name,
                        banner.getColorForState().getEntityColor(),
                        pos
                );
            }
        }
    }

}
