/**********************************************************************************************
 * ----------       APPLICATIONLOGIC-CLASS (LM3)- WRITTEN BY: LEONIE SCHIBURR       -----------
 *********************************************************************************************/

/**
 * The ApplicationLogic Class provides the program logic for the learn mode.
 * It initializes the Gui with the data from the xml and database, like question and level, and
 * it reacts to the user inputs and validates its answer and executes the logic for right or wrong
 * answers.
 * 
 * Methods and Variables are commented in the Code.
 * 
 *  */

package de.bg.fhdw.bfwi413a.karthago.activities.lm3_g;

import java.util.ArrayList;
import java.util.Date;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

public class ApplicationLogic {
	
	private Gui mGui;
	private Data mData;
	Context mContext;
	String questionText;
	String textForLevel;
	ArrayList<String> mCorrectAnswers;
	String user;
    String cardfile;
	
	public ApplicationLogic(Gui gui, Data data, Context context) {
		mGui = gui;
		mData = data;
		mContext = context;
		initDataToGui();
	}
	
	private void initDataToGui() {
		//Inits Data from XML to TextView and Button
	    textForLevel = "Level: " + mData.getDbhandler().getCurrentLevelForQuestionId(mData.getQuestionID());
	    mGui.setLeveltext(textForLevel);
	    questionText = mData.getQuestionText();
	    mGui.setQuestion(questionText);
		mCorrectAnswers = mData.getCorrectAnswers();
		user = mData.getSession().getUserDetails();
		cardfile = mData.getSession().getCardfileID();
		
	}


	public void onButtonClicked() {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
		builder.setTitle("Lösung");
		TextView corAns = new TextView(mContext);
		for(int i = 0; i < mCorrectAnswers.size(); i++){
			corAns.append(mCorrectAnswers.get(i).toString());
		}
		corAns.setGravity(Gravity.CENTER_HORIZONTAL);
		builder.setView(corAns);
		// Add the buttons
		builder.setPositiveButton("Habs gewusst!", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	boolean rightORwrong = true;
	        	String event_name = "correct";
	        	long tstamp = new Date().getTime();
				mData.getDbhandler().IncreaseOrDecreaseLevelAndSetNewTimestamp(rightORwrong, mData.getQuestionID(), tstamp);
				mData.getDbhandler().insertEvent(event_name, tstamp, user, cardfile);
				dialog.cancel();
				mData.getmActivity().finish();
				mData.getApplicationLogicSelection().startSingleQuestion(mContext);
	           }
	       });
		builder.setNegativeButton("Falsche Antwort!", new DialogInterface.OnClickListener() {
	           public void onClick(DialogInterface dialog, int id) {
	        	   boolean rightORwrong = false;
	        	   String event_name = "incorrect";
		        	long tstamp = new Date().getTime();
					mData.getDbhandler().IncreaseOrDecreaseLevelAndSetNewTimestamp(rightORwrong, mData.getQuestionID(), tstamp);
					mData.getDbhandler().insertEvent(event_name, tstamp, user, cardfile);
					dialog.cancel();
					mData.getmActivity().finish();
					mData.getApplicationLogicSelection().startSingleQuestion(mContext);
	           }
	       });
		
		// Create the AlertDialog
		builder.show();
		
	}

	
}
