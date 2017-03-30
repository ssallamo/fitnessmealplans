package nz.ac.cornell.fitnessmealplans.Dairy;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.expandablelistview.BaseSwipeMenuExpandableListAdapter;
import com.baoyz.swipemenulistview.ContentViewWrapper;

import java.util.ArrayList;

import nz.ac.cornell.fitnessmealplans.Models.Category;
import nz.ac.cornell.fitnessmealplans.Models.Menu;
import nz.ac.cornell.fitnessmealplans.R;

/**
 * Basically, it's the same as BaseExpandableListAdapter. But added controls
 * to every item's swipability
 *
 * @author yuchentang
 * @see android.widget.BaseExpandableListAdapter
 */
class AppAdapter extends BaseSwipeMenuExpandableListAdapter {
    private Context context;
    private ArrayList<Category> categoryList;
    boolean myMenuFlag = false;

    public AppAdapter(Context context, ArrayList<Category> categoryList) {
        this.context = context;
        this.categoryList = new ArrayList<Category>();
        this.categoryList.addAll(categoryList);
        //this.originalList = new ArrayList<Category>();
        //this.originalList.addAll(categoryList);
    }

    /**
     * Whether this group item swipable
     *
     * @param groupPosition
     * @return
     * @see com.allen.expandablelistview.BaseSwipeMenuExpandableListAdapter#isGroupSwipable(int)
     */
    @Override
    public boolean isGroupSwipable(int groupPosition) {
        return false;
    }

    /**
     * Whether this child item swipable
     *
     * @param groupPosition
     * @param childPosition
     * @return
     * @see com.allen.expandablelistview.BaseSwipeMenuExpandableListAdapter#isChildSwipable(int,
     *      int)
     */
    @Override
    public boolean isChildSwipable(int groupPosition, int childPosition) {
        Category category = (Category)getGroup(groupPosition);
        if(!(category.getCategoryId()).equals("personal")) {
            return false;
        }
        return true;
    }

        /*@Override
        public int getChildType(int groupPosition, int childPosition) {
            return childPosition % 3;
        }

        @Override
        public int getChildTypeCount() {
            return 3;
        }

        @Override
        public int getGroupType(int groupPosition) {
            return groupPosition % 3;
        }

        @Override
        public int getGroupTypeCount() {
            return 3;
        }*/

    class ParentViewHolder {
        //ImageView iv_icon;
        //TextView tv_name;
        ImageView ivParent;
        TextView tvCategory;

        public ParentViewHolder(View view) {
            //iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            //tv_name = (TextView) view.findViewById(R.id.tv_name);
            ivParent = (ImageView) view.findViewById(R.id.ivParent);
            tvCategory = (TextView) view.findViewById(R.id.tvCategory);
            view.setTag(this);
        }
    }

    class ChildViewHolder {
        //ImageView iv_icon;
        //TextView tv_name;
        TextView tvName, tvCalory;

        public ChildViewHolder(View view) {
            //iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            //tv_name = (TextView) view.findViewById(R.id.tv_name);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvCalory = (TextView) view.findViewById(R.id.tvCalory);
            view.setTag(this);
        }
    }

    @Override
    public int getGroupCount() {
        return categoryList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ArrayList<Menu> menuList = categoryList.get(groupPosition).getMenuList();
        return menuList.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categoryList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ArrayList<Menu> menuList = categoryList.get(groupPosition).getMenuList();
        return menuList.get(childPosition);
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

    @Override
    public ContentViewWrapper getGroupViewAndReUsable(int groupPosition, boolean isExpanded, View convertView,
                                                      ViewGroup parent) {
        boolean reUseable = true;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.parent_layout, null);
            convertView.setTag(new ParentViewHolder(convertView));
            reUseable = false;
        }
        ParentViewHolder holder = (ParentViewHolder) convertView.getTag();
        //ApplicationInfo item = (ApplicationInfo) getGroup(groupPosition);
        Category category = (Category)getGroup(groupPosition);
        //holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
        //holder.tv_name.setText(item.loadLabel(getPackageManager()));
        holder.tvCategory.setText(category.getCategoryName());
        return new ContentViewWrapper(convertView, reUseable);
    }

    @Override
    public ContentViewWrapper getChildViewAndReUsable(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                                                      ViewGroup parent) {
        boolean reUseable = true;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.child_layout, null);
            convertView.setTag(new ChildViewHolder(convertView));
            reUseable = false;
        }
        ChildViewHolder holder = (ChildViewHolder) convertView.getTag();
        if (null == holder) {
            holder = new ChildViewHolder(convertView);
        }

        //ApplicationInfo item = (ApplicationInfo) getGroup(groupPosition);
        //convertView.setBackgroundColor(Color.GRAY);
        //holder.iv_icon.setImageDrawable(item.loadIcon(getPackageManager()));
        Menu menu = (Menu) getChild(groupPosition, childPosition);

        holder.tvName.setText(menu.getMenuName());
        holder.tvCalory.setText(String.valueOf(menu.getCalories()));

        //holder.tv_name.setText("this is child");
        return new ContentViewWrapper(convertView, reUseable);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}