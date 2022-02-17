package me.ci.folks.npc;

import java.util.Arrays;

import javax.annotation.Nullable;

import me.ci.folks.ai.pathfinding.EntityPathHandler;
import me.ci.folks.ai.pathfinding.Path;
import me.ci.folks.registry.ModEntities;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class NPCEntity extends CreatureEntity {

    private final NPCInventory inventory;
    private final EntityPathHandler pathHandler;
    private String name;

    public NPCEntity(World world, String name) {
        super(ModEntities.NPC, world);

        this.inventory = new NPCInventory(this, 36, 4);
        this.pathHandler = new EntityPathHandler(this);

        setCustomNameVisible(true);
        setPersistenceRequired();
        setNPCName(name);
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Arrays.asList(
            this.inventory.getItem(this.inventory.getArmorSlot(NPCInventory.HEAD_SLOT)),
            this.inventory.getItem(this.inventory.getArmorSlot(NPCInventory.BODY_SLOT)),
            this.inventory.getItem(this.inventory.getArmorSlot(NPCInventory.LEGS_SLOT)),
            this.inventory.getItem(this.inventory.getArmorSlot(NPCInventory.FEET_SLOT)));
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType type) {
        return this.inventory.getItem(this.inventory.getItemSlotByType(type));
    }

    @Override
    public void setItemSlot(EquipmentSlotType type, ItemStack item) {
        this.inventory.setItem(this.inventory.getItemSlotByType(type), item);
    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    @Override
    protected boolean shouldDropExperience() {
        return false;
    }

    public static AttributeModifierMap.MutableAttribute createNPCAttributes() {
        return MobEntity.createMobAttributes()
            .add(Attributes.MOVEMENT_SPEED, 0.5)
            .add(Attributes.ATTACK_DAMAGE, 0.5)
            .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    public String getNPCName() {
        return name;
    }

    public void setNPCName(String name) {
        this.name = name;
        setCustomName(new StringTextComponent(name));
    }

    public void setCurrentPath(@Nullable Path path) {
        this.pathHandler.setPath(path);
    }

    @Nullable
    public Path getCurrentPath() {
        return this.pathHandler.getPath();
    }

    @Override
    public void tick() {
        super.tick();
        this.pathHandler.tick();
    }
}
