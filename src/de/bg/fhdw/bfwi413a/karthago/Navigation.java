/**
 * Die Navigation Class steuert den Datenaustausch zwischen den
 * verschiedenen Activities, sowie deren gegenseitigen Aufruf.
 *  
 * In dieser Klasse werden alle notwendigen persistenten und nicht persistenten
 * Daten übermittelt
 * 
 * Autor: Leonie Schiburr
 *  */

package de.bg.fhdw.bfwi413a.karthago;

import android.app.Activity;
import android.content.Intent;

public class Navigation {

	public static final Class<?> LOGIN_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.activities.login.Init.class;
	public static final String KEY_LOGIN_ID = "de.bg.fhdw.bfwis413a.karthago.KEY_LOGIN_VALUE";
	
	public static final Class<?> CONFIG_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.ConfigActivity.class;
	public static final String KEY_CONFIG_ID = "de.bg.fhdw.bfwi413a.karthago.KEY_CONFIG_VALUE";
	
	public static final Class<?> SELECTION_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.activities.selection.Init.class;
	public static final String KEY_SELECTION_ID = "de.bg.fhdw.bfwi413a.karthago.KEY_SELECTION_VALUE";
	
	public static final Class<?> MENU_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.activities.menu.Init.class;
	public static final String KEY_MENU_ID = "de.bg.fhdw.bfwi413a.karthago.KEY_MENU_VALUE";
	
	public static final Class<?> STATISTICS_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.activities.statistics.Init.class;
	public static final String KEY_STATISTICS_ID = "de.bg.fhdw.bfwi413a.karthago.KEY_MENU_VALUE";
	
	public static final Class<?> LM3_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.activities.lm3_g.Init.class;
	public static final String KEY_LM3_ID = "de.bg.fhdw.bfwi413a.karthago.KEY_LM3_VALUE";
	
	public static final Class<?> LM2_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.activities.lm2_ft.Init.class;
	public static final String KEY_LM2_ID = "de.bg.fhdw.bfwi413a.karthago.KEY_LM2_VALUE";
	
	public static final Class<?> LM1_ACTIVITY_CLASS =
			de.bg.fhdw.bfwi413a.karthago.activities.lm1_mc.Init.class;
	public static final String KEY_LM1_ID = "de.bg.fhdw.bfwi413a.karthago.KEY_LM1_VALUE";
	
	private static void startActivity (Activity activity, Class<?> classOfActivityToStart) {
		Intent intent;
		
		intent = new Intent();
		intent.setClass(activity, classOfActivityToStart);
		//intent.putExtra(key, par);
		activity.startActivity(intent);
		activity.finish();
	}
	
	private static void startActivity (Activity activity, Class<?> classOfActivityToStart, String key, String par) {
		Intent intent;
		
		intent = new Intent();
		intent.setClass(activity, classOfActivityToStart);
		intent.putExtra(key, par);
		activity.startActivity(intent);
		activity.finish();
	}
	
	public static void startActivityLogin (Activity callingActivity) {
		startActivity(callingActivity, LOGIN_ACTIVITY_CLASS);
	}
	
	public static void startActivitySelection (Activity callingActivity) {
		startActivity(callingActivity, SELECTION_ACTIVITY_CLASS);
	}
	
	public static void startActivityConfig (Activity callingActivity) {
		startActivity(callingActivity, CONFIG_ACTIVITY_CLASS);
	}
	
	public static void startActivityMenu (Activity callingActivity){
		startActivity(callingActivity, MENU_ACTIVITY_CLASS);
	}

	public static void startActivityStatistics(Activity callingActivity) {
		startActivity(callingActivity, MENU_ACTIVITY_CLASS);		
	}
	
	public static void startActivityLM1_MC(Activity callingActivity, String questionId) {
		startActivity(callingActivity, LM1_ACTIVITY_CLASS, "currentQuestionId", questionId);		
	}
	
	public static void startActivityLM2_FT(Activity callingActivity, String questionId) {
		startActivity(callingActivity, LM2_ACTIVITY_CLASS, "currentQuestionId", questionId);		
	}
	
	public static void startActivityLM3_G(Activity callingActivity, String questionId) {
		startActivity(callingActivity, LM3_ACTIVITY_CLASS, "currentQuestionId", questionId);		
	}


	
}
