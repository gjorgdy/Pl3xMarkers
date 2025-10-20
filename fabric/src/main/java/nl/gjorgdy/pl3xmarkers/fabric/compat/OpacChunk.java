package nl.gjorgdy.pl3xmarkers.fabric.compat;

import net.minecraft.util.math.ChunkPos;

public record OpacChunk(ChunkPos pos, String playerName, String name, int color) { }
