package com.survivalkid.menu;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.survivalkid.R;
import com.survivalkid.game.entity.GameEntity;
import com.survivalkid.game.entity.enemy.impl.Bull;
import com.survivalkid.game.entity.enemy.impl.Caterpillar;
import com.survivalkid.game.entity.enemy.impl.CircularSaw;
import com.survivalkid.game.entity.item.impl.BalloonCrate;
import com.survivalkid.game.util.BitmapUtil;

public class Item_Adapter extends BaseAdapter{
    private LayoutInflater inflater;
    private List<GameEntity> data;

    public Item_Adapter(Context context, List<GameEntity> table){
    // Caches the LayoutInflater for quicker uses
    this.inflater = LayoutInflater.from(context);
    // Sets the events data
    this.data= table;
    }

    public int getCount() {
        return this.data.size();
    }

    public GameEntity getItem(int position) throws IndexOutOfBoundsException{
        return this.data.get(position);
    }

    public long getItemId(int position) throws IndexOutOfBoundsException{
        if(position < getCount() && position >= 0 ){
            return position;
        }
        return -1;
    }

    public int getViewTypeCount(){
        return 1;
    }

    public View getView(int position, View convertView, ViewGroup parent){
    	GameEntity entity = getItem(position);           

        if(convertView == null){ // If the View is not cached
            // Inflates the Common View from XML file
            convertView = this.inflater.inflate(R.layout.how_to_play_single_entity, null);
        }
        
        ImageView img = (ImageView)convertView.findViewById(R.id.entityImage);
        
        if (entity instanceof Bull) {
        	img.setImageBitmap(BitmapUtil.createBitmap(R.drawable.bull_oneframe));
        } else if (entity instanceof Caterpillar) {
        	img.setImageBitmap(BitmapUtil.createBitmap(R.drawable.caterpillar_oneframe));
        } else if (entity instanceof CircularSaw) {
        	img.setImageBitmap(BitmapUtil.createBitmap(R.drawable.enemy_circular_saw_oneframe));
        } else if (entity instanceof BalloonCrate) {
        	img.setImageBitmap(BitmapUtil.createBitmap(R.drawable.balloon_crate_oneframe));
        } else {
        	img.setImageBitmap(entity.getSprite().getBitmap());
        }
        
        
        TextView name = (TextView)convertView.findViewById(R.id.entityTitle);
        TextView desc = (TextView)convertView.findViewById(R.id.entityText);
        name.setText(entity.getName());
        desc.setText(entity.getDescription());

        return convertView;
    }
}