package neutkaproj.marallys;

import com.mojang.brigadier.Command;
import net.minecraft.entity.EntityType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.network.message.SignedMessage;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.*;
import neutkaproj.marallys.method.thecurseofdarkness;
import org.apache.logging.log4j.message.LocalizedMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static net.minecraft.network.message.SentMessage.Chat;
import neutkaproj.marallys.command.trycommand;
import java.util.Collection;
import java.util.Random;
import net.minecraft.text.Style;


public class RoleplayHelper implements ModInitializer {
	public static final String MOD_ID = "roleplayhelper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		thecurseofdarkness.CurseMethod();
		trycommand.tryCommand();


	}
}