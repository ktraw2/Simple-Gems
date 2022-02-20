package com.ktraw.simplegems.util.containers;

import com.ktraw.simplegems.blocks.generator.GeneratorContainerMenu;
import com.ktraw.simplegems.blocks.infuser.InfuserContainerMenu;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;

public class SimpleGemsContainerMenuFactory {

    public enum SimpleGemsContainerMenuType {
        GENERATOR,
        INFUSER
    }

    public static AbstractContainerMenu newMenu(SimpleGemsContainerMenuType type, int i, Inventory playerInventory, SimpleGemsContainerBlockEntity<?> blockEntity, ContainerData data) {
        return switch (type) {
            case GENERATOR -> new GeneratorContainerMenu(i, playerInventory, blockEntity, data);
            case INFUSER -> new InfuserContainerMenu(i, playerInventory, blockEntity, data);
        };
    }

    private SimpleGemsContainerMenuFactory() {}
}
