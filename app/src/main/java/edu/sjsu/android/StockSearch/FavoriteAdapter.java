package edu.sjsu.android.StockSearch;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter class that fills the Favorite Section RecyclerView
 */
public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private LayoutInflater inflator;
    List<StockInfo> favoriteList = new ArrayList<>();
    private Context context;

    public FavoriteAdapter(Context context, List<StockInfo> favoriteList){
        inflator = LayoutInflater.from(context);
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflator.inflate(R.layout.custom_favorite_row, parent,false);
        FavoriteViewHolder holder = new FavoriteViewHolder(context, view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {

        final StockInfo current = favoriteList.get(position);

        holder.symbol.setText(current.ticker);
        holder.price.setText(current.price);
        holder.favoritesRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(((MainActivity)context), activity_StockDetail.class);
                intent.putExtra("my_data", current.ticker);
                intent.putExtra("favorite", true);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return 0;
    }
    public void newFavorites(List<StockInfo> newList) {
        favoriteList.clear();
        favoriteList.addAll(newList);
        notifyDataSetChanged();
    }

    public void removeFavorites(String str) {
        for(StockInfo favorite : favoriteList) {
            if(favorite.ticker.equals(str)) {
                favoriteList.remove(favorite);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public void clearData() {
        favoriteList.clear();
        notifyDataSetChanged();
    }
    public void addFav(StockInfo favInfo) {
        favoriteList.add(favInfo);
        notifyDataSetChanged();
    }


    /**
     * Inner View holder Class that holds the row view
                    for stock in the favorite section
     */
    public class FavoriteViewHolder extends RecyclerView.ViewHolder {
        TextView symbol;
        TextView price;
        TextView changeRate;
        LinearLayout favoritesRow;
        public FavoriteViewHolder(final Context context, View itemView){
            super(itemView);
            favoritesRow = (LinearLayout)itemView.findViewById(R.id.favoriteRow);
            symbol = (TextView)itemView.findViewById(R.id.favoriteTicker);
            price = (TextView)itemView.findViewById(R.id.favoritePrice);
            changeRate = (TextView)itemView.findViewById(R.id.favoriteChangeRate);
        }
    }
}
