package com.ktraw.simplegems.blocks.infuser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.blocks.ModBlocks;
import com.mojang.realmsclient.util.JsonUtils;
import it.unimi.dsi.fastutil.ints.IntList;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistryEntry;

import java.util.List;

@Getter
@ToString
public class InfuserRecipe implements Recipe<InfuserBlockEntity> {

    public static final ResourceLocation NAME = new ResourceLocation(SimpleGems.MODID, "infuser");
    private static final ResourceLocation.Serializer resourceLocationSerializer = new ResourceLocation.Serializer();

    private final RecipeType<InfuserRecipe> type = ModBlocks.INFUSER_RECIPE_TYPE;
    private final RecipeSerializer<InfuserRecipe> serializer = ModBlocks.INFUSER_SERIALIZER;

    private final ResourceLocation id;
    private final String group;
    private final ItemStack resultItem;
    private final NonNullList<Ingredient> ingredients;
    private final int energy;
    private final int processTime;

    @Getter(AccessLevel.NONE)
    private final boolean isSimple;

    public InfuserRecipe(ResourceLocation id, String group, ItemStack resultItem, NonNullList<Ingredient> ingredients, int energy, int processTime) {
        this.id = id;
        this.group = group;
        this.resultItem = resultItem;
        this.ingredients = ingredients;
        this.energy = energy;
        this.processTime = processTime;
        isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    public InfuserRecipe(CompoundTag nbt) {
        this.id = resourceLocationSerializer.deserialize(JsonParser.parseString(nbt.getString("id")), null, null);
        this.group = nbt.getString("group");
        this.resultItem = ItemStack.of(nbt.getCompound("resultItem"));

        NonNullList<Ingredient> ingredients = NonNullList.create();
        ListTag ingredientsListTag = nbt.getList("ingredients", Tag.TAG_STRING);

        final int size = ingredientsListTag.size();
        for (int i = 0; i < size; i++) {
            ingredients.add(Ingredient.fromJson(JsonParser.parseString(ingredientsListTag.getString(i))));
        }

        this.ingredients = ingredients;

        this.energy = nbt.getInt("energy");
        this.processTime = nbt.getInt("processTime");
        this.isSimple = nbt.getBoolean("isSimple");
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("id", resourceLocationSerializer.serialize(id, null, null).toString());
        nbt.putString("group", group);
        nbt.put("resultItem", resultItem.serializeNBT());

        ListTag ingredientsListTag = new ListTag();
        for (Ingredient i : ingredients) {
            ingredientsListTag.add(StringTag.valueOf(i.toJson().toString()));
        }

        nbt.put("ingredients", ingredientsListTag);

        nbt.putInt("energy", energy);
        nbt.putInt("processTime", processTime);
        nbt.putBoolean("isSimple", isSimple);

        return nbt;
    }

    @Override
    public boolean matches(InfuserBlockEntity inv, Level worldIn) {

        StackedContents recipeitemhelper = new StackedContents();
        List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for(int j = 0; j < InfuserBlockEntity.TOTAL_CRAFTING_SLOTS; ++j) {
            ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple) {
                    recipeitemhelper.accountStack(itemstack, 1);
                }
                else {
                    inputs.add(itemstack);
                }
            }
        }

        return i == this.ingredients.size() && (isSimple ? recipeitemhelper.canCraft(this, (IntList)null) : net.minecraftforge.common.util.RecipeMatcher.findMatches(inputs,  this.ingredients) != null);

    }

    @Override
    public ItemStack assemble(InfuserBlockEntity inv) {
        return resultItem.copy();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return ingredients.size() <= width * height;
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<InfuserRecipe> {

        public Serializer() {
            setRegistryName("infuser");
        }

        @Override
        public InfuserRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            // get the group
            String group = JsonUtils.getStringOr("group", json, "");

            // built the ingredients list
            NonNullList<Ingredient> ingredients = NonNullList.create();
            JsonArray jsonIngredients = json.get("ingredients").getAsJsonArray();
            for (int i = 0; i < jsonIngredients.size(); i++) {
                Ingredient ingredient = Ingredient.fromJson(jsonIngredients.get(i));
                if (!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }

            // validate the ingredients list
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients specified");
            }
            else if (ingredients.size() > InfuserBlockEntity.TOTAL_CRAFTING_SLOTS) {
                throw new JsonParseException("Too many ingredients");
            }

            int energy = JsonUtils.getIntOr("energy", json, 0);

            int processTime = JsonUtils.getIntOr("processTime", json, 0);

            // get the output
            ItemStack recipeOutput = ShapedRecipe.itemStackFromJson(json.get("result").getAsJsonObject()/* JSONUtils.getJsonObject(json, "result")*/);

            // return a Java object with the parsed data
            return new InfuserRecipe(recipeId, group, recipeOutput, ingredients, energy, processTime);
        }

        @Override
        public InfuserRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            String group = buffer.readUtf(0x7FFF);

            int sizeOfIngredients = buffer.readVarInt();
            NonNullList<Ingredient> ingredients = NonNullList.withSize(sizeOfIngredients, Ingredient.EMPTY);

            for (int i = 0; i < ingredients.size(); i++) {
                ingredients.set(i, Ingredient.fromNetwork(buffer));
            }

            int energy = buffer.readVarInt();

            int processTime = buffer.readVarInt();

            ItemStack recipeOutput = buffer.readItem();

            return new InfuserRecipe(recipeId, group, recipeOutput, ingredients, energy, processTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, InfuserRecipe recipe) {
            buffer.writeUtf(recipe.group, 0x7FFF);

            buffer.writeVarInt(recipe.ingredients.size());

            for (Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeVarInt(recipe.energy);

            buffer.writeVarInt(recipe.processTime);

            buffer.writeItemStack(recipe.resultItem, false); // TODO: should be false?
        }
    }

    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(ModBlocks.INFUSER);
    }
}
