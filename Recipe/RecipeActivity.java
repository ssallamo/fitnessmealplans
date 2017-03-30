package nz.ac.cornell.fitnessmealplans.Recipe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import nz.ac.cornell.fitnessmealplans.DB.DaoCategory;
import nz.ac.cornell.fitnessmealplans.DB.DaoUser;
import nz.ac.cornell.fitnessmealplans.Models.Menu;
import nz.ac.cornell.fitnessmealplans.Models.RecipeListItem;
import nz.ac.cornell.fitnessmealplans.R;

/**
 * Meal Plans data what Database has display though grid view
 * Created by HJS on 2016-05-13.
 */
public class RecipeActivity extends AppCompatActivity {

    ListViewAdapter adapter;
    ListView listView ;
    private ArrayList<Menu> recipeList = new ArrayList<Menu>();

    private String userID = null;
    private String userCodeID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent intent = this.getIntent();
        setUserID(intent.getExtras().getString("userID"));
        //Fetch user's code ID from Database
        setUserCodeID(getUserCodeIdFromDb(getUserID()));

        //Create Adapter
        adapter = new ListViewAdapter();
        loadRecipeList();
        assignAdapter();

        //Setting up the adapter on the listView
        listView = (ListView)findViewById(R.id.lvRecipe);
        listView.setAdapter(adapter);

        //take locateion values and read that values from listView item and Link to searching site with keyword
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                RecipeListItem curItem = (RecipeListItem) adapter.getItem(position);
                String curData=curItem.getMealName();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/#q="+curData)));
            }

        });
    }

    /**
     * Fetch user CodeID from db
     * @param userId
     * @return codeID
     */
    public String getUserCodeIdFromDb(String userId){
        String codeId = null;
        DaoUser databaseAccess = DaoUser.getInstance(this);
        databaseAccess.open();
        codeId = databaseAccess.getUserCodeID(userId);
        databaseAccess.close();
        Log.d("getUserCodeIdFromDb", codeId);
        return codeId;
    }

    /**
     * Call DAO menu data.
     * Fetch menu list from db and Assign to ArrayList<menu>
     */
    private void loadRecipeList() {
        DaoCategory databaseAccess = DaoCategory.getInstance(this);
        databaseAccess.open();
        recipeList = databaseAccess.getRecipeList(getUserCodeID());
        databaseAccess.close();
    }

    /**
     * Assign to ListView
     */
    private void assignAdapter() {
        for(Menu aMenu : recipeList){
            adapter.addItem(ContextCompat.getDrawable(this, R.drawable.icon01), aMenu.getMenuName(), String.valueOf(aMenu.getCalories())+"Kcal");
        }
    }

    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getUserCodeID() { return userCodeID; }
    public void setUserCodeID(String userCodeID) { this.userCodeID = userCodeID; }
}
