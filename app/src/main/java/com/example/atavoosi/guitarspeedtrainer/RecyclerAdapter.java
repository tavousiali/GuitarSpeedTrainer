package com.example.atavoosi.guitarspeedtrainer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private Context context;
    private LayoutInflater inflater;
    List<SimilarAppModel.SimilarApp> items;

    public RecyclerAdapter(Context context, List<SimilarAppModel.SimilarApp> similarApps) {
        this.context = context;
        this.items = similarApps;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.similar_app_recycler_layout, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(context)
                .load(items.get(position).imageUrl)
                .error(R.drawable.ic_menu) //باید تصحیح شود
                .placeholder(R.drawable.image_pre_view)
                .into(holder.thumbnail);

        //holder.thumbnail.setImageResource(R.drawable.change_time);
        holder.title.setText(items.get(position).title);
        holder.desc.setText(items.get(position).desc);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        TextView desc;

        public ViewHolder(View itemView) {
            super(itemView);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            title = (TextView) itemView.findViewById(R.id.title);
            desc = (TextView) itemView.findViewById(R.id.desc);
        }
    }
}
