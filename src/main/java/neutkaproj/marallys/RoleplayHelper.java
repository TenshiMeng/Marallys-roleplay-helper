package neutkaproj.marallys;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import net.minecraft.text.Style;

public class RoleplayHelper implements ModInitializer {
	public static final String MOD_ID = "roleplayhelper";
	public static final String ANSI_RESET = "#FFAA00";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	private static final Random RANDOM = new Random();
	public static String Done = new String();

	@Override
	public void onInitialize() {
		//Создаем определение стиля для сообщений
		Style msgStyle = Style.EMPTY.withColor(0xFFD700);
		//Создаем цветные статусы
		//Блок - успешно
		String successMessageString = "Успешно!";
		String failureMessageString = "Неуспешно...";
		Style successMessage = Style.EMPTY.withColor(0x00FF00).withBold(true);
		//Блок - не успешно
		Style failureMessage = Style.EMPTY.withColor(0xFF0000).withBold(true);

		//Инициализируем в логах...
		LOGGER.info("Initializing RoleplayHelper...");
		// Регистрация базовой команды
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(CommandManager.literal("try")
					.requires(source -> source.hasPermissionLevel(0))
					.executes(context -> {
						// Если команда вызвана без аргумента
						Style.EMPTY.withColor(0xFFD700);
						context.getSource().sendFeedback(() -> Text.literal("Команда /try написана без условия. Добавьте любой текст после /try.").setStyle(msgStyle), false);

						return 1;
					})
					.then(CommandManager.argument("action", StringArgumentType.greedyString())
							.executes(context -> {
								String action = StringArgumentType.getString(context, "action");
								boolean success = RANDOM.nextBoolean(); // 50% шанс

								ServerPlayerEntity player = context.getSource().getPlayer(); // Получаем игрока
								// Создаем цветные компоненты для "Удачно" и "Неудачно"
								//Выводим сообщение
								if (success == true) {
								String resultMessage;
								resultMessage = ("Игрок " + player.getName().getString() + " совершил действие: " + action);
								player.sendMessage(Text.literal(resultMessage).setStyle(msgStyle));
								player.sendMessage(Text.literal(successMessageString).setStyle(successMessage));
								} else {
									String resultMessage;
									resultMessage = ("Игрок " + player.getName().getString() + " совершил действие: " + action);
									player.sendMessage(Text.literal(resultMessage).setStyle(msgStyle));
									player.sendMessage(Text.literal(failureMessageString).setStyle(failureMessage));
								}
								return 1;
							})
					)
			);
		});
	}

	//Реализация интерфейса получения контекста для реализации при выполнении команды.
	Command<ServerCommandSource> command = context -> {
		ServerCommandSource source = context.getSource(); //вместо .getSource - любой из вариантов.
		return 0;

	};
}

