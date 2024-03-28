package com.ktraw.simplegems.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;

public class DirectionUtil {
    public static Direction getFacingFromEntity(BlockPos clickedBlock, LivingEntity entity, boolean horizontalOnly) {
        if (horizontalOnly) {
            return entity.getDirection().getOpposite();
        }
        else {
            return Direction.getNearest(
                    (float) (entity.getX() - clickedBlock.getX()),
                    (float) (entity.getY() - clickedBlock.getY()),
                    (float) (entity.getZ() - clickedBlock.getZ())
            );
        }
    }
}
