package phychips.arete.newver;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpLib;
import com.phychips.rcp.RcpTypeCTag;
import com.phychips.rcp.iRcpEvent;

import phychips.arete.newver.R;

import android.os.Bundle;
import android.app.Activity;

import android.view.View;
import android.view.View.OnClickListener;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KillActivity extends Activity implements iRcpEvent
{
    private Button back, done;
    private TextView kill_targetTag, kill_password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_kill);

	kill_targetTag = (TextView) findViewById(R.id.kill_targetTag);
	kill_targetTag.setText(TagAccessActivity.nowTag);

	kill_password = (TextView) findViewById(R.id.kill_pass);

	back = (Button) findViewById(R.id.kill_navigation_back_button);
	back.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		moveTaskToBack(false);
		finish();
	    }
	});

	done = (Button) findViewById(R.id.kill_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (RcpApi.isOpen)
		{
		    try
		    {
			String epc = kill_targetTag.getText().toString();
			RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2, 1);

			tag.password = Long.parseLong(kill_password.getText()
				.toString(), 16);

			tag.epc = RcpLib.convertStringToByteArray(epc);

			tag.recom = 0;

			RcpApi.killTag(tag);
		    }
		    catch (Exception e)
		    {
			Toast.makeText(KillActivity.this, e.toString(),
				Toast.LENGTH_SHORT).show();
		    }
		}
		else
		{
		    Toast.makeText(KillActivity.this, "Kill Tag successs",
			    Toast.LENGTH_SHORT).show();
		}
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
		Toast.makeText(KillActivity.this, "Success",
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
		Toast.makeText(KillActivity.this, "Error: Error Code = " + data[0],
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
