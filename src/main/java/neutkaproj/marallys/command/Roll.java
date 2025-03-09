package neutkaproj.marallys.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Collection;
import java.util.Random;

public class Roll {
    private static final Random RANDOM = new Random();
    //Создаем определение стиля для сообщений


    public static void rollCommand() {
        //Создаем цветные статусы
        Style msgStyleRoll = Style.EMPTY.withColor(Formatting.GOLD).withBold(false);
        Style perfectRollMessage = Style.EMPTY.withColor(Formatting.BLUE).withBold(true);
        Style successRollMessage = Style.EMPTY.withColor(Formatting.GREEN).withBold(true);
        Style failureRollMessage = Style.EMPTY.withColor(Formatting.YELLOW).withBold(true);
        Style losslessRollMessage = Style.EMPTY.withColor(Formatting.RED).withBold(true);

        //Регистрация команды
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(CommandManager.literal("roll")
                    .requires(source -> source.hasPermissionLevel(0))
                    .executes(context -> {
                        // Если команда вызвана без аргумента
                        context.getSource().sendFeedback(() -> Text.literal("Команда /roll написана без условия. Добавьте любой текст после /roll.").setStyle(msgStyleRoll), false);
                        return 1;
                    })

            .then(CommandManager.argument("action", StringArgumentType.greedyString())
                    .executes(context -> {
                        // Получаем источник команды
                        ServerCommandSource source = context.getSource();
                        String action = StringArgumentType.getString(context, "action");


                        // Получаем список всех игроков на сервере
                        Collection<ServerPlayerEntity> players = source.getServer().getPlayerManager().getPlayerList();
                        ServerPlayerEntity player_command = context.getSource().getPlayer();// Получаем игрока

                        // Создаем цветные компоненты для "Удачно" и "Неудачно"
                        //Выводим сообщение
                        int roll_result = RANDOM.nextInt(1,20);
                        //scope - player_command - игрок, который писал комманду.
                        String resultMessage = ("Игрок " + player_command.getName().getString() + " пытается " + action + " и кидает кубик. Выпадает...");
                        String roll_result_output = String.valueOf(roll_result); //Интерпретируем Int в String для вывода в чат

                        //Вывод результата в чат
                        switch (roll_result){
                            case 20, 19, 18, 17:
                                for (ServerPlayerEntity player : players) {
                                    player.sendMessage(Text.literal(resultMessage).setStyle(msgStyleRoll));
                                    player.sendMessage(Text.literal(roll_result_output + ":)").setStyle(perfectRollMessage));
                                }
                                break;
                            case 16, 15, 14, 13, 12:
                                for (ServerPlayerEntity player : players) {
                                    player.sendMessage(Text.literal(resultMessage).setStyle(msgStyleRoll));
                                    player.sendMessage(Text.literal(roll_result_output + "!").setStyle(successRollMessage));
                                }
                                break;
                            case 11, 10, 9, 8, 7, 6:
                                for (ServerPlayerEntity player : players) {
                                    player.sendMessage(Text.literal(resultMessage).setStyle(msgStyleRoll));
                                    player.sendMessage(Text.literal(roll_result_output + "!").setStyle(failureRollMessage));
                                }
                                break;
                            case 5, 4, 3, 2, 1:
                                for (ServerPlayerEntity player : players) {
                                    player.sendMessage(Text.literal(resultMessage).setStyle(msgStyleRoll));
                                    player.sendMessage(Text.literal(roll_result_output + ":(").setStyle(losslessRollMessage));
                                }
                                break;
                            default:
                                context.getSource().sendFeedback(() -> Text.literal("Команда /roll написана без условия. Добавьте любой текст после /roll."), false);
                        }
                        return 1;
                    })
            ));
        });
    }
}
