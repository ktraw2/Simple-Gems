package com.ktraw.simplegems.blocks.infuser;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.ktraw.simplegems.SimpleGems;
import com.ktraw.simplegems.registry.Blocks;
import com.ktraw.simplegems.registry.RecipeSerializers;
import com.ktraw.simplegems.registry.RecipeTypes;
import com.ktraw.simplegems.util.JSONHelper;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
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
import net.minecraftforge.common.util.RecipeMatcher;

import javax.annotation.Nonnull;
import java.util.List;

@Getter
@ToString
public class InfuserRecipe implements Recipe<InfuserBlockEntity> {
    public static final ResourceLocation NAME = new ResourceLocation(SimpleGems.MODID, "infuser");
    private static final ResourceLocation.Serializer resourceLocationSerializer = new ResourceLocation.Serializer();
    private final RecipeType<InfuserRecipe> type = RecipeTypes.INFUSER_RECIPE_TYPE.get();
    private final RecipeSerializer<InfuserRecipe> serializer = RecipeSerializers.INFUSER_SERIALIZER.get();
    private final ResourceLocation id;
    private final String group;
    private final ItemStack resultItem;
    private final NonNullList<Ingredient> ingredients;
    private final int energy;
    private final int processTime;

    @Getter(AccessLevel.NONE)
    private final boolean isSimple;

    public InfuserRecipe(
            final ResourceLocation id,
            final String group,
            final ItemStack resultItem,
            final NonNullList<Ingredient> ingredients,
            final int energy,
            final int processTime
    ) {
        this.id = id;
        this.group = group;
        this.resultItem = resultItem;
        this.ingredients = ingredients;
        this.energy = energy;
        this.processTime = processTime;
        isSimple = ingredients.stream().allMatch(Ingredient::isSimple);
    }

    public InfuserRecipe(final CompoundTag nbt) {
        this.id = resourceLocationSerializer.deserialize(JsonParser.parseString(nbt.getString("id")), null, null);
        this.group = nbt.getString("group");
        this.resultItem = ItemStack.of(nbt.getCompound("resultItem"));

        final NonNullList<Ingredient> ingredients = NonNullList.create();
        final ListTag ingredientsListTag = nbt.getList("ingredients", Tag.TAG_STRING);

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
        final CompoundTag nbt = new CompoundTag();
        nbt.putString("id", resourceLocationSerializer.serialize(id, null, null).toString());
        nbt.putString("group", group);
        nbt.put("resultItem", resultItem.serializeNBT());

        final ListTag ingredientsListTag = new ListTag();
        for (final Ingredient i : ingredients) {
            ingredientsListTag.add(StringTag.valueOf(i.toJson().toString()));
        }

        nbt.put("ingredients", ingredientsListTag);

        nbt.putInt("energy", energy);
        nbt.putInt("processTime", processTime);
        nbt.putBoolean("isSimple", isSimple);

        return nbt;
    }

    @Override
    public boolean matches(
            @Nonnull final InfuserBlockEntity inv,
            @Nonnull final Level worldIn
    ) {
        final StackedContents recipeitemhelper = new StackedContents();
        final List<ItemStack> inputs = new java.util.ArrayList<>();
        int i = 0;

        for (int j = 0; j < InfuserBlockEntity.TOTAL_CRAFTING_SLOTS; ++j) {
            final ItemStack itemstack = inv.getItem(j);
            if (!itemstack.isEmpty()) {
                ++i;
                if (isSimple) {
                    recipeitemhelper.accountStack(itemstack, 1);
                } else {
                    inputs.add(itemstack);
                }
            }
        }

        return i == this.ingredients.size()
                && (
                isSimple
                        ? recipeitemhelper.canCraft(this, null)
                        : RecipeMatcher.findMatches(inputs, this.ingredients) != null
        );

    }

    @Nonnull
    @Override
    public ItemStack assemble(
            @Nonnull final InfuserBlockEntity inv,
            @Nonnull final RegistryAccess registryAccess
    ) {
        return resultItem.copy();
    }

    @Nonnull
    @Override
    public ItemStack getResultItem(@Nonnull final RegistryAccess registryAccess) {
        return resultItem;
    }

    @Override
    public boolean canCraftInDimensions(
            final int width,
            final int height
    ) {
        return ingredients.size() <= width * height;
    }

    public static class Serializer implements RecipeSerializer<InfuserRecipe> {
        @Nonnull
        @Override
        public InfuserRecipe fromJson(
                @Nonnull final ResourceLocation recipeId,
                @Nonnull final JsonObject json
        ) {
            // get the group
            final String group = JSONHelper.getStringOrDefault("group", json, "");

            // built the ingredients list
            final NonNullList<Ingredient> ingredients = NonNullList.create();
            final JsonArray jsonIngredients = json.get("ingredients").getAsJsonArray();
            for (int i = 0; i < jsonIngredients.size(); i++) {
                final Ingredient ingredient = Ingredient.fromJson(jsonIngredients.get(i));
                if (!ingredient.isEmpty()) {
                    ingredients.add(ingredient);
                }
            }

            // validate the ingredients list
            if (ingredients.isEmpty()) {
                throw new JsonParseException("No ingredients specified");
            } else if (ingredients.size() > InfuserBlockEntity.TOTAL_CRAFTING_SLOTS) {
                throw new JsonParseException("Too many ingredients");
            }

            final int energy = JSONHelper.getIntOrDefault("energy", json, 0);

            final int processTime = JSONHelper.getIntOrDefault("processTime", json, 0);

            // get the output
            final ItemStack recipeOutput = ShapedRecipe.itemStackFromJson(json.get("result").getAsJsonObject());

            // return a Java object with the parsed data
            return new InfuserRecipe(recipeId, group, recipeOutput, ingredients, energy, processTime);
        }

        @Override
        public InfuserRecipe fromNetwork(
                @Nonnull final ResourceLocation recipeId,
                final FriendlyByteBuf buffer
        ) {
            final String group = buffer.readUtf(0x7FFF);

            final int sizeOfIngredients = buffer.readVarInt();
            final NonNullList<Ingredient> ingredients = NonNullList.withSize(sizeOfIngredients, Ingredient.EMPTY);

            ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

            final int energy = buffer.readVarInt();

            final int processTime = buffer.readVarInt();

            final ItemStack recipeOutput = buffer.readItem();

            return new InfuserRecipe(recipeId, group, recipeOutput, ingredients, energy, processTime);
        }

        @Override
        public void toNetwork(
                final FriendlyByteBuf buffer,
                final InfuserRecipe recipe
        ) {
            buffer.writeUtf(recipe.group, 0x7FFF);

            buffer.writeVarInt(recipe.ingredients.size());

            for (final Ingredient ingredient : recipe.ingredients) {
                ingredient.toNetwork(buffer);
            }

            buffer.writeVarInt(recipe.energy);

            buffer.writeVarInt(recipe.processTime);

            buffer.writeItemStack(recipe.resultItem, false); // TODO: should be false?
        }
    }

    @Nonnull
    @Override
    public ItemStack getToastSymbol() {
        return new ItemStack(Blocks.INFUSER.get());
    }
}
