package neutkaproj.marallys.method;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class thecurseofdarkness {
    // Количество тиков в минуту (20 тиков/сек * 60 сек = 1200 тиков).
    private static final int TICKS_PER_MINUTE = 1200;

    // Лимит спавна на одну минуту.
    private static final int MAX_ZOMBIES_PER_MINUTE = 20;
    private static final int MAX_LIGHTNING_PER_MINUTE = 20;

    // Текущие счётчики (обнуляются по истечении минуты).
    private static int tickCounter = 0;
    private static int spawnLimitZombies = MAX_ZOMBIES_PER_MINUTE;
    private static int spawnLimitLightning = MAX_LIGHTNING_PER_MINUTE;

    public static void CurseMethod() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (!world.isClient) {
                // Увеличиваем счётчик тиков
                tickCounter++;

                // Если прошла одна «игровая минута», обнуляем счётчики
                if (tickCounter >= TICKS_PER_MINUTE) {
                    tickCounter = 0;
                    spawnLimitZombies = MAX_ZOMBIES_PER_MINUTE;
                    spawnLimitLightning = MAX_LIGHTNING_PER_MINUTE;
                }

                // Вызываем метод спавна
                spawnZombiesAndStrikeLightning((ServerWorld) world);
            }
        });
    }

    private static void spawnZombiesAndStrikeLightning(ServerWorld world) {
        // Выбираем только тех игроков, которые не slghtrhs и чьи координаты (X,Z) > 4000
        List<ServerPlayerEntity> playersToNotify = world.getPlayers().stream()
                .filter(player -> !player.getName().getString().equals("slghtrhs")
                        && (player.getX() > 4000 || player.getZ() > 4000))
                .collect(Collectors.toList());

        for (ServerPlayerEntity player : playersToNotify) {

            // Если исчерпаны оба лимита, прерываем цикл — спавнить больше нельзя
            if (spawnLimitZombies <= 0 && spawnLimitLightning <= 0) {
                break;
            }
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            // Спавним зомби, если осталось разрешение
            if (spawnLimitZombies > 0) {
                ZombieEntity zombie = new ZombieEntity(EntityType.ZOMBIE, world);
                // Метод спавна сущностей около игрока
                zombie.refreshPositionAndAngles(
                        x + world.random.nextGaussian() * 2,
                        y,
                        z + world.random.nextGaussian() * 2,
                        world.random.nextFloat() * 360F,
                        0
                );
                world.spawnEntity(zombie);

                // Уменьшаем лимит на зомби
                spawnLimitZombies--;
            }

            // Спавним молнию, если осталось разрешение
            if (spawnLimitLightning > 0) {
                LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                lightning.setPosition(player.getX(),player.getY(),player.getZ());
                world.spawnEntity(lightning);

                // Уменьшаем лимит молний
                spawnLimitLightning--;
            }
        }
    }
}


