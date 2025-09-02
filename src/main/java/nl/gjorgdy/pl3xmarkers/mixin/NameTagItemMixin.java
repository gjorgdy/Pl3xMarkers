package nl.gjorgdy.pl3xmarkers.mixin;

import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.NameTagItem;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import nl.gjorgdy.pl3xmarkers.Pl3xMarkers;
import nl.gjorgdy.pl3xmarkers.helpers.PortalHelper;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(NameTagItem.class)
public abstract class NameTagItemMixin extends Item {

    public NameTagItemMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (context.getWorld().getBlockState(context.getBlockPos()).isOf(Blocks.NETHER_PORTAL)) {
            Text customName = context.getStack().getCustomName();
            if (customName != null) {
                var portalCenter = PortalHelper.getNetherPortalCenter(context.getWorld(), context.getBlockPos());
                Pl3xMarkers.api().renameNetherPortalIconMarker(context.getWorld().getRegistryKey().getValue(), portalCenter, customName.getLiteralString());
                context.getStack().decrementUnlessCreative(1, context.getPlayer());
            }
            System.out.println("renaming portal to " + (customName == null ? "" : customName.getLiteralString()));
            return ActionResult.CONSUME;
        }
        return ActionResult.PASS;
    }
}
