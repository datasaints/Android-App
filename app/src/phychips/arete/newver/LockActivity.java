package phychips.arete.newver;

import com.makeramen.segmented.SegmentedRadioGroup;
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
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

public class LockActivity extends Activity implements OnCheckedChangeListener, iRcpEvent
{
    private Button back;
    private Button done;

    private SegmentedRadioGroup segment_TagMemory;
    private SegmentedRadioGroup segment_Action;

    private TextView lock_targetTag, lock_accessPass;

    private int seed = 0;
    private int targetMemory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_lock);

	segment_TagMemory = (SegmentedRadioGroup) findViewById(R.id.seg_group_three);
	segment_TagMemory.setOnCheckedChangeListener(this);
	segment_Action = (SegmentedRadioGroup) findViewById(R.id.seg_group_four);
	segment_Action.setOnCheckedChangeListener(this);

	lock_targetTag = (TextView) findViewById(R.id.lock_targetTag);
	lock_targetTag.setText(TagAccessActivity.nowTag);

	lock_accessPass = (TextView) findViewById(R.id.lock_accessPass);

	back = (Button) findViewById(R.id.lock_navigation_back_button);
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

	done = (Button) findViewById(R.id.lock_done_btn);
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
			String epc = lock_targetTag.getText().toString();
			RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2, 1);
			tag.password = Long.parseLong(lock_accessPass.getText()
				.toString(), 16);

			tag.epc = RcpLib.convertStringToByteArray(epc);

			int lock = 0;			
			switch(targetMemory)
			{
			    case 0:
				lock = (seed << 8) | (3 << 18);
				break;
			    case 1:
				lock = (seed << 6) | (3 << 16);
				break;
			    case 2:
				lock = (seed << 4) | (3 << 14);
				break;
			    case 3:
				lock = (seed << 2) | (3 << 12);
				break;
			    case 4:
				lock = (seed << 0) | (3 << 10);
				break;
			}
			
			tag.lock_mask = lock;

			RcpApi.lockTagMemory(tag);
		    }
		    catch (Exception e)
		    {

		    }
		}
		else
		{
		    Toast.makeText(LockActivity.this, "lock Tag successs",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});
    }

    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
	if (group == segment_TagMemory)
	{
	    switch(checkedId)
	    {
	    case R.id.segment_kill:
		targetMemory = 0;
		break;
	    case R.id.segment_acs:
		targetMemory = 1;
		break;
	    case R.id.segment_epc:
		targetMemory = 2;
		break;
	    case R.id.segment_tid:
		targetMemory = 3;
		break;
	    case R.id.segment_user:
		targetMemory = 4;
		break;
	    }   
	    
	    /*
	    if (checkedId == R.id.segment_kill)
	    {
		lockdata = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_acs)
	    {
		lockdata = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_epc)
	    {
		lockdata = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_tid)
	    {
		lockdata = (action << 8) | (3 << 18);
	    }
	    else if (checkedId == R.id.segment_user)
	    {
		lockdata = (action << 8) | (3 << 18);
	    }
	    */
	    
	}
	else if (group == segment_Action)
	{
	    switch(checkedId)
	    {
	    case R.id.segment_unlock:
		seed = 0;
		break;
	    case R.id.segment_punlock:
		seed = 1;
		break;
	    case R.id.segment_lock:
		seed = 2;
		break;
	    case R.id.segment_plock:
		seed = 3;
		break;		
	    }
	    /*
	    if (checkedId == R.id.segment_unlock)
	    {
		action = 0;
	    }
	    else if (checkedId == R.id.segment_punlock)
	    {
		action = 1;
	    }
	    else if (checkedId == R.id.segment_lock)
	    {
		action = 2;
	    }
	    else if (checkedId == R.id.segment_plock)
	    {
		action = 3;
	    }
	    */
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
		Toast.makeText(LockActivity.this, "Success",
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	// TODO Auto-generated method stub
	//System.out.println("onFailureReceived");	
	runOnUiThread(new Runnable() 
	{
	    public void run()
	    {	
		Toast.makeText(LockActivity.this, "Error: Error Code = " + data[0],
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
