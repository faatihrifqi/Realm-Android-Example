package com.latihanandroid.kerjakan.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.latihanandroid.kerjakan.R;
import com.latihanandroid.kerjakan.model.Kerjaan;

import io.realm.RealmResults;

public class KerjaanAdapter extends RecyclerView.Adapter<KerjaanAdapter.ViewHolder> {

    private RealmResults<Kerjaan> kerjaanList;

    final private ListItemClickListener mOnClickListener;

    public interface ListItemClickListener{
        void onListItemClick(Kerjaan kerjaan);
    }

    public KerjaanAdapter(RealmResults<Kerjaan> kerjaanList, ListItemClickListener listener) {
        this.kerjaanList = kerjaanList;
        mOnClickListener = listener;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tv_kerjaan;

        ViewHolder(View view) {
            super(view);
            tv_kerjaan = (TextView) view.findViewById(R.id.tv_kerjaan);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(kerjaanList.get(clickedPosition));
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lakukan, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Kerjaan kerjaan = kerjaanList.get(position);

        holder.tv_kerjaan.setText(kerjaan.getItemKerjaan());
    }

    @Override
    public int getItemCount() {
        return kerjaanList.size();
    }

}
