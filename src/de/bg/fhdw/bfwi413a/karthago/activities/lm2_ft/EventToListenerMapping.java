package de.bg.fhdw.bfwi413a.karthago.activities.lm2_ft;

//author: Leonie

import android.view.View;
import android.view.View.OnClickListener;

public class EventToListenerMapping implements OnClickListener{

	private ApplicationLogic mApplicationLogic;
	private Gui mGui;
	
	public EventToListenerMapping (Gui gui, ApplicationLogic applicationLogic) {
		mApplicationLogic = applicationLogic;
		mGui = gui;
//		mGui.getmButtonSENDFT().setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
//		mApplicationLogic.onButtonClicked();		
	}
	
}
