package cazador.furnaceoverhaul.utils;

import org.apache.commons.codec.language.bm.Lang;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cazador.furnaceoverhaul.Reference;

public class Utils {
	
	private static Logger logger;
	private static Lang lang;
	
	public static Logger getLogger() {
		if(logger == null) {
			logger = LogManager.getFormatterLogger(Reference.MOD_ID);
		}
		return logger;
	}
	
}