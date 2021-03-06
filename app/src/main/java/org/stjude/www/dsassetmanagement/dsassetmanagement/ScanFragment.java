package org.stjude.www.dsassetmanagement.dsassetmanagement;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.widget.Button;

import com.jwidjaja.aretetest.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private Intent newIntent;

    private OnFragmentInteractionListener mListener;

    public ScanFragment() {
        // Required empty public constructor
    }

    /**
     * Placeholder to create new fragment Instance
     * @param param1 Parameter 1.
     * @return A new instance of fragment ScanFragment.
     */
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_scan, container, false);
        final Button tagSearch = (Button) v.findViewById(R.id.tag_search_button);

        tagSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newIntent = getActivity().getPackageManager().
                        getLaunchIntentForPackage("com.uk.tsl.rfid.findatag");
                startActivity(newIntent);
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
}
