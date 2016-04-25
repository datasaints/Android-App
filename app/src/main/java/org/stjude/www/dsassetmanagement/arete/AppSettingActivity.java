package org.stjude.www.dsassetmanagement.arete;



import org.stjude.www.dsassetmanagement.arete.R;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.CompoundButton;

import android.widget.ToggleButton;

import android.app.Activity;
import android.content.SharedPreferences;

public class AppSettingActivity extends Activity
{

    // private RadioGroup logGroup;//, messageGroup;
   

    // private Spinner spinner_encoding;
    // private ArrayList<String> spinnerEncodingArray;
    // private ArrayAdapter<String> spinnerEncodingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_app_setting);

	Button back = (Button) findViewById(R.id.app_setting_navigation_back);
	back.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		moveTaskToBack(false);
		finish();
	    }
	});
		
	ToggleButton rssiSet = (ToggleButton) findViewById(R.id.appsetting_rssi);
	SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(AppSettingActivity.this);
	rssiSet.setChecked(prefs.getBoolean("DISPLAY_RSSI", false));
	
	rssiSet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() 
	{
	    boolean rssi = false;
	    
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		if (isChecked)
		{
		    rssi = true;
		}
		else
		{
		    rssi = false;
		}
		
		System.out.println("rssi = " + rssi);
		
		SharedPreferences prefs = PreferenceManager
			.getDefaultSharedPreferences(AppSettingActivity.this);		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("DISPLAY_RSSI", rssi);
		editor.commit();
	    }
	});

	// logGroup = (RadioGroup) findViewById(R.id.LogGroup);
	// logGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
	// {
	//
	// @Override
	// public void onCheckedChanged(RadioGroup group, int checkedId)
	// {
	// // TODO Auto-generated method stub
	// if (checkedId == group.getChildAt(0).getId())
	// {
	// MainActivity.saveLog = true;
	//
	// }
	// else
	// {
	// MainActivity.saveLog = false;
	// }
	// }
	// });

	// if (MainActivity.saveLog)
	// {
	// ((RadioButton) logGroup.getChildAt(0)).setChecked(true);
	// }
	// else
	// {
	// ((RadioButton) logGroup.getChildAt(1)).setChecked(true);
	// }

	// messageGroup = (RadioGroup) findViewById(R.id.messageGroup);
	// messageGroup.setOnCheckedChangeListener(new OnCheckedChangeListener()
	// {
	// @Override
	// public void onCheckedChanged(RadioGroup group, int checkedId)
	// {
	// // TODO Auto-generated method stub
	// if (checkedId == group.getChildAt(0).getId())
	// {
	// MainActivity.flag = true;
	// }
	// else
	// {
	// MainActivity.flag = false;
	// }
	// }
	// });
	//
	// if (MainActivity.flag)
	// {
	// ((RadioButton) messageGroup.getChildAt(0)).setChecked(true);
	// }
	//
	// else
	// {
	// ((RadioButton) messageGroup.getChildAt(1)).setChecked(true);
	// }

	// spinner_encoding = (Spinner) findViewById(R.id.encoding);
	//
	// spinner_encoding.setOnItemSelectedListener(new
	// OnItemSelectedListener()
	// {
	// @Override
	// public void onItemSelected(AdapterView<?> arg0, View arg1,
	// int arg2, long arg3)
	// {
	// // TODO Auto-generated method stub
	// MainActivity.encoding_type = arg2;
	// }
	//
	// @Override
	// public void onNothingSelected(AdapterView<?> arg0)
	// {
	// // TODO Auto-generated method stub
	// }
	// });
	//
	// spinnerEncodingArray = new ArrayList<String>();
	// spinnerEncodingArray.add("Basic");
	// spinnerEncodingArray.add("ASC");
	//
	// spinnerEncodingAdapter = new ArrayAdapter<String>(this,
	// android.R.layout.simple_dropdown_item_1line,
	// spinnerEncodingArray);
	// spinner_encoding.setAdapter(spinnerEncodingAdapter);
	//
	// spinner_encoding.setSelection(MainActivity.encoding_type);
    }

    @Override
    protected void onStart()
    {
	// TODO Auto-generated method stub
	super.onRestart();
	overridePendingTransition(R.anim.slide_out_right1,
		R.anim.slide_out_right2);
    }
}
