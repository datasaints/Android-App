package org.stjude.www.dsassetmanagement.dsassetmanagement;

import java.util.ArrayList;

// Not used
//import phychips.arete.newver.R;
//import phychips.arete.newver.TagAccessActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jwidjaja.aretetest.R;

public class CustomListAdapter extends ArrayAdapter<customCell>
{

    private Context context;
    private int mResource;
    private ArrayList<customCell> mList;
    private LayoutInflater mInflater;

    public CustomListAdapter(Context context, int layoutResource,
	    ArrayList<customCell> objects)
    {
	super(context, layoutResource, objects);
	this.context = context;
	this.mResource = layoutResource;
	this.mList = objects;
	this.mInflater = (LayoutInflater) context
		.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
	final customCell cell = mList.get(position);

	if (convertView == null)
	{
	    convertView = mInflater.inflate(mResource, null);
	}

	if (cell != null)
	{

	    TextView tTextView = (TextView) convertView
		    .findViewById(R.id.list_tag);
	    TextView cTextView = (TextView) convertView
		    .findViewById(R.id.list_count);
	    @SuppressWarnings("unused")
	    ImageView cImageView = (ImageView) convertView
		    .findViewById(R.id.listSelect);

	    tTextView.setText(cell.getName());
	    cTextView.setText(cell.getValue());
	    // tTextView.setOnLongClickListener(new OnLongClickListener() {
	    //
	    // @Override
	    // public boolean onLongClick(View v) {
	    // // TODO Auto-generated method stub
	    // Intent intent = new Intent(context, TagMemoryActivity.class);
	    // System.out.println(stag.getTag());
	    // intent.putExtra("tagitem", stag.getTag());
	    // context.startActivity(intent);
	    // return false;
	    // }
	    // });

	    // convertView.setOnClickListener(new OnClickListener()
	    // {
	    //
	    // @Override
	    // public void onClick(View v) {
	    // // TODO Auto-generated method stub
	    // Intent intent = new Intent(context, TagAccessActivity .class);
	    // intent.putExtra("tagitem", stag.getName());
	    // context.startActivity(intent);
	    //
	    // }
	    // });

	}

	return convertView;

    }
}
