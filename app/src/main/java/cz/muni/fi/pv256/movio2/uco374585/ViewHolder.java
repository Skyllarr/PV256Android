package cz.muni.fi.pv256.movio2.uco374585;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by Skylar on 12/29/2016.
 */

public class ViewHolder extends RecyclerView.ViewHolder {
    CardView cardView;
    ImageView imageView;

    public ViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }
}
