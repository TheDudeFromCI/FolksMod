package me.ci.folks.npc;

import java.util.Arrays;

import me.ci.folks.ai.commands.MoveToPositionCmd;
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
    private final NPCController controller;
    private String name;

    public NPCEntity(World world, String name) {
        super(ModEntities.NPC, world);

        this.inventory = new NPCInventory(this, 36, 4);
        this.controller = new NPCController(this);

        setCustomNameVisible(true);
        setPersistenceRequired();
        setNPCName(name);

        this.controller.addCommand(new MoveToPositionCmd());
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
            .add(Attributes.ATTACK_DAMAGE, 0.5);
    }

    public String getNPCName() {
        return name;
    }

    public void setNPCName(String name) {
        this.name = name;
        setCustomName(new StringTextComponent(name));
    }

    @Override
    public void tick() {
        this.controller.tick();
        super.tick();
    }

    public NPCController getController() {
        return this.controller;
    }

    public void runCommand(String... args) {
        this.controller.runCommand(args);
    }
}
