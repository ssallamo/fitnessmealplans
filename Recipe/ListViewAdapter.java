package nz.ac.cornell.fitnessmealplans.Recipe;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import nz.ac.cornell.fitnessmealplans.Models.RecipeListItem;
import nz.ac.cornell.fitnessmealplans.R;

/**
 * This class is dealing with one item of listview
 * Created by HJS on 2016-05-08.
 */
public class ListViewAdapter extends BaseAdapter {

    private ArrayList<RecipeListItem> lvRecipe = new ArrayList<RecipeListItem>();

    public ListViewAdapter() {
    }

    /**
     * number of List
     * @return : int - number of list Item
     */
    @Override
    public int getCount() {
        return lvRecipe.size();
    }

    /**
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // View Declear
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_recipe_item, parent, false);
        }

        ImageView iconImageView = (ImageView) convertView.findViewById(R.id.iconItem);
        TextView mealName = (TextView) convertView.findViewById(R.id.dataName);
        TextView mealKCal = (TextView) convertView.findViewById(R.id.dataKcal);

        // setting each values
        RecipeListItem recipeItem = lvRecipe.get(position);
        iconImageView.setImageDrawable(recipeItem.getmIcon());
        mealName.setText(recipeItem.getMealName());
        mealKCal.setText(recipeItem.getMealKCal());

        return convertView;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return lvRecipe.get(position);
    }

    /**
     * Add items to Listview
     * @param icon
     * @param name
     * @param kcal
     */
    public void addItem(Drawable icon, String name, String kcal) {
        RecipeListItem item = new RecipeListItem();
        item.setmIcon(icon);
        item.setMealName(name);
        item.setMealKCal(kcal);
        lvRecipe.add(item);
    }
}
