package org.stjude.www.dsassetmanagement.arete;


import phychips.arete.newver.R;

import com.makeramen.segmented.SegmentedRadioGroup;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
//import android.os.Handler;
//import android.os.Message;
//import com.arete.custom.IOnHandlerMessage;
//import com.arete.custom.WeakRefHandler;
import com.phychips.rcp.*;

@SuppressLint("DefaultLocale")
public class ReadWriteActivity extends Activity implements
	OnCheckedChangeListener, iRcpEvent// , IOnHandlerMessage
{
    private Button back;
    private Button done;
    private SegmentedRadioGroup segment_ReadWrite;
    private SegmentedRadioGroup segment_option;

    private TextView tvTargetTag;
    private TextView tvLength;
    private TextView tvPassword;
    private TextView tvAddress;
    private TextView tvData;
    private TextView tvTitle;
    // private TextView tvDataText;

    private boolean state = true;

    private int option = 0;

    private String dataText;

    private StringBuffer dataBuffer;

    // private Handler m_Handler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_read);

	RcpApi.setRcpEvent(this);

	// m_Handler = new WeakRefHandler(this);

	segment_ReadWrite = (SegmentedRadioGroup) findViewById(R.id.seg_group_one);
	segment_ReadWrite.setOnCheckedChangeListener(this);
	segment_option = (SegmentedRadioGroup) findViewById(R.id.seg_group_two);
	segment_option.setOnCheckedChangeListener(this);

	tvTargetTag = (TextView) findViewById(R.id.read_targettag);
	tvTargetTag.setText(TagAccessActivity.nowTag);

	tvLength = (TextView) findViewById(R.id.read_length);

	tvPassword = (TextView) findViewById(R.id.read_accesspass);

	tvAddress = (TextView) findViewById(R.id.read_startAddress);

	tvData = (TextView) findViewById(R.id.read_data);

	tvTitle = (TextView) findViewById(R.id.titlebar_text);

	back = (Button) findViewById(R.id.read_navigation_back_button);

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

	done = (Button) findViewById(R.id.read_done_btn);
	done.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (state)
		{
		    try
		    {
			String epc = tvTargetTag.getText().toString();

			int data_length = Integer.parseInt(tvLength.getText()
				.toString());

			RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2,
				data_length);

			tag.password = Long.parseLong(tvPassword.getText()
				.toString(), 16);

			tag.epc = RcpLib.convertStringToByteArray(epc);

			tag.start_address = Integer.parseInt(tvAddress
				.getText().toString(), 16);
			tag.memory_bank = option;

			if (data_length < 64)
			{
			    RcpApi.readFromTagMemory(tag);
			}
			else
			{
			    dataBuffer = new StringBuffer();
			    RcpApi.readLongTag(tag);
			}
		    }
		    catch (Exception e)
		    {
			tvData.setText(e.toString());
			Toast.makeText(ReadWriteActivity.this, e.toString(),
				Toast.LENGTH_SHORT).show();
		    }
		}
		else
		{
		    try
		    {
			String epc = tvTargetTag.getText().toString();
			String data_temp = tvData.getText().toString();

			int data_length;

			if (tvLength.getText().toString().equals("0"))
			{
			    data_length = data_temp.length() / 4; // word length
								  // = string
								  // length / 2
								  // / 2
			}
			else
			{
			    data_length = Integer.parseInt(tvLength.getText()
				    .toString());
			}

			RcpTypeCTag tag = new RcpTypeCTag(epc.length() / 2,
				data_length);
			System.out.println("[1] data_length = " + data_length);

			tag.password = Long.parseLong(tvPassword.getText()
				.toString(), 16);
			System.out.println("[2] tvPassword = "
				+ tvPassword.getText().toString());
			tag.epc = RcpLib.convertStringToByteArray(epc);
			System.out
				.println("[3]  RcpLib.convertStringToByteArray(epc)"
					+ RcpLib.convertStringToByteArray(epc));
			tag.start_address = Integer.parseInt(tvAddress
				.getText().toString(), 16);
			System.out.println("[4]");
			tag.data = RcpLib.convertStringToByteArray(data_temp);
			System.out.println("[5] data_temp = ");
			tag.memory_bank = option;
			System.out.println("[6]");
			RcpApi.writeToTagMemory(tag);
			System.out.println("[7]");
		    }
		    catch (Exception e)
		    {
			// data.setText(e.toString());
			Toast.makeText(ReadWriteActivity.this, e.toString(),
				Toast.LENGTH_SHORT).show();
		    }
		}
	    }
	});
    }

    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
	if (group == segment_ReadWrite)
	{
	    if (checkedId == R.id.segment_read)
	    {
		state = true;
		tvTitle.setText("Read");
	    }
	    else if (checkedId == R.id.segment_write)
	    {
		state = false;
		tvTitle.setText("Write");
	    }
	}
	else if (group == segment_option)
	{
	    if (checkedId == R.id.segment_rfu)
	    {
		option = 0x00;
	    }
	    else if (checkedId == R.id.segment_epc)
	    {
		option = 0x01;
	    }
	    else if (checkedId == R.id.segment_tid)
	    {
		option = 0x02;
	    }
	    else if (checkedId == R.id.segment_user)
	    {
		option = 0x03;
	    }
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
    public void onTagMemoryReceived(final int[] data)
    {
	// TODO Auto-generated method stub
	dataText = RcpLib.int2str(data);
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		tvData.setText(dataText);
		// tvDataText.setText("Data(HEX) " + data.length + " byte");
		// System.out.println(tvData.length() / 2);
	    }
	});
	// m_Handler.sendEmptyMessage(0);
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
		Toast.makeText(ReadWriteActivity.this, "Success",
			Toast.LENGTH_LONG).show();
	    }
	});
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	// TODO Auto-generated method stub
	System.out.println("onFailureReceived");

	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		Toast.makeText(ReadWriteActivity.this,
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

    // @Override
    // public void handlerMessage(Message msg)
    // {
    // // TODO Auto-generated method stub
    // switch (msg.what)
    // {
    // case 0:
    // tvData.setText(data_return);
    // tvLegth.setText("Data(HEX) " + tvData.length() / 2 + " byte");
    // System.out.println(tvData.length() / 2);
    // break;
    // }
    // }

    @Override
    public void onAdcReceived(int[] dest)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onTagMemoryLongReceived(int[] dest)
    {
	// TODO Auto-generated method stub
	if (dest.length > 1)
	    dataBuffer.append(RcpLib.int2str(dest).substring(6));

	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		tvData.setText( editAddressString(dataBuffer));
	    }
	});
    }
    
    public String editAddressString(StringBuffer dataString){
	StringBuffer sb = new StringBuffer();

	for(int i=0; i<dataString.length(); i++)
	{
	    if(i%16 == 0 ){
		sb.append("0x"+Integer.toHexString(i/4+0x100000).toUpperCase().substring(2) + "   ");      
	    }
	    sb.append(dataString.charAt(i));  
	    if(i%2 == 1)
		sb.append(" ");
            if(i%16 == 15 ){
		sb.append("\n");
		                  
            }
	}
	return sb.toString();
    }

    @Override
    public void onTagWithRssiReceived(int[] data, int rssi)
    {
	// TODO Auto-generated method stub
	
    }

}
