package com.isuo.inspection.application.ui.data.history.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.isuo.inspection.application.R;
import com.isuo.inspection.application.model.bean.HistoryData;
import com.isuo.inspection.application.model.bean.Type1Data;
import com.isuo.inspection.application.model.bean.Type3Data;
import com.isuo.inspection.application.model.bean.Type3ItemBean;
import com.isuo.inspection.application.ui.data.history.widget.LayoutType3;
import com.isuo.inspection.application.utils.DataUtil;
import com.sito.tool.library.widget.PinnedHeaderExpandableListView;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Type3Adapter extends BaseExpandableListAdapter {
    //展示页面
    private PinnedHeaderExpandableListView mExpandListView;
    private Context context;
    private int groupLayout, childLayout;
    private List<HistoryData> data = new ArrayList<>();


    public Type3Adapter(Context context, PinnedHeaderExpandableListView expandableListView, int groupLayout, int childLayout) {
        this.context = context;
        this.groupLayout = groupLayout;
        this.childLayout = childLayout;
        this.mExpandListView = expandableListView;
    }

    public void setData(List<HistoryData> data) {
        this.data.clear();
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public void addData(List<HistoryData> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (data.get(groupPosition).getType3DataList() == null) {
            return 0;
        }
        return Objects.requireNonNull(data.get(groupPosition).getType3DataList()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return data.get(groupPosition).getType3DataList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("DefaultLocale")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(context).inflate(groupLayout, null);
            holder.timeText = convertView.findViewById(R.id.group_text);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.timeText.setText(DataUtil.timeFormat(data.get(groupPosition).getTime(), "yyyy-MM"));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(context).inflate(childLayout, null);
            holder.value1Text = convertView.findViewById(R.id.text_1);
            holder.layout = convertView.findViewById(R.id.layout);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        Type3Data bean = data.get(groupPosition).getType3DataList().get(childPosition);
        if (bean != null) {
            holder.value1Text.setText(DataUtil.timeFormat(bean.getTime(), null));
            holder.layout.removeAllViews();
            for (Type3ItemBean item : bean.getItems()) {
                LayoutType3 view = new LayoutType3(context);
                view.setData(item.getPositionText(), item.getValue1(), item.getValue2());
                holder.layout.addView(view);
            }
        }
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    /**
     * 外部显示ViewHolder
     */
    private static class GroupViewHolder {
        TextView timeText;
    }

    /**
     * 内部显示ViewHolder
     */
    private static class ChildViewHolder {
        TextView value1Text;
        LinearLayout layout;
    }
}
