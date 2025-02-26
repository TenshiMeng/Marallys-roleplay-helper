package neutkaproj.marallys.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Random;

public class trycommand {
    private static final Random RANDOM = new Random();
    public static void tryCommand() {
        ////БЛОК КОМАНДЫ /TRY
        //Создаем определение стиля для сообщений
        Style msgStyle = Style.EMPTY.withColor(0xFFD700);
        //Создаем цветные статусы
        //Блок - успешно
        String successMessageString = "Успешно!";
        String failureMessageString = "Неуспешно...";
        Style successMessage = Style.EMPTY.withColor(0x00FF00).withBold(true);
        //Блок - не успешно
        Style failureMessage = Style.EMPTY.withColor(0xFF0000).withBold(true);

        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            trycommand.register(dispatcher);
            // Здесь можно зарегистрировать другие команды
        });

        // Регистрация базовой команды try
        CommandRegistrationCallback.EVENT.register((dispatcher_try, registryAccess_try, environment_try) -> {
            dispatcher_try.register(CommandManager.literal("try")
                    .requires(source -> source.hasPermissionLevel(0))
                    .executes(context -> {
                        // Если команда вызвана без аргумента
                        context.getSource().sendFeedback(() -> Text.literal("Команда /try написана без условия. Добавьте любой текст после /try.").setStyle(msgStyle), false);
                        return 1;
                    })

                    .then(CommandManager.argument("action", StringArgumentType.greedyString())
                            .executes(context -> {
                                // Получаем источник команды
                                ServerCommandSource source = context.getSource();
                                String action = StringArgumentType.getString(context, "action");
                                boolean success = RANDOM.nextBoolean(); // 50% шанс

                                // Получаем список всех игроков на сервере
                                Collection<ServerPlayerEntity> players = source.getServer().getPlayerManager().getPlayerList();
                                ServerPlayerEntity player_command = context.getSource().getPlayer();// Получаем игрока

                                // Создаем цветные компоненты для "Удачно" и "Неудачно"
                                //Выводим сообщение

                                //scope - player_command - игрок, который писал комманду.
                                String resultMessage_true;
                                resultMessage_true = ("Игрок " + player_command.getName().getString() + " совершил действие: " + action);

                                if (success) {
                                    for (ServerPlayerEntity player : players) {
                                        player.sendMessage(Text.literal(resultMessage_true).setStyle(msgStyle));
                                        player.sendMessage(Text.literal(successMessageString).setStyle(successMessage));
                                    }

                                } else if (success == false) {
                                    for (ServerPlayerEntity player : players) {
                                        player.sendMessage(Text.literal(resultMessage_true).setStyle(msgStyle));
                                        player.sendMessage(Text.literal(failureMessageString).setStyle(failureMessage));
                                    }
                                }
                                return 1;
                            })
                    ));
        });
    }

    private static void register(CommandDispatcher<ServerCommandSource> dispatcher) {

    }
}

