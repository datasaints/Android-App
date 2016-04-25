package org.stjude.www.dsassetmanagement.arete;

import com.phychips.rcp.iRcpEvent;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import phychips.arete.newver.R;

public class StopCondisionsActivity extends Activity implements iRcpEvent
{
    private Button back, done;

    private EditText text1, text2, text3;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_stop_condisions);

	text1 = (EditText) findViewById(R.id.stop_editText1);
	text2 = (EditText) findViewById(R.id.stop_editText2);
	text3 = (EditText) findViewById(R.id.stop_editText3);

	back = (Button) findViewById(R.id.stopconditions_navigation_back_button);
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

	done = (Button) findViewById(R.id.stop_done_btn);
	done.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {

		// TODO Auto-generated method stub
		if (isNumber(text1.getText().toString()))
		{

		    MainActivity.max_tag = Integer.parseInt(text1.getText()
			    .toString());

		    if (isNumber(text2.getText().toString()))
		    {
			MainActivity.max_time = Integer.parseInt(text2
				.getText().toString());
			if (isNumber(text3.getText().toString()))
			{
			    MainActivity.repeat_cycle = Integer.parseInt(text3
				    .getText().toString());
			    Toast.makeText(StopCondisionsActivity.this,
				    "success", Toast.LENGTH_SHORT).show();
			}
			else
			{
			    Toast.makeText(StopCondisionsActivity.this,
				    "Error: Only integers allowed", Toast.LENGTH_SHORT).show();
			}
		    }
		    else
		    {
			Toast.makeText(StopCondisionsActivity.this, "Error: Only integers allowed",
				Toast.LENGTH_SHORT).show();
		    }

		}
		else
		{
		    Toast.makeText(StopCondisionsActivity.this, "Error: Only integers allowed",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    public static boolean isNumber(String str)
    {
	boolean check = true;
	
	if (!str.isEmpty())
	{
	    for (int i = 0; i < str.length(); i++)
	    {
		if (!Character.isDigit(str.charAt(i)))
		{
		    check = false;
		    break;
		}
	    }
	    return check;
	}
	else
	{
	    check = false;
	    return check;
	}
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
		Toast.makeText(StopCondisionsActivity.this, "Success",
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
		Toast.makeText(StopCondisionsActivity.this, "Error: Error Code = " + data[0],
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
