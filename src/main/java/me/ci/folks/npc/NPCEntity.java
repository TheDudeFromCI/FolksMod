package me.ci.folks.npc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.ci.folks.ai.pathfinding.EntityPathHandler;
import me.ci.folks.ai.pathfinding.IMovement;
import me.ci.folks.ai.pathfinding.IPathfindingGoal;
import me.ci.folks.ai.pathfinding.Path;
import me.ci.folks.ai.pathfinding.PathfindingTask;
import me.ci.folks.ai.pathfinding.goals.MoveToPositionGoal;
import me.ci.folks.ai.pathfinding.movements.BasicMovement;
import me.ci.folks.registry.ModEntities;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.BlockPos;
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

    public void walkTo(double x, double y, double z) {
        List<IMovement> movementTypes = new ArrayList<>();
        movementTypes.add(new BasicMovement(this.level));

        BlockPos start = getOnPos().relative(Direction.UP, 1);
        IPathfindingGoal goal = new MoveToPositionGoal(new BlockPos(x, y, z));

        PathfindingTask task = new PathfindingTask(start, goal, movementTypes);

        Path path = null;
        while (path == null) {
            task.tick();
            path = task.getPath();
        }

        this.pathHandler.setPath(path);
    }

    @Override
    public void tick() {
        super.tick();
        this.pathHandler.tick();
    }
}
