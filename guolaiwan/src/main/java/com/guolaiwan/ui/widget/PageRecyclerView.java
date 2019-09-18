package com.guolaiwan.ui.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * 创建者: 蔡朝阳
 * 日期:  2019/1/5.
 * 说明:  重写RecyclerView实现多行
 *        通过dispatchTouchEvent处理与下拉刷新控件产生的横向滑动冲突
 */

public class PageRecyclerView extends RecyclerView{

    private Context mContext = null;
    private PageAdapter myAdapter = null;
    private int spanRow = 1; // 行数
    private int spanColumn = 3; // 每页列数
    private int pageMargin = 0; // 页间距


    public PageRecyclerView(Context context) {
        this(context, null);
    }

    public PageRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        defaultInit(context);
    }

    // 默认初始化
    private void defaultInit(Context context) {
        this.mContext = context;
        setLayoutManager(new AutoGridLayoutManager(mContext, spanRow, AutoGridLayoutManager.HORIZONTAL, false));
        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    /**
     * 设置行数和每页列数
     *
     * @param spanRow    行数，<=0表示使用默认的行数
     * @param spanColumn 每页列数，<=0表示使用默认每页列数
     */
    public void setPageSize(int spanRow, int spanColumn) {
        this.spanRow = spanRow <= 0 ? this.spanRow : spanRow;
        this.spanColumn = spanColumn <= 0 ? this.spanColumn : spanColumn;
        setLayoutManager(new AutoGridLayoutManager(
                mContext, this.spanRow, AutoGridLayoutManager.HORIZONTAL, false));
    }

    /**
     * 设置页间距
     *
     * @param pageMargin 间距(px)
     */
    public void setPageMargin(int pageMargin) {
        this.pageMargin = pageMargin;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
        this.myAdapter = (PageAdapter) adapter;
    }

    private int lastXIntercepted;
    private int lastYIntercepted;
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int x = (int) ev.getX();
        int y = (int) ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                final int deltaX = x - lastXIntercepted;
                final int deltaY = y - lastYIntercepted;
                if (Math.abs(deltaX) > Math.abs(deltaY)) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        lastXIntercepted = x;
        lastYIntercepted = y;
        return super.dispatchTouchEvent(ev);
    }



    /**
     * 数据适配器
     */
    public class PageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<?> dataList = null;
        private CallBack mCallBack = null;
        private int itemWidth = 0;
        private int itemCount = 0;

        /**
         * 实例化适配器
         *
         * @param data
         * @param callBack
         */
        public PageAdapter(List<?> data, CallBack callBack) {
            this.dataList = data;
            this.mCallBack = callBack;
            itemCount = dataList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (itemWidth <= 0) {
                // 计算Item的宽度
                itemWidth = (parent.getWidth() - pageMargin * 2) / spanColumn;
            }

            RecyclerView.ViewHolder holder = mCallBack.onCreateViewHolder(parent, viewType);

            holder.itemView.measure(0, 0);
            holder.itemView.getLayoutParams().width = itemWidth;
            holder.itemView.getLayoutParams().height = holder.itemView.getMeasuredHeight();

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (spanColumn == 1) {
                // 每个Item距离左右两侧各pageMargin
                holder.itemView.getLayoutParams().width = itemWidth + pageMargin * 2;
                holder.itemView.setPadding(pageMargin, 0, pageMargin, 0);
            } else {
                int m = position % (spanRow * spanColumn);
                if (m < spanRow) {
                    // 每页左侧的Item距离左边pageMargin
                    holder.itemView.getLayoutParams().width = itemWidth + pageMargin;
                    holder.itemView.setPadding(pageMargin, 0, 0, 0);
                } else if (m >= spanRow * spanColumn - spanRow) {
                    // 每页右侧的Item距离右边pageMargin
                    holder.itemView.getLayoutParams().width = itemWidth + pageMargin;
                    holder.itemView.setPadding(0, 0, pageMargin, 0);
                } else {
                    // 中间的正常显示
                    holder.itemView.getLayoutParams().width = itemWidth;
                    holder.itemView.setPadding(0, 0, 0, 0);
                }
            }

            if (position < dataList.size()) {
                holder.itemView.setVisibility(View.VISIBLE);
                mCallBack.onBindViewHolder(holder, position);
            } else {
                holder.itemView.setVisibility(View.INVISIBLE);
            }

        }

        @Override
        public int getItemCount() {
            return itemCount;
        }

    }

    public interface CallBack {

        /**
         * 创建VieHolder
         *
         * @param parent
         * @param viewType
         */
        RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

        /**
         * 绑定数据到ViewHolder
         *
         * @param holder
         * @param position
         */
        void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    }



}
