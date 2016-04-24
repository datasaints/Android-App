package phychips.arete.newver;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.RcpFhLbtParam;
import com.phychips.rcp.iRcpEvent;

import phychips.arete.newver.R;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.TextView;
import android.widget.Toast;

public class OnOffTimeActivity extends Activity implements iRcpEvent
{
    Button back, done;
    //TextView TextView_OnTime, TextView_OffTime;
    EditText edit_text1, edit_text2;
    RcpFhLbtParam param;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_on_off_time);

	//TextView_OnTime = (TextView) findViewById(R.id.onofftimetext);
	//TextView_OffTime = (TextView) findViewById(R.id.onofftimetext2);

	String value = getIntent().getStringExtra("value");

	//TextView_OnTime.setText(value.substring(0, value.indexOf("/")));
	//TextView_OffTime.setText(value.substring(value.indexOf("/") + 1));

	edit_text1 = (EditText) findViewById(R.id.onofftimetext);
	edit_text2 = (EditText) findViewById(R.id.onofftimetext2);
	edit_text1.setText(value.substring(0, value.indexOf("/")));
	edit_text2.setText(value.substring(value.indexOf("/") + 1));
	

	this.param = PopSettingActivity.param;

	back = (Button) findViewById(R.id.onofftime_navigation_back_button);
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

	done = (Button) findViewById(R.id.onoff_done_btn);
	done.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {

		param.readtime = Integer.parseInt(edit_text1.getText()
			.toString());
		param.idletime = Integer.parseInt(edit_text2.getText()
			.toString());
		try
		{
		    RcpApi.setFhLbtParam(param);
		}
		catch (RcpException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		Toast.makeText(OnOffTimeActivity.this, "Success",
			Toast.LENGTH_SHORT).show();
	    }
	});
    }

    @Override
    public void onTagReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onReaderInfoReceived(int[] data)
    {
	// TODO Auto-generated method stub
	
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
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onSuccessReceived(int[] data)
    {
	// TODO Auto-generated method stub
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		Toast.makeText(OnOffTimeActivity.this, "Success",
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
		Toast.makeText(OnOffTimeActivity.this, "Error: Error Code = " + data[0],
			Toast.LENGTH_LONG).show();
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
    public void onAdcReceived(int[] dest)
    {
	// TODO Auto-generated method stub
	
    }

    @Override
    public void onTestFerPacketReceived(int[] dest)
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
