package nz.ac.cornell.fitnessmealplans.Models;

import java.util.ArrayList;

public class Category {
    private String categoryId;
    private String categoryName;
    private ArrayList<Menu> menuList = new ArrayList<Menu>();

    public Category(String categoryId, String categoryName, ArrayList<Menu> menuList) {
        super();
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.menuList = menuList;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public ArrayList<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList() {
        this.menuList = menuList;
    }
}
