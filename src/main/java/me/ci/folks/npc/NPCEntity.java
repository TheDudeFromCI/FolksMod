package me.ci.folks.npc;

import java.util.Arrays;

import me.ci.folks.registry.ModEntities;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.LookAtGoal;
import net.minecraft.entity.ai.goal.LookRandomlyGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.HandSide;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class NPCEntity extends CreatureEntity {

    private final NPCInventory inventory;
    private String name;

    public NPCEntity(World world, String name) {
        super(ModEntities.NPC, world);

        inventory = new NPCInventory(this, 36, 4);

        setCustomNameVisible(true);
        setPersistenceRequired();
        setNPCName(name);

        GroundPathNavigator nav = (GroundPathNavigator) getNavigation();
        nav.setCanFloat(true);
        nav.setCanOpenDoors(true);
    }

    @Override
    public void aiStep() {
        this.updateSwingTime();
        super.aiStep();
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Arrays.asList(inventory.getItem(inventory.getArmorSlot(NPCInventory.HEAD_SLOT)),
                inventory.getItem(inventory.getArmorSlot(NPCInventory.BODY_SLOT)),
                inventory.getItem(inventory.getArmorSlot(NPCInventory.LEGS_SLOT)),
                inventory.getItem(inventory.getArmorSlot(NPCInventory.FEET_SLOT)));
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType type) {
        return inventory.getItem(inventory.getItemSlotByType(type));
    }

    @Override
    public void setItemSlot(EquipmentSlotType type, ItemStack item) {
        inventory.setItem(inventory.getItemSlotByType(type), item);
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
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.ATTACK_DAMAGE, 0.5)
                .add(Attributes.FOLLOW_RANGE, 32.0);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0, 120));
        goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 8f, 1f));
        goalSelector.addGoal(8, new LookRandomlyGoal(this));
    }

    public String getNPCName() {
        return name;
    }

    public void setNPCName(String name) {
        this.name = name;
        setCustomName(new StringTextComponent(name));
    }
}
