package com.ktraw.simplegems.blocks.infuser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.ModBlocks;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.Arrays;

public class InfuserRecipe implements IRecipe<InfuserTile> {

    private final IRecipeType<InfuserRecipe> type = ModBlocks.INFUSER_RECIPE_TYPE;
    private final IRecipeSerializer<InfuserRecipe> serializer = ModBlocks.INFUSER_SERIALIZER;

    private final ResourceLocation id;
    private final String group;
    private final ItemStack recipeOutput;
    private final NonNullList<Ingredient> ingredients;
    private final int energy;
    private final int processTime;

    private final boolean isSimple;

    public InfuserRecipe(ResourceLocation id, String group, ItemStack recipeOutput, NonNullList<Ingredient> ingredients, int energy, int processTime) {
        this.id = id;
        this.group = group;
        this.recipeOutput = recipeOutput;
        this.ingredients = ingredients;
        this.energy = energy;
        this.processTime = processTime;
        isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    @Override
    public boolean matches(InfuserTile inv, World worldIn) {
        RecipeItemHelper recipeitemhelper = new RecipeItemHelper();
        java.util.List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < inv.getSizeInventory(); ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple)
                    recipeitemhelper.func_221264_a(itemstack, 1);
                else inputs.add(itemstack);
            }
        }

        return i == this.ingredients.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);

    }

    @Override
    public ItemStack getCraftingResult(InfuserTile inv) {
        return recipeOutput.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return ingredients.size() <= width * height;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return recipeOutput;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return serializer;
    }

    @Override
    public IRecipeType<?> getType() {
        return type;
    }

    @Override
    public String getGroup() {
        return group;
    }

    public int getEnergy() {
        return energy;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InfuserRecipe{");
        sb.append("id=").append(id);
        sb.append(", group='").append(group).append('\'');
        sb.append(", recipeOutput=").append(recipeOutput);
        sb.append(", ingredients=[");
        if (ingredients.size() > 0) {
            for (Ingredient ingredient : ingredients) {
                sb.append(Arrays.toString(ingredient.getMatchingStacks())).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("], energy=").append(energy);
        sb.append(", processTime=").append(processTime);
        sb.append(", isSimple=").append(isSimple);
        sb.append('}');
        return sb.toString();
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<InfuserRecipe> {
        private static final ResourceLocation NAME = new ResourceLocation(SimpleGems.MODID, "infuser");

        public Serializer() {
            setRegistryName("infuser");
        }

        @Override
        public InfuserRecipe read(ResourceLocation recipeId, JsonObject json) {
            // get the group
            String group = JSONUtils.getString(json, "group", "");

            // built the ingredients list
            NonNullList<Ingredient> ingredients = NonNullList.create();
            JsonArray jsonIngredients = JSONUtils.getJsonArray(json, "ingredients");
            for (int i = 0; i < jsonIngredients.size(); i++) {
                Ingredient ingredient = Ingredient.deserialize(jsonIngredients.get(i));
                if (!ingredient.hasNoMatchingItems()) {
                    ingredients.add(ingredient);
                }
            }

            // validate the ingredients list
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients specified");
            }
            else if (ingredients.size() > 4) {
                throw new JsonParseException("Too many ingredients");
            }

            int energy = JSONUtils.getInt(json, "energy", 0);

            int processTime = JSONUtils.getInt(json, "processTime", 0);

            // get the output
            ItemStack recipeOutput = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));

            // return a Java object with the parsed data
            return new InfuserRecipe(recipeId, group, recipeOutput, ingredients, energy, processTime);
        }

        @Override
        public InfuserRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            String group = buffer.readString(0x7FFF);

            int sizeOfIngredients = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(sizeOfIngredients, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.read(buffer));
            }

            int energy = buffer.readVarInt();

            int processTime = buffer.readVarInt();

            ItemStack recipeOutput = buffer.readItemStack();

            return new InfuserRecipe(recipeId, group, recipeOutput, ingredients, energy, processTime);
        }

        @Override
        public void write(PacketBuffer buffer, InfuserRecipe recipe) {
            buffer.writeString(recipe.group, 0x7FFF);

            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.write(buffer);
            }

            buffer.writeVarInt(recipe.energy);

            buffer.writeVarInt(recipe.processTime);

            buffer.writeItemStack(recipe.recipeOutput);
        }
    }
}
