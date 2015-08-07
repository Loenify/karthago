package de.bg.fhdw.bfwi413a.karthago.activities.menu;

import de.bg.fhdw.bfwi413a.karthago.activities.menu.Data;
import android.app.Activity;
import android.os.Bundle;

public class Init extends Activity{
	
	private Data mData;
	private Gui mGui;
	private ApplicationLogic mApplicationLogic;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData(savedInstanceState);
		initGui();
		initApplicationLogic();
		initEventToListenerMapping();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}
	
	private void initData(Bundle savedInstanceState) {
		mData = new Data(this, savedInstanceState);
	}
	
	private void initGui(){
		mGui = new Gui(this);
	}
	
	private void initApplicationLogic(){
		mApplicationLogic = new ApplicationLogic(mGui, mData);
	}

	private void initEventToListenerMapping(){
		new EventToListenerMapping(mGui, mApplicationLogic);
	}
}
