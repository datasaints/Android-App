package phychips.arete.newver;

import java.util.ArrayList;

import phychips.arete.newver.R;

import com.phychips.rcp.RcpApi;
import com.phychips.rcp.RcpException;
import com.phychips.rcp.iRcpEvent;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class OutPutPowerActivity extends Activity implements iRcpEvent
{
    private Button back;
    private Button done;

    private ListView outList;

    private ArrayAdapter<String> adapter;

    private ArrayList<String> array_list;

    private int max = 0, min = 0;

    private int powerlevel = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_out_put_power);

	array_list = new ArrayList<String>();

	min = PopSettingActivity.tx_minpower;
	max = PopSettingActivity.tx_maxpower;

	powerlevel = PopSettingActivity.powerLevel;


	for (int i = min; i <= max; i += 5)
	{
	    array_list.add(Integer.toString(i).substring(0,2)+"."+Integer.toString(i).substring(2));
	}

	adapter = new ArrayAdapter<String>(this, R.layout.simple_list_item_checked,array_list);

	back = (Button) findViewById(R.id.outputpower_navigation_back_button);
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

	outList = (ListView) findViewById(R.id.outpout_listview1);
	outList.setAdapter(adapter);
	outList.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	outList.setOnItemClickListener(new OnItemClickListener()
	{

	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		powerlevel = 200 + (arg2 * 5);
		//RcpApi.setOutputPowerLevel((byte) ((byte) 0xC8 + (arg2 * 5)));
		//RcpApi.setOutputPowerLevel((byte) ((200 + (arg2 * 5)) & 0xff) );
	    }
	});
	
	outList.setItemChecked((powerlevel/5)-40, true);
	adapter.notifyDataSetChanged();
	
	done = (Button) findViewById(R.id.power_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub		
		try
		{
		    RcpApi.setOutputPowerLevel((byte) (powerlevel & 0xff) );
		}
		catch (RcpException e)
		{
		    // TODO Auto-generated catch block
		    e.printStackTrace();
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
		Toast.makeText(OutPutPowerActivity.this, "Success",
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
		Toast.makeText(OutPutPowerActivity.this, "Error: Error Code = " + data[0],
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
