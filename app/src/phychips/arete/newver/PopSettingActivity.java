package phychips.arete.newver;

import java.util.ArrayList;
import phychips.arete.newver.R;

import com.arete.custom.IOnHandlerMessage;
import com.arete.custom.PopSettingAdapter;
import com.arete.custom.WeakRefHandler;
import com.arete.custom.customCell;
import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.RcpFhLbtParam;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.iRcpEvent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Toast;

import android.widget.ToggleButton;

public class PopSettingActivity extends Activity implements iRcpEvent,
	IOnHandlerMessage
{
    private Button back;

    private ListView listViewRegulatoryParam;
    private ListView listViewReadingParam;

    private PopSettingAdapter adapterRegulatoryParam;
    private PopSettingAdapter adapterReadingParam;

    private ArrayList<customCell> listRegulatoryParam;
    private ArrayList<customCell> listReadingParam;

    private String beepState;

    private int on_time, off_time, sense_time, fh_enable, lbt_enable,
	    cw_enable;
    static int tx_minpower, tx_maxpower, powerLevel;

    private customCell OutputPower, OnOffTime, StopConditions;
    private ToggleButton beepSet;

    private Handler m_Handler;

    static RcpFhLbtParam param;

    private int timerError = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_pop_setting);

	back = (Button) findViewById(R.id.popsetting_navigation_back_button);
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

	RcpApi.setRcpEvent(this);

	param = new RcpFhLbtParam();

	listViewRegulatoryParam = (ListView) findViewById(R.id.popsetting_listview1);
	listViewReadingParam = (ListView) findViewById(R.id.popsetting_listview2);

	listRegulatoryParam = new ArrayList<customCell>();
	listReadingParam = new ArrayList<customCell>();

	OutputPower = new customCell("Ouput Power", "detail");
	OnOffTime = new customCell("On/Off Time(ms)", "detail");
	StopConditions = new customCell("Stop Conditions", MainActivity.max_tag
		+ "/" + MainActivity.max_time + "/" + MainActivity.repeat_cycle);

	beepSet = (ToggleButton) findViewById(R.id.popsetting_toggle);
	beepSet.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{

	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		// TODO Auto-generated method stub
		if (isChecked)
		{
		    try
		    {
			RcpApi.setBeep(true);
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
		else
		{
		    try
		    {
			RcpApi.setBeep(false);
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    }
	});
	
	ToggleButton rssiSet = (ToggleButton) findViewById(R.id.appsetting_rssi);
	SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(PopSettingActivity.this);
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
			.getDefaultSharedPreferences(PopSettingActivity.this);		
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean("DISPLAY_RSSI", rssi);
		editor.commit();
	    }
	});

	adapterRegulatoryParam = new PopSettingAdapter(this,
		R.layout.popsettinglist, listRegulatoryParam);
	adapterReadingParam = new PopSettingAdapter(this,
		R.layout.popsettinglist, listReadingParam);

	listViewRegulatoryParam.setAdapter(adapterRegulatoryParam);
	listViewReadingParam.setAdapter(adapterReadingParam);

	listRegulatoryParam.add(OutputPower);
	listRegulatoryParam.add(OnOffTime);

	listReadingParam.add(StopConditions);

	adapterRegulatoryParam.notifyDataSetChanged();
	adapterReadingParam.notifyDataSetChanged();

	m_Handler = new WeakRefHandler(this);
	try
	{
	    RcpApi.getReaderInfo(0xB0);
	    timerError = 0;
	    m_Handler.sendEmptyMessageDelayed(-1, 1000);
	}
	catch (RcpException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    protected void onResume()
    {
	// TODO Auto-generated method stub
	super.onResume();
	try
	{
	    RcpApi.getReaderInfo(0xB0);
	}
	catch (RcpException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }

    @Override
    protected void onStart()
    {
	// TODO Auto-generated method stub
	super.onRestart();
	overridePendingTransition(R.anim.slide_out_right1,
		R.anim.slide_out_right2);
    }

    @Override
    protected void onDestroy()
    {
	super.onDestroy();
	m_Handler.removeMessages(-1);
    }

    @Override
    public void onTagReceived(int[] data)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onReaderInfoReceived(final int[] data)
    {
	try
	{
	    RcpApi.getCurrChannel();
	}
	catch (RcpException e)
	{
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// TODO Auto-generated method stub
	//String temp = RcpLib.int2str(data);
	// TODO Auto-generated method stub

	//if (temp.length() == 40)
	if (data.length == 20 && ((data[0]&0xff)  == 0xB0) )
	{
	    String temp = RcpLib.int2str(data);
	    
	    beepState = temp.substring(2, 4);

	    on_time = Integer.parseInt(temp.substring(6, 10), 16);

	    off_time = Integer.parseInt(temp.substring(10, 14), 16);

	    sense_time = Integer.parseInt(temp.substring(14, 18), 16);

	    //lbt_rf_level = Integer.parseInt(temp.substring(18, 22), 16);

	    fh_enable = Integer.parseInt(temp.substring(22, 24), 16);

	    lbt_enable = Integer.parseInt(temp.substring(24, 26), 16);

	    cw_enable = Integer.parseInt(temp.substring(26, 28), 16);

	    powerLevel = Integer.parseInt(temp.substring(28, 32), 16);

	    tx_minpower = Integer.parseInt(temp.substring(32, 36), 16);

	    tx_maxpower = Integer.parseInt(temp.substring(36, 40), 16);
    
	    
	    param.readtime = on_time;
	    param.idletime = off_time;
	    param.sensetime = sense_time;
	    param.powerlevel = powerLevel;
	    param.fhmode = fh_enable;
	    param.lbtmode = lbt_enable;
	    param.cwmode = cw_enable;

	    
	    //m_Handler.sendEmptyMessage(0);
        	runOnUiThread(new Runnable() 
        	{
        	    public void run()
        	    {	
        		listRegulatoryParam.clear();
        		listReadingParam.clear();

        		OutputPower = new customCell("Ouput Power", Integer
        			    .toString(powerLevel).substring(0, 2)
        			    + "."
        			    + Integer.toString(powerLevel).substring(2));
        		OnOffTime = new customCell("On/Off Time(ms)", Integer.toString(on_time)
        			    + "/" + Integer.toString(off_time));
        		StopConditions = new customCell("Stop Conditions", MainActivity.max_tag
        				+ "/" + MainActivity.max_time + "/" + MainActivity.repeat_cycle);
        		    
        		if (beepState.equals("01"))
        		{
        			beepSet.setChecked(true);
        		}
        		else
        		{
        			beepSet.setChecked(false);
        		}

        		listRegulatoryParam.add(OutputPower);
        		listRegulatoryParam.add(OnOffTime);
        		listReadingParam.add(StopConditions);
        		adapterRegulatoryParam.notifyDataSetChanged();
        		adapterReadingParam.notifyDataSetChanged();
        		
        	    }
        	});	    
	}
	else
	{

	    //m_Handler.sendEmptyMessage(1);	    
        	runOnUiThread(new Runnable() 
        	{
        	    public void run()
        	    {	
        		    Toast.makeText(PopSettingActivity.this, "Update needed",
        			    Toast.LENGTH_SHORT).show();
        	    }
        	});

	}

	// powerMin = (Integer.parseInt(temp.substring(34, 38), 16) % 100) / 10;
	// powerMax = (Integer.parseInt(temp.substring(36, 38), 16) % 100) / 10;

	// L
	// tx_off_time L

	// temp.substring(14, 16); // tx_sense_time M
	// temp.substring(16, 18); // tx_sense_time L
	// temp.substring(18, 20); // rf_level M
	// temp.substring(20, 22); // rf_level L
	// temp.substring(22, 24); // fh_en
	// temp.substring(24, 26); // lbt_en
	// temp.substring(26, 28); // cw_en
    }

    @Override
    public void onRegionReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onSelectParamReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onQueryParamReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onChannelReceived(int[] data)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onFhLbtReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTxPowerLevelReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTagMemoryReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onHoppingTableReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onModulationParamReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onAnticolParamReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTempReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onRssiReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onRegistryItemReceived(int[] data)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onResetReceived(int[] data)
    {

    }

    @Override
    public void onSuccessReceived(int[] data)
    {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		Toast.makeText(PopSettingActivity.this, "Success",
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		Toast.makeText(PopSettingActivity.this,
			"Error: Error Code = " + data[0], Toast.LENGTH_LONG)
			.show();
	    }
	});
    }

    @Override
    public void onAuthenticat(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onBeepStateReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTestFerPacketReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void handlerMessage(Message msg)
    {
	// TODO Auto-generated method stub
	switch (msg.what)
	{
	
//	case 0:
//
//	    param.readtime = on_time;
//	    param.idletime = off_time;
//	    param.sensetime = sense_time;
//	    param.powerlevel = powerLevel;
//	    param.fhmode = fh_enable;
//	    param.lbtmode = lbt_enable;
//	    param.cwmode = cw_enable;
//
//	    listRegulatoryParam.clear();
//	    listReadingParam.clear();
//
//	    OutputPower = new customCell("Ouput Power", Integer.toString(
//		    powerLevel).substring(0, 2)
//		    + "." + Integer.toString(powerLevel).substring(2));
//	    OnOffTime = new customCell("On/Off Time(ms)",
//		    Integer.toString(on_time) + "/"
//			    + Integer.toString(off_time));
//	    StopConditions = new customCell("Stop Conditions",
//		    MainActivity.max_tag + "/" + MainActivity.max_time + "/"
//			    + MainActivity.repeat_cycle);
//
//	    if (beepState.equals("01"))
//	    {
//		beepSet.setChecked(true);
//	    }
//	    else
//	    {
//		beepSet.setChecked(false);
//	    }
//
//	    listRegulatoryParam.add(OutputPower);
//	    listRegulatoryParam.add(OnOffTime);
//	    listReadingParam.add(StopConditions);
//	    adapterRegulatoryParam.notifyDataSetChanged();
//	    adapterReadingParam.notifyDataSetChanged();
//	    break;
//	case 1:
//
//	    Toast.makeText(PopSettingActivity.this, "Need firm ware Update",
//		    Toast.LENGTH_SHORT).show();
//	    break;
//
//	case 2:
//
//	    break;
	case -1:
	    // System.out.println("timer - 1");
	    if (this.listRegulatoryParam.size() == 0 && timerError < 5)
	    {
		try
		{
		    RcpApi.getReaderInfo(0xB0);
		    m_Handler.sendEmptyMessageDelayed(-1, 1000);
		}
		catch (RcpException e1)
		{
		    // TODO Auto-generated catch block
		    e1.printStackTrace();
		}

		timerError++;
	    }

	}

    }

    @Override
    public void onAdcReceived(int[] dest)
    {
	// TODO Auto-generated method stub

    }

    @Override
    public void onTagMemoryLongReceived(int[] dest)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTagWithRssiReceived(int[] data, int rssi)
    {
	// TODO Auto-generated method stub
	
    }

}
