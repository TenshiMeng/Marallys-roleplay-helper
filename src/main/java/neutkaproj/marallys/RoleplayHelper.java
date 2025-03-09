package neutkaproj.marallys;

import net.fabricmc.api.ModInitializer;
import neutkaproj.marallys.command.Roll;
import neutkaproj.marallys.method.TheCurseOfDarkness;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import neutkaproj.marallys.command.Try;


public class RoleplayHelper implements ModInitializer {
	public static final String MOD_ID = "roleplayhelper";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		TheCurseOfDarkness.CurseMethod();
		Try.tryCommand();
		Roll.rollCommand();
	}
}