package com.ui.advanced.advanceddemo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by vivian on 2016/12/19.
 */

public class HomeAdapter extends RecyclerView.Adapter {
    public final static String TAG = HomeAdapter.class.getSimpleName();

    private SparseArray<String> mData;
    private Context mContext;

    private int mDataType = 0;

    // click listener
    private OnItemClickLitener mOnItemClickLitener;

    public HomeAdapter(Context context, int dataType) {
        mContext = context;
        mDataType = dataType;
        initData();
    }

    protected void initData() {
        mData = new SparseArray<String>();
        // check dataType
        if (mDataType < 0 || mDataType >= Const.ARRAYLIST.length) {
            mDataType = 0;
        }

        final String[] contents = mContext.getResources().getStringArray(Const.ARRAYLIST[mDataType]);
        for (int i = 0; i < contents.length; i++) {
            mData.put(i, contents[i]);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.recycler_item, parent,
                false));
        return holder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        // get data array, {id,text}
        String[] array = mData.get(position).split(",");
        if (array.length > 1) {
            viewHolder.tv.setText(array[1]);
            // set listener
            setItemListener(viewHolder, array);
        } else {
            viewHolder.tv.setText(array[0]);
        }
    }

    private void setItemListener(final MyViewHolder holder, final String[] array) {
        if (mOnItemClickLitener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos, array);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // view holder
    class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv;

        public MyViewHolder(View view) {
            super(view);
            tv = (TextView) view.findViewById(R.id.tv_item);
        }
    }

    public interface OnItemClickLitener {
        /**
         * @param view
         * @param position
         * @param info     {id, name}
         */
        void onItemClick(View view, int position, String[] info);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickLitener(OnItemClickLitener onItemClickLitener) {
        this.mOnItemClickLitener = onItemClickLitener;
    }
}
