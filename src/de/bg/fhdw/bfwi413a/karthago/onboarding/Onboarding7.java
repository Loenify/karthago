package de.bg.fhdw.bfwi413a.karthago.onboarding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import de.bg.fhdw.bfwi413a.karthago.R;

public class Onboarding7 extends Activity{ 
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.onboarding_7);
	}
	
		public boolean onTouchEvent(MotionEvent event) {

		    // get masked (not specific to a pointer) action
		    int maskedAction = event.getActionMasked();

		    switch (maskedAction) {

		    case MotionEvent.ACTION_DOWN:
		    	Intent i = new Intent(Onboarding7.this, EndOnboarding.class);
				finish();
				startActivity(i);
		      break;

		    
		    }
			return false;
		    } 
}

