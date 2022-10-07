package com.faboslav.friendsandfoes.entity.ai.brain;

import com.faboslav.friendsandfoes.FriendsAndFoes;
import com.faboslav.friendsandfoes.entity.WildfireEntity;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.Activity;
import net.minecraft.entity.ai.brain.Brain;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.ai.brain.sensor.Sensor;
import net.minecraft.entity.ai.brain.sensor.SensorType;
import net.minecraft.entity.ai.brain.task.*;
import net.minecraft.util.math.intprovider.UniformIntProvider;

import java.util.List;

@SuppressWarnings({"rawtypes", "unchecked"})
public final class WildfireBrain
{
	public static final List<MemoryModuleType<?>> MEMORY_MODULES;
	public static final List<SensorType<? extends Sensor<? super WildfireEntity>>> SENSORS;

	public WildfireBrain() {
	}

	public static Brain<?> create(Brain<WildfireEntity> brain) {
		addCoreActivities(brain);
		addIdleActivities(brain);
		addFightActivities(brain);

		brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
		brain.setDefaultActivity(Activity.IDLE);
		brain.resetPossibleActivities();

		return brain;
	}

	private static void addCoreActivities(Brain<WildfireEntity> brain) {
		brain.setTaskList(Activity.CORE,
			0,
			ImmutableList.of(
				new StayAboveWaterTask(0.8F),
				new WalkTask(2.5F),
				new LookAroundTask(45, 90),
				new WanderAroundTask()
			));
	}

	private static void addIdleActivities(
		Brain<WildfireEntity> brain
	) {
		brain.setTaskList(Activity.IDLE,
			ImmutableList.of(
				Pair.of(0, new RandomTask(
					ImmutableList.of(
						Pair.of(new StrollTask(1.0F), 2),
						Pair.of(new GoTowardsLookTarget(1.0F, 3), 2),
						Pair.of(new WaitTask(30, 60), 1)
					)
				))
			)
		);
	}


	private static void addFightActivities(
		Brain<WildfireEntity> brain
	) {

	}

	public static void updateActivities(WildfireEntity wildfire) {
		wildfire.getBrain().resetPossibleActivities(ImmutableList.of(
			Activity.IDLE
		));
	}

	static {
		SENSORS = List.of(
			SensorType.NEAREST_PLAYERS,
			SensorType.HURT_BY
		);
		MEMORY_MODULES = List.of(
			MemoryModuleType.NEAREST_VISIBLE_PLAYER,
			MemoryModuleType.NEAREST_VISIBLE_TARGETABLE_PLAYER,
			MemoryModuleType.PATH,
			MemoryModuleType.LOOK_TARGET,
			MemoryModuleType.WALK_TARGET,
			MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE
		);
	}
}
