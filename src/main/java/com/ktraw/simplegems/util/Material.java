package com.ktraw.simplegems.util;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;

@RequiredArgsConstructor
@NoArgsConstructor(force = true)
public enum Material {
    STONE(MapColor.STONE, NoteBlockInstrument.BASEDRUM),
    METAL(MapColor.METAL, null),
    BUILDABLE_GLASS;

    private final MapColor mapColor;
    private final NoteBlockInstrument noteBlockInstrument;

    public BlockBehaviour.Properties properties() {
        return BlockBehaviour.Properties.of()
                .mapColor(mapColor)
                .instrument(noteBlockInstrument);
    }
}
