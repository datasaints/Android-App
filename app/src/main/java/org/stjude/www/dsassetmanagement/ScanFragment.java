package org.stjude.www.dsassetmanagement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.content.Intent;

import com.phychips.common.ReaderIo;
import com.phychips.rcp.*;



/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment implements iRcpEvent {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_scan, container, false);

        TextView tv = (TextView) v.findViewById(R.id.tvFragSecond);
        tv.setText(getArguments().getString("msg"));
        final Button button = (Button) v.findViewById(R.id.scan_button);

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAct = new Intent(getActivity().getApplicationContext(), MainActivity2.class);
                newAct.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(newAct);

                //Start new intent here

//                if(!RcpApi.isOpen) { //check if reader is open
//                    try {
//                        RcpApi.open();
//                        button.setText("Stop Reading");
//                    }
//                    catch(Exception e) {
//                        System.out.println("Reader cannot be opened");
//                    }
//
//                    //read tags here
//
//                }
//                else {
//                    try {
//                        RcpApi.close();
//                        button.setText("Start Reading");
//                    }
//                    catch(Exception e) {
//                        System.out.println("Reader cannot be closed");
//                    }
//                }
            }
        });

        return v;
    }

    public static ScanFragment newInstance(String text) {

        ScanFragment f = new ScanFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
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

    public void onTagReceived(int[] var1) {

    }

    public void onTagWithRssiReceived(int[] var1, int var2) {

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

    public void onFailureReceived(int[] var1) {

    }

    public void onAuthenticat(int[] var1) {

    }

    public void onBeepStateReceived(int[] var1) {

    }

    public void onAdcReceived(int[] var1) {

    }

    public void onTestFerPacketReceived(int[] var1){

    }

    public void onTagMemoryLongReceived(int[] var1) {

    }
}