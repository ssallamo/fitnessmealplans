package nz.ac.cornell.fitnessmealplans.Models;

import android.graphics.drawable.Drawable;

/**
 * This class is a contaner for recipes from dateabase
 * Created by HJS on 2016-05-15.
 */
public class RecipeListItem {

    private Drawable mIcon;
    private String mealCategory ;
    private String mealName ;
    private String mealKCal;
    private boolean mSelectable = true;

    public RecipeListItem() {
    }

    public RecipeListItem(Drawable icon, String obj01, String obj02,  String obj03) {
        mIcon = icon;
        mealCategory = obj01;
        mealName = obj02;
        mealKCal = obj03;
    }

    public Drawable getmIcon() {return mIcon;}
    public String getMealCategory() {return mealCategory;}
    public String getMealName() {return mealName;}
    public String getMealKCal() {return mealKCal;}
    public boolean ismSelectable() {return mSelectable;}
    public void setmIcon(Drawable mIcon) {this.mIcon = mIcon;}
    public void setMealCategory(String mealCategory) {this.mealCategory = mealCategory;}
    public void setMealName(String mealName) {this.mealName = mealName;}
    public void setMealKCal(String mealKCal) {this.mealKCal = mealKCal;}
    public void setmSelectable(boolean mSelectable) {this.mSelectable = mSelectable;}
    public void setSelectable(boolean selectable) {
        mSelectable = selectable;
    }

}
