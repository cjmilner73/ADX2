package sds.com.adx;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class PositionListAdapter extends ArrayAdapter {

    private String TAG = "PositionListAdapter";

    private Context context;
    private boolean useList = true;



    public PositionListAdapter(Context context, List items) {
        super(context, android.R.layout.simple_list_item_1, items);
        this.context = context;
    }

    /**
     * Holder for the list items.
     */
    private class ViewHolder{
        TextView titleText;
    }

    /**
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        PositionListItem positionItem = (PositionListItem)getItem(position);
        View viewToUse = null;

        // This block exists to inflate the settings list item conditionally based on whether
        // we want to support a grid or list view.
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
//        if (convertView == null) {
//            if(useList){
//                viewToUse = mInflater.inflate(R.layout.position_item, null);
//            } else {
//                viewToUse = mInflater.inflate(R.layout.position_item, null);
//            }
//
//
//
//
//            holder = new ViewHolder();
//            holder.titleText = (TextView)viewToUse.findViewById(R.id.firstLine);
//
//            viewToUse.setTag(holder);
//        } else {
//            viewToUse = convertView;
//            holder = (ViewHolder) viewToUse.getTag();
//        }

        View rowView;
        rowView = mInflater.inflate(R.layout.position_item, parent, false);
        viewToUse = rowView;

//        ImageView i = (ImageView) rowView.findViewById(R.id.buy);
        TextView tSymbol = (TextView) rowView.findViewById(R.id.positionSymbol);
        TextView tProfit = (TextView) rowView.findViewById(R.id.positionProfit);
        TextView tDaysActive = (TextView) rowView.findViewById(R.id.positionDaysActive);
        TextView tTriggered = (TextView) rowView.findViewById(R.id.triggered);




//        if (item.getDirection().equals("BUY")) {
//            i.setImageResource(R.drawable.green_arrow);
//        } else {
//            i.setImageResource(R.drawable.red_arrow);
//        }

        if (positionItem.getDirection().equals("BUY")) {
//             tSymbol.setBackgroundColor(context.getResources().getColor(R.color.green));
//             tLatestPrice.setBackgroundColor(context.getResources().getColor(R.color.green));
//             tTrigger.setBackgroundColor(context.getResources().getColor(R.color.green));
//             tDaysActive.setBackgroundColor(context.getResources().getColor(R.color.green));
//             tSymbol.setTextColor(context.getResources().getColor(R.color.blue));
            tProfit.setTextColor(context.getResources().getColor(R.color.blue));
//             tTrigger.setTextColor(context.getResources().getColor(R.color.blue));
//             tDaysActive.setTextColor(context.getResources().getColor(R.color.blue));
        } else {
//             tSymbol.setBackgroundColor(context.getResources().getColor(R.color.red));
//             tLatestPrice.setBackgroundColor(context.getResources().getColor(R.color.red));
//             tTrigger.setBackgroundColor(context.getResources().getColor(R.color.red));
//             tDaysActive.setBackgroundColor(context.getResources().getColor(R.color.red));
//             tSymbol.setTextColor(context.getResources().getColor(R.color.red));
            tProfit.setTextColor(context.getResources().getColor(R.color.red));
//             tTrigger.setTextColor(context.getResources().getColor(R.color.red));
//             tDaysActive.setTextColor(context.getResources().getColor(R.color.red));

        }

        tSymbol.setText(positionItem.getPositionSymbol());
        tProfit.setText(String.valueOf(positionItem.getProfit()));
        tDaysActive.setText(String.valueOf(positionItem.getDaysActive()));
        tTriggered.setText(String.valueOf(positionItem.getTriggered()));

//        holder.titleText.setText(item.getItemTitle());

        return viewToUse;
    }
}

