package practise.com.samplemap.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;

import practise.com.samplemap.Interfaces.ItemClickListener;
import practise.com.samplemap.R;


public class MapAdapter extends RecyclerView.Adapter<MapAdapter.MyViewHolder> {
    private ArrayList<String> text,detail_names;
    private ArrayList<Integer>images;
    Context c;
    private int Selected=-1;
    private ItemClickListener listener;
    public MapAdapter(ArrayList<String>s, ArrayList<String>n, ArrayList<Integer>m, Context c){
        text=s;
        images=m;
        detail_names=n;
        this.c=c;
    }
   /* public MapAdapter(ArrayList<String>s, ArrayList<String>n, ArrayList<Integer>m, Context c, int
            Selected){
        text=s;
        images=m;
        detail_names=n;
        this.c=c;
        this.Selected=Selected;
    }*/
    public void setClickListener(ItemClickListener itemClickListener) {
        this.listener = itemClickListener;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.map_items,parent,false),c);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        holder.textView.setText(text.get(position));
      //  holder.imageView.setImageResource(images.get(position));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Selected=position;
                listener.Click(view,position);
                notifyDataSetChanged();
            }
        });
        if(Selected==position){
            holder.layout.setBackgroundColor(Color.parseColor("#ededed"));

        }
        else
        {
            holder.layout.setBackgroundColor(Color.parseColor("#ffffff"));

        }


    }

    @Override
    public int getItemCount() {
        return text.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        ImageView imageView;
        TextView textView;
        ConstraintLayout layout;
        Context c;
        public MyViewHolder(View itemView,Context c) {
            super(itemView);
            this.c=c;
            textView=itemView.findViewById(R.id.map_item_txt);
            imageView=itemView.findViewById(R.id.map_item_img);
            layout=itemView.findViewById(R.id.map_constraintlayout);

        }


    }
}
