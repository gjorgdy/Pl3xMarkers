package nl.gjorgdy.pl3xmarkers.core.helpers;

import net.pl3x.map.core.world.World;
import nl.gjorgdy.pl3xmarkers.core.Pl3xMarkersCore;

public class WorldHelpers {

	public static boolean isOverworld(World world) {
		return isOverworld(world.getKey());
	}

    public static boolean isOverworld(String worldKey) {
        return Pl3xMarkersCore.isBukkit() ? isOverworldBukkit(worldKey) : isOverworldFabric(worldKey);
    }

    private static boolean isOverworldBukkit(String worldKey) {
        return !worldKey.contains("_nether") && !worldKey.contains("_the_end");
    }

    private static boolean isOverworldFabric(String worldKey) {
        return !worldKey.contains("the_nether") && !worldKey.contains("the_end");
    }

	public static boolean isNether(World world) {
		return isNether(world.getKey());
	}

    public static boolean isNether(String worldKey) {
        return (Pl3xMarkersCore.isBukkit() && worldKey.contains("_nether")) || worldKey.contains("the_nether");
    }

	public static boolean isEnd(World world) {
		return isEnd(world.getKey());
	}

    public static boolean isEnd(String worldKey) {
        return (Pl3xMarkersCore.isBukkit() && worldKey.contains("_the_end")) || worldKey.contains("the_end");
    }

}
