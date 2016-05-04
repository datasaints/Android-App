package org.stjude.www.dsassetmanagement.dsassetmanagement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.media.AudioManager;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.jwidjaja.aretetest.R;
import com.phychips.rcp.*;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements iRcpEvent {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

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

    IntentBroadcastReceiver m_IntentReceiver = null;


    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment ScanFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanFragment newInstance(String param1) {
        ScanFragment fragment = new ScanFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        usrRegisterIntent();

        //phone call stuff?
        TelephonyManager mTelMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan, container, false);

        //for listing stuff
        epclist = (ListView) v.findViewById(R.id.listView);
        tvTagCount = (TextView) v.findViewById(R.id.name);
        tvHeadsetStatus = (TextView) v.findViewById(R.id.aboutvalue);
        tvBattery = (TextView) v.findViewById(R.id.textView3);
        tagAdapter = new CustomListAdapter(getActivity().getApplicationContext(),
                R.layout.customlistview, tagArray); // might need to change later? Not sure about application context
        epclist.setAdapter(tagAdapter);

        final Button button = (Button) v.findViewById(R.id.scan_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!RcpApi.isOpen) { //check if reader is open
                    try {
                        RcpApi.open();
                        setVolumeMax();
                        button.setText("Stop Reading");

                    }
                    catch(Exception e) {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                Toast.makeText(getActivity(), "Reader Cannot be Opened.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    //start reading tags
                    try {
                        RcpApi.startReadTagsWithRssi(max_tag, max_time, repeat_cycle);
                    }
                    catch (Exception e) {
                        System.out.println("Reading failed");
                    }


                }
                else {
                    try {
                        RcpApi.stopReadTags();
//                        RcpApi.close();
                        button.setText("Start Reading");
                    }
                    catch(Exception e) {
                        System.out.println("Reader cannot be closed");
                    }
                }
            }
        });

        clearScreen = (Button) v.findViewById(R.id.clear_button);
        clearScreen.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(tagArray.size() > 0) {
                    ListClear();
                }
            }
        });

        return v;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onResume() {
        super.onResume();
        RcpApi.setRcpEvent(this);
    }

    // volumax now Voulume backup and volume Max
    private void setVolumeMax()
    {
        AudioManager AudioManager = (AudioManager) getActivity().
                getApplicationContext().getSystemService(Context.AUDIO_SERVICE);

        // m_VolumeBackup = AudioManager
        // .getStreamVolume(android.media.AudioManager.STREAM_MUSIC);

        AudioManager
                .setStreamVolume(
                        android.media.AudioManager.STREAM_MUSIC,
                        AudioManager
                                .getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC),
                        1);
    }

    public class IntentBroadcastReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context arg0, Intent intent)
        {
            System.out.println(intent.toString());

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_SCREEN_OFF))
            {
                PowerManager pm = (PowerManager) getActivity().getSystemService(Context.POWER_SERVICE);
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
                        getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                // m_Handler.sendEmptyMessage(5);
                                headsetConnected = false;
                                tvHeadsetStatus.setText("Unplugged");
                                try {
                                    RcpApi.stopReadTags();
                                }
                                catch(Exception e) {
                                    System.out.println("Cannot stop read");
                                }
                                // Toast.makeText(MainActivity.this,
                                // "Headset plug out",
                                // Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    else if (intent.getIntExtra("state", 0) == 1)
                    {
                        getActivity().runOnUiThread(new Runnable()
                        {
                            public void run()
                            {
                                // m_Handler.sendEmptyMessage(6);
                                headsetConnected = true;
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

    private void usrRegisterIntent()
    {
        usrUnRegisterIntent();

        m_IntentReceiver = new IntentBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_HEADSET_PLUG);
        getActivity().registerReceiver(m_IntentReceiver, filter);
    }

    private void usrUnRegisterIntent()
    {
        if (m_IntentReceiver != null)
        {
            getActivity().unregisterReceiver(m_IntentReceiver);
            m_IntentReceiver = null;
        }
    }

    synchronized private void ListClear()
    {
        tagArray.clear();
        tagAdapter.notifyDataSetChanged();
        tvTagCount.setText("0  tags");
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

                    getActivity().runOnUiThread(new Runnable()
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

                    getActivity().runOnUiThread(new Runnable()
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


    @Override
    public void onPause()
    {
        super.onPause();
    }

    // Destroy - clearCache
    @Override
    public void onDestroy()
    {
        if (getActivity().isFinishing())
        {
            try
            {
                if (RcpApi.isOpen == true)
                {
                    RcpApi.close();
                }
                usrUnRegisterIntent();
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


    public void onTagReceived(int[] var1) {

    }

    public void onTagWithRssiReceived(int[] data, int rssi) {
        ListRefreshWithRssi(data, rssi);
    }

    public void onReaderInfoReceived(int[] var1) {

    }

    public void onRegionReceived(int[] var1) {

    }

    public void onSelectParamReceived(int[] var1) {

    }

    public void onQueryParamReceived(int[] var1) {

    }

    public void onChannelReceived(int[] var1) {

    }

    public void onFhLbtReceived(int[] var1) {

    }

    public void onTxPowerLevelReceived(int[] var1) {

    }

    public void onTagMemoryReceived(int[] var1) {

    }

    public void onHoppingTableReceived(int[] var1) {

    }

    public void onModulationParamReceived(int[] var1) {

    }

    public void onAnticolParamReceived(int[] var1) {

    }

    public void onTempReceived(int[] var1) {

    }

    public void onRssiReceived(int[] var1) {

    }

    public void onRegistryItemReceived(int[] var1) {

    }

    public void onResetReceived(int[] var1) {

    }

    public void onSuccessReceived(int[] var1) {

    }

    public void onFailureReceived(final int[] data) {
        getActivity().runOnUiThread(new Runnable()
        {
            public void run()
            {
                test_msg = RcpLib.int2str(data);
                Toast.makeText(getActivity(), "Error code  :  " + test_msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onAuthenticat(int[] var1) {

    }

    public void onBeepStateReceived(int[] var1) {

    }

    //do we even need this?
    public void onAdcReceived(int[] dest) {
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
        getActivity().runOnUiThread(new Runnable()
        {
            public void run()
            {
                tvBattery.setText(Integer.toString(battery) + "%");
                tvHeadsetStatus.setText("Connected");
            }
        });
    }

    public void onTestFerPacketReceived(int[] var1){

    }

    public void onTagMemoryLongReceived(int[] var1) {

    }


}
