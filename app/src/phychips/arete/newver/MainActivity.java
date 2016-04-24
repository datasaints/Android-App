package phychips.arete.newver;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import com.arete.custom.*;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import com.phychips.rcp.*;

public class MainActivity extends Activity implements iRcpEvent
{
    public static final String KEY_ENCODING = "my_encoding";
    public static final String KEY_SAVELOG = "my_saveLog";

    public static int encoding_type = 0;
    private static boolean displayRssi = false;
    public static int max_tag = 0;
    public static int max_time = 0;
    public static int repeat_cycle = 0;

    private String test_msg;
    private int battery;
    private ToggleButton open;
    private Button stopAutoRead, startAutoRead, clearScreen, option;
    private ListView epclist;
    private TextView tvTagCount, tvHeadsetStatus, tvBattery;
    private ArrayList<customCell> tagArray = new ArrayList<customCell>();
    private CustomListAdapter tagAdapter;
    private boolean headsetConnected = false;
    static private boolean m_bOnCreate = false;

    IntentBroadcastReceiver m_IntentReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	if (!m_bOnCreate)
	{
	    m_bOnCreate = true;
	    startActivity(new Intent(this, SplashActivity.class));
	    usrRegisterIntent();
	}

	epclist = (ListView) findViewById(R.id.tag_list);
	tvTagCount = (TextView) findViewById(R.id.name);
	tvHeadsetStatus = (TextView) findViewById(R.id.aboutvalue);
	tvBattery = (TextView) findViewById(R.id.textView3);
	tagAdapter = new CustomListAdapter(this, R.layout.customlistview,
		tagArray);
	epclist.setAdapter(tagAdapter);
	epclist.setOnItemClickListener(new OnItemClickListener()
	{
	    @Override
	    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		    long arg3)
	    {
		try
		{
		    RcpApi.stopReadTags();
		}
		catch (RcpException e)
		{
		    e.printStackTrace();
		}

		Intent intent = new Intent(getBaseContext(),
			TagAccessActivity.class);
		intent.putExtra("tagitem",
			((customCell) (tagArray.toArray()[arg2])).getName());
		startActivity(intent);
	    }
	});

	// Open button
	open = (ToggleButton) findViewById(R.id.toggle_on);
	open.setOnCheckedChangeListener(new OnCheckedChangeListener()
	{
	    @Override
	    public void onCheckedChanged(CompoundButton buttonView,
		    boolean isChecked)
	    {
		if (isChecked)
		{
		    try
		    {
			RcpApi.open();
			setVolumeMax();
			// m_Handler.sendEmptyMessage(8);
		    }
		    catch (Exception e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
		else
		{
		    try
		    {
			RcpApi.close();
			if (headsetConnected)
			{
			    tvHeadsetStatus.setText("Plugged");
			}
			else
			{
			    tvHeadsetStatus.setText("Unplugged");
			}
		    }
		    catch (Exception e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    }
	});

	// On(toggle button) Clickalbe set
	if (headsetConnected)
	    open.setClickable(true);
	else
	    open.setClickable(false);

	// Read button
	startAutoRead = (Button) findViewById(R.id.btn_read);
	startAutoRead.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if (RcpApi.isOpen)
		{
		    try
		    {
			if (displayRssi)
			{
			    RcpApi.startReadTagsWithRssi(max_tag, max_time,
				    repeat_cycle);
			}
			else
			{
			    RcpApi.startReadTags(max_tag, max_time,
				    repeat_cycle);
			}
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
		else
		{
		    Toast.makeText(MainActivity.this, "Reader  not Opened",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});

	// Stop button
	stopAutoRead = (Button) findViewById(R.id.btn_stop);
	stopAutoRead.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		if (RcpApi.isOpen)
		{
		    try
		    {
			RcpApi.stopReadTags();
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    // if (saveLog)
		    // {
		    // startCapture();
		    // }
		}
		else
		{
		    Toast.makeText(MainActivity.this,
			    "Failure to initialize audio port",
			    Toast.LENGTH_SHORT).show();
		}
	    }
	});

	// Clear button
	clearScreen = (Button) findViewById(R.id.btn_clear);
	clearScreen.setOnClickListener(new OnClickListener()
	{
	    @Override
	    public void onClick(View v)
	    {
		ListClear();
	    }
	});

	option = (Button) findViewById(R.id.option);
	option.setOnClickListener(new OnClickListener()
	{

	    @Override
	    public void onClick(View v)
	    {
		// TODO Auto-generated method stub
		if (RcpApi.isOpen)
		{
		    try
		    {
			RcpApi.stopReadTags();
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }

		    Intent intent = new Intent(MainActivity.this,
			    OptionActivity.class);
		    startActivity(intent);
		}
	    }
	});

	TelephonyManager mTelMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
	mTelMgr.listen(new PhoneStateListener()
	{
	    public void onCallStateChanged(int state, String incomingNumber)
	    {
		switch (state)
		{
		case TelephonyManager.CALL_STATE_IDLE:

		    break;
		case TelephonyManager.CALL_STATE_OFFHOOK:
		case TelephonyManager.CALL_STATE_RINGING:
		    try
		    {
			RcpApi.close();
			open.setChecked(false);
			System.out.println("calling stop............");
		    }
		    catch (Exception e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		    break;
		}
	    }
	}, PhoneStateListener.LISTEN_CALL_STATE);

    }

    private void usrRegisterIntent()
    {
	usrUnRegisterIntent();

	m_IntentReceiver = new IntentBroadcastReceiver();

	IntentFilter filter = new IntentFilter();
	filter.addAction(Intent.ACTION_SCREEN_OFF);
	filter.addAction(Intent.ACTION_HEADSET_PLUG);
	// registerReceiver(m_IntentReceiver,
	// new IntentFilter(Intent.ACTION_HEADSET_PLUG));
	registerReceiver(m_IntentReceiver, filter);
    }

    private void usrUnRegisterIntent()
    {
	if (m_IntentReceiver != null)
	{
	    unregisterReceiver(m_IntentReceiver);
	    m_IntentReceiver = null;
	}
    }

    // -------------------------------------------------------------end
    // MainActivity onCreate

    // Headset Connection
    public class IntentBroadcastReceiver extends BroadcastReceiver
    {
	@Override
	public void onReceive(Context arg0, Intent intent)
	{
	    System.out.println(intent.toString());

	    if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF))
	    {
		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		PowerManager.WakeLock wl = pm.newWakeLock(
			PowerManager.SCREEN_DIM_WAKE_LOCK
				| PowerManager.ON_AFTER_RELEASE, "POWER_ON");
		wl.setReferenceCounted(true);
		wl.acquire();
		wl.release();
	    }
	    else if (intent.getAction().equalsIgnoreCase(
		    Intent.ACTION_HEADSET_PLUG))
	    {
		if (intent.hasExtra("state"))
		{
		    if (intent.getIntExtra("state", 0) == 0)
		    {
			runOnUiThread(new Runnable()
			{
			    public void run()
			    {
				// m_Handler.sendEmptyMessage(5);
				headsetConnected = false;
				open.setChecked(false);
				open.setClickable(false);
				tvHeadsetStatus.setText("Unplugged");
				// Toast.makeText(MainActivity.this,
				// "Headset plug out",
				// Toast.LENGTH_SHORT).show();
			    }
			});

		    }
		    else if (intent.getIntExtra("state", 0) == 1)
		    {
			runOnUiThread(new Runnable()
			{
			    public void run()
			    {
				// m_Handler.sendEmptyMessage(6);
				headsetConnected = true;
				open.setClickable(true);
				tvHeadsetStatus.setText("Plugged");

				// Toast.makeText(MainActivity.this,
				// "Headset plug in",
				// Toast.LENGTH_SHORT).show();
			    }
			});

		    }
		}
	    }// else if(intent.getAction().equalsIgnoreCase(Intent.
	}
    }

    public void startCapture()
    {
	long time = System.currentTimeMillis();
	Calendar c = Calendar.getInstance();
	c.setTimeInMillis(time);

	String fileName;

	fileName = "POPLog_"
		+ Build.MODEL
		+ "_"
		+ Integer.toString(c.get(Calendar.YEAR))
		+ Integer.toString(c.get(Calendar.MONTH) + 101).substring(1)
		+ Integer.toString(c.get(Calendar.DATE) + 100).substring(1)
		+ Integer.toString(c.get(Calendar.HOUR_OF_DAY) + 100)
			.substring(1)
		+ Integer.toString(c.get(Calendar.MINUTE) + 100).substring(1)
		+ Integer.toString(c.get(Calendar.SECOND) + 100).substring(1)
		+ ".csv";

	this.writeFile(fileName);
    }

    private FileOutputStream m_fos = null;

    private void writeFile(String filename)
    {

	try
	{
	    File file = new File(Environment.getExternalStorageDirectory()
		    .getPath() + "/ARETE/", filename);
	    m_fos = new FileOutputStream(file);
	    DataOutputStream ostream = new DataOutputStream(m_fos);

	    synchronized (tagArray)
	    {
		for (int i = 0; i < tagArray.size(); i++)
		{
		    String temp_tag = tagArray.get(i).getName();
		    String temp_count = tagArray.get(i).getValue();

		    try
		    {
			ostream.writeBytes(temp_tag + ",");
			ostream.writeBytes(temp_count + "\n");
		    }
		    catch (IOException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    }
	    m_fos.flush();
	    ostream.flush();
	    m_fos.close();
	    ostream.close();
	}
	catch (IOException e)
	{
	    e.printStackTrace();
	}
    }

    public static Intent createIntent(Context context)
    {
	Intent i = new Intent(context, MainActivity.class);
	i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	return i;
    }

    // private void createShareIntent()
    // {
    //
    // if (RcpApi.isOpen)
    // {
    //
    // Intent intent = new Intent(MainActivity.this, OptionActivity.class);
    // startActivity(intent);
    // }
    // else
    // {
    // Toast.makeText(MainActivity.this, "Not Opend", Toast.LENGTH_SHORT)
    // .show();
    // }
    // }
    // list clear - adapter clear and array clear

    synchronized private void ListClear()
    {
	// runOnUiThread(new Runnable()
	// {
	// public void run()
	// {
	tagArray.clear();
	tagAdapter.notifyDataSetChanged();
	// tvTagCount.setText(" 0  tags");
	tvTagCount.setText("0  tags");
	// }
	// });
    }

    synchronized private void ListRefreshWithRssi(final int[] data,
	    final int rssi)
    {
	{
	    {
		String str = RcpLib.int2str(data);

		boolean newTagReceived = true;
		int index;

		for (index = 0; index < tagArray.size(); index++)
		{
		    if (tagArray.get(index).getName().equals(str))
		    {
			// System.out.println(seqArray.get(index).getName() +
			// " -- " + str);
			newTagReceived = false;
			break;
		    }
		}

		if (newTagReceived)
		{
		    final customCell ttag = new customCell();
		    // tagArray.add(str);

		    if (encoding_type == 0)
		    {
			ttag.setName(str);
		    }
		    else
		    {
			ttag.setName(new String(RcpLib
				.convertStringToByteArray(str)));
		    }

		    ttag.setValue(Integer.toString(rssi));

		    runOnUiThread(new Runnable()
		    {
			public void run()
			{
			    tagArray.add(ttag);
			    tagAdapter.notifyDataSetChanged();
			    tvTagCount.setText(tagArray.size() + " tags");
			}
		    });
		}
		else
		{
		    final int indexLock = index;

		    runOnUiThread(new Runnable()
		    {
			public void run()
			{
			    tagArray.get(indexLock).setValue(
				    Integer.toString(rssi));
			    tagAdapter.notifyDataSetChanged();
			}
		    });

		}
	    }

	}

    }

    synchronized private void ListRefresh(final int[] data)
    {
	// new Thread(new Runnable()
	{
	    // @Override
	    // public void run()
	    {
		String str = RcpLib.int2str(data);
		boolean newTagReceived = true;
		int index;

		for (index = 0; index < tagArray.size(); index++)
		{
		    if (tagArray.get(index).getName().equals(str))
		    {
			// System.out.println(seqArray.get(index).getName() +
			// " -- " + str);
			newTagReceived = false;
			break;
		    }
		}

		if (newTagReceived)
		{
		    final customCell ttag = new customCell();
		    // tagArray.add(str);

		    if (encoding_type == 0)
		    {
			ttag.setName(str);
		    }
		    else
		    {
			ttag.setName(new String(RcpLib
				.convertStringToByteArray(str)));
		    }

		    ttag.setValue("1");

		    runOnUiThread(new Runnable()
		    {
			public void run()
			{
			    tagArray.add(ttag);
			    tagAdapter.notifyDataSetChanged();
			    tvTagCount.setText(tagArray.size() + " tags");
			}
		    });
		}
		else
		{

		    final int indexLock = index;
		    final int currCount = Integer.parseInt((tagArray
			    .get(indexLock).getValue()));

		    runOnUiThread(new Runnable()
		    {
			public void run()
			{
			    tagArray.get(indexLock).setValue(
				    Integer.toString(currCount + 1));
			    tagAdapter.notifyDataSetChanged();
			}
		    });

		}
	    }

	}// ).start();

	// m_Handler.sendEmptyMessageDelayed(4, 1000);
    }

    // Resume setRcpEvent- mainActivity
    // RcpOpen check

    @Override
    protected void onResume()
    {
	super.onResume();

	SharedPreferences prefs = PreferenceManager
		.getDefaultSharedPreferences(this);

	boolean currentDisplayRssi = prefs.getBoolean("DISPLAY_RSSI", false);

	if (displayRssi != currentDisplayRssi)
	{
	    ListClear();
	    displayRssi = currentDisplayRssi;
	}

	RcpApi.setRcpEvent(this);
	if (RcpApi.isOpen)
	{
	    runOnUiThread(new Runnable()
	    {
		public void run()
		{
		    // m_Handler.sendEmptyMessage(8);
		    try
		    {
			RcpApi.getReaderInfo((byte) 0xB0);
		    }
		    catch (RcpException e)
		    {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }
		}
	    });
	}
    }

    // Stop - not need
    @Override
    protected void onStop()
    {
	super.onStop();
	overridePendingTransition(R.anim.slide_out_left1,
		R.anim.slide_out_left1);
    }

    @Override
    protected void onPause()
    {
	super.onPause();
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation)
    {
	// TODO Auto-generated method stub
	super.setRequestedOrientation(requestedOrientation);
    }

    // Destroy - clearCache
    @Override
    protected void onDestroy()
    {
	if (isFinishing())
	{
	    try
	    {
		if (RcpApi.isOpen == true)
		{
		    RcpApi.close();
		}
		usrUnRegisterIntent();
		m_bOnCreate = false;
	    }
	    catch (Exception e)
	    {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	// pref.edit().putBoolean("flag", flag);
	// pref.edit().commit();
	super.onDestroy();
    }

    // volumax now Voulume backup and volume Max
    private void setVolumeMax()
    {
	AudioManager AudioManager = (AudioManager) getApplicationContext()
		.getSystemService(Context.AUDIO_SERVICE);

	// m_VolumeBackup = AudioManager
	// .getStreamVolume(android.media.AudioManager.STREAM_MUSIC);

	AudioManager
		.setStreamVolume(
			android.media.AudioManager.STREAM_MUSIC,
			AudioManager
				.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC),
			1);
    }

    @Override
    public void onTagReceived(int[] data)
    {
	ListRefresh(data);
    }

    @Override
    public void onTagWithRssiReceived(int[] data, int rssi)
    {
	ListRefreshWithRssi(data, rssi);
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
    public void onSuccessReceived(int[] data)
    {
	// TODO Auto-generated method stub
	displayConnected();
    }

    @Override
    public void onReaderInfoReceived(int[] data)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onFailureReceived(final int[] data)
    {
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		test_msg = RcpLib.int2str(data);
		Toast.makeText(MainActivity.this, "Error code  :  " + test_msg,
			Toast.LENGTH_SHORT).show();
	    }
	});
    }

    @Override
    public void onResetReceived(int[] data)
    {
	// TODO Auto-generated method stub
	displayConnected();
    }

    private void displayConnected()
    {
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		tvHeadsetStatus.setText("Connected");
	    }
	});
	// m_Handler.sendEmptyMessage(3);
    }

    @Override
    public void onAuthenticat(int[] arg0)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onBeepStateReceived(int[] arg0)
    {
	// TODO Auto-generated method stub
    }

    @Override
    public void onAdcReceived(final int[] dest)
    {
	if (dest.length < 3)
	    return;

	int now = dest[0] & 0xff;
	int min = dest[1] & 0xff;
	int max = dest[2] & 0xff;

	if (max - min != 0)
	{
	    battery = now - min;
	    battery *= 100;
	    battery /= max - min;
	}
	else
	{
	    battery = 0;
	}

	// clamping 0 <= battery <= 100
	if (battery > 100)
	{
	    battery = 100;
	}

	if (battery < 0)
	{
	    battery = 0;
	}

	// m_Handler.sendEmptyMessage(7);
	runOnUiThread(new Runnable()
	{
	    public void run()
	    {
		tvBattery.setText(Integer.toString(battery) + "%");
		tvHeadsetStatus.setText("Connected");
	    }
	});
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

}
