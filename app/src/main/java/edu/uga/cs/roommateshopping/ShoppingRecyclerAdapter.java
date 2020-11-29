package edu.uga.cs.roommateshopping;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class ShoppingRecyclerAdapter extends RecyclerView.Adapter<ShoppingRecyclerAdapter.DataHolder> {

    public static final String DEBUG_TAG = "RecyclerAdapter";

    private List<Item> itemsList;

    public ShoppingRecyclerAdapter(List<Item> itemsList ) {
            this.itemsList = itemsList;
    }

    // The adapter must have a ViewHolder class to "hold" one item to show.
    class DataHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView price;
        TextView quantity;

        public DataHolder(View itemView ) {
            super(itemView);

            name = (TextView) itemView.findViewById( R.id.name );
            price = (TextView) itemView.findViewById( R.id.price );
            quantity = (TextView) itemView.findViewById( R.id.quantity );
        }
    }

        @Override
        public DataHolder onCreateViewHolder( ViewGroup parent, int viewType ) {
            View view = LayoutInflater.from( parent.getContext()).inflate( R.layout.activity_recycler_view, parent, false );
            return new DataHolder( view );
        }

        // This method fills in the values of the Views to show a JobLead
        @Override
        public void onBindViewHolder( DataHolder holder, int position ) {
            Item item = itemsList.get( position );

            Log.d( DEBUG_TAG, "onBindViewHolder: " + item );

            holder.name.setText( item.getName());
            holder.price.setText( ""  + item.getPrice() );
            holder.quantity.setText( ""  + item.getQuantity() );
        }

        @Override
        public int getItemCount() {
            return itemsList.size();
        }
}