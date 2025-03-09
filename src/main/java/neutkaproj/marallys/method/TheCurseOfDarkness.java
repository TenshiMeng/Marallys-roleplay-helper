package neutkaproj.marallys.method;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.StrayEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.List;
import java.util.stream.Collectors;

public class TheCurseOfDarkness {
    // Количество тиков в минуту (20 тиков/сек * 60 сек = 1200 тиков).
    private static final int TICKS_PER_MINUTE = 1200;

    // Лимит спавна на одну минуту.
    private static final int MAX_ENEMY_PER_MINUTE = 3;
    private static final int MAX_EFFECT_PER_MINUTE = 4;

    // Текущие счётчики (обнуляются по истечении минуты).
    private static int tickCounter = 0;
    private static int spawnLimitEnemy = MAX_ENEMY_PER_MINUTE;
    private static int spawnLimitEffect = MAX_EFFECT_PER_MINUTE;

    // Длительность эффектов (1 минута = 1200 тиков).
    private static final int EFFECT_DURATION = 400;

    public static void CurseMethod() {
        ServerTickEvents.END_WORLD_TICK.register(world -> {
            if (!world.isClient) {
                // Увеличиваем счётчик тиков
                tickCounter++;

                // Если прошла одна «игровая минута», обнуляем счётчики
                if (tickCounter >= TICKS_PER_MINUTE) {
                    tickCounter = 0;
                    spawnLimitEnemy = MAX_ENEMY_PER_MINUTE;
                    spawnLimitEffect = MAX_EFFECT_PER_MINUTE;
                }

                // Вызываем метод спавна
                spawnEnemyAndEffect((ServerWorld) world);
            }
        });
    }

    private static void spawnEnemyAndEffect(ServerWorld world) {
        // Выбираем только тех игроков, которые не slghtrhs и чьи координаты (X,Z) > 4000 и кто в выживании
        List<ServerPlayerEntity> playersToNotify = world.getPlayers().stream()
                .filter (player -> !player.getName().getString().equals("slghtrhs") && (!player.isCreative() && (!player.isSpectator()))
                        && (player.getX() >= 4000 || player.getX() <= -4000 || player.getZ() >= 4000 || player.getZ() <= -4000))
                .collect(Collectors.toList());

        for (ServerPlayerEntity player : playersToNotify) {
            if (spawnLimitEnemy <= 0 && spawnLimitEffect <= 0) {
                break;
            }
            double x = player.getX();
            double y = player.getY();
            double z = player.getZ();
            // Спавним зомби, если осталось разрешение
            if (spawnLimitEnemy > 0) {
                // Получаем тип сущности из ID

                StrayEntity stray = new StrayEntity(EntityType.STRAY, world);
                // Получаем тип сущности гулей по идентификатору
                // Метод спавна сущностей около игрока
                stray.refreshPositionAndAngles(
                        x + world.random.nextGaussian() * 3,
                        y,
                        z + world.random.nextGaussian() * 3,
                        world.random.nextFloat() * 360F,
                        0
                );
                world.spawnEntity(stray);
                //iceandfire:dread_ghoul
                // Уменьшаем лимит на зомби
                spawnLimitEnemy--;
            }

            // Спавним молнию, если осталось разрешение
            if (spawnLimitEffect > 0) {
                LightningEntity lightning = new LightningEntity(EntityType.LIGHTNING_BOLT, world);
                lightning.setPosition(player.getX(),player.getY(),player.getZ());
                world.spawnEntity(lightning);

                // Добавляем эффекты слабости, тошноты и слепоты на 1 минуту
                // Уровень эффекта 0 означает Level I
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, EFFECT_DURATION, 0));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, EFFECT_DURATION, 0));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, EFFECT_DURATION, 0));
                // Если исчерпаны оба лимита, прерываем цикл — спавнить больше нельзя

                // Уменьшаем лимит молний
                spawnLimitEffect--;
            }
        }
    }
}


