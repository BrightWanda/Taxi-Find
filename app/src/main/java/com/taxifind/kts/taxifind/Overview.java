package com.taxifind.kts.taxifind;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.taxifind.kts.POJOs.Countries;
import com.taxifind.kts.POJOs.Country;
import com.taxifind.kts.POJOs.Province;
import com.taxifind.kts.POJOs.Value;
import com.taxifind.kts.taxifind.model.TreeNode;
import com.taxifind.kts.taxifind.view.AndroidTreeView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Overview extends AppCompatActivity{
    private AndroidTreeView tView;
    private ApiInterface apiInterface;
    private Countries countries;
    private ProgressBar spinner;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initToolBar();
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Countries> call = apiInterface.GetCountries();

        call.enqueue(new Callback<Countries>() {
            @Override
            public void onResponse(Call<Countries> call, Response<Countries> response) {
                spinner.setVisibility(View.GONE);
                countries = response.body();

                ViewGroup containerView = (ViewGroup) findViewById(R.id.container);

                List<Value> amazwe = countries.getValue();
                TreeNode root = TreeNode.root();

                for (Value izwe:amazwe) {
                    TreeNode countryRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, izwe.getName() )) ;

                    /*TreeNode mEC = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Eastern Cape"));
                    TreeNode mPE = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Port Elizabeth"));
                    TreeNode mdatsane = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_drive_file, "Mdatsane"));
                    TreeNode bhayi = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_drive_file, "Bhayi"));
                    TreeNode grahamstown = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_drive_file, "Grahams Town"));
                    fillDownloadsFolder(mPE);
                    mPE.addChildren(mdatsane, bhayi, grahamstown);

                    TreeNode myMedia = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo_library, "Photos"));
                    TreeNode photo1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo, "Folder 1"));
                    TreeNode photo2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo, "Folder 2"));
                    TreeNode photo3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo, "Folder 3"));
                    myMedia.addChildren(photo1, photo2, photo3);

                    mEC.addChild(mPE);
                    countryRoot.addChildren(mEC, myMedia);*/

                    root.addChildren(countryRoot);
                }

                tView = new AndroidTreeView(getApplicationContext(), root);
                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                tView.setDefaultViewHolder(IconTreeItemHolder.class);
                tView.setDefaultNodeClickListener(nodeClickListener);
                tView.setDefaultNodeLongClickListener(nodeLongClickListener);

                containerView.addView(tView.getView());
                    /*spinner.setVisibility(View.GONE);
                    Intent intent = new Intent(Overview.this, ChooseRank.class);
                    intent.putExtra(EXTRA_MESSAGE, distance);
                    intent.putExtra(LONG, longitude +"");
                    intent.putExtra(LAT, latitude + "");
                    startActivity(intent);*/
            }

            @Override
            public void onFailure(Call<Countries> call, Throwable t) {
                spinner.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
            }
        });



        if (savedInstanceState != null) {
            String state = savedInstanceState.getString("tState");
            if (!TextUtils.isEmpty(state)) {
                tView.restoreState(state);
            }
        }
        //setHasOptionsMenu(true);
    }

    public void initToolBar()
    {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.toolbarTitle);

        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_menu_black_18dp);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Overview.this, AddTaxiRank.class);
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.expandAll:
                tView.expandAll();
                break;

            case R.id.collapseAll:
                tView.collapseAll();
                break;
        }
        return true;
    }

    private int counter = 0;

    private void fillDownloadsFolder(TreeNode node) {
        TreeNode downloads = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Downloads" + (counter++)));
        node.addChild(downloads);
        if (counter < 5) {
            fillDownloadsFolder(downloads);
        }
    }

    private TreeNode.TreeNodeClickListener nodeClickListener = new TreeNode.TreeNodeClickListener() {
        @Override
        public void onClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;

            spinner.setVisibility(View.VISIBLE);

            apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

            Call<Province> call = apiInterface.GetProvinces();

            call.enqueue(new Callback<Province>() {
                @Override
                public void onResponse(Call<Province> call, Response<Province> response) {
                    spinner.setVisibility(View.GONE);
                    province = response.body();

                    ViewGroup containerView = (ViewGroup) findViewById(R.id.container);

                    List<Value> amazwe = countries.getValue();
                    TreeNode root = TreeNode.root();

                    for (Value izwe : amazwe) {
                        TreeNode countryRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, izwe.getName()));
                    }
                }

                @Override
                public void onFailure(Call<Province> call, Throwable t) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                }
            });

            TreeNode mEC = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Eastern Cape"));
            TreeNode mPE = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, "Port Elizabeth"));
            TreeNode mdatsane = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_drive_file, "Mdatsane"));
            TreeNode bhayi = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_drive_file, "Bhayi"));
            TreeNode grahamstown = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_drive_file, "Grahams Town"));
            fillDownloadsFolder(mPE);
            mPE.addChildren(mdatsane, bhayi, grahamstown);

            TreeNode myMedia = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo_library, "Photos"));
            TreeNode photo1 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo, "Folder 1"));
            TreeNode photo2 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo, "Folder 2"));
            TreeNode photo3 = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_photo, "Folder 3"));
            myMedia.addChildren(photo1, photo2, photo3);

            mEC.addChild(mPE);
            node.addChildren(mEC, myMedia);

            //Toast.makeText(getApplicationContext(), item.text, Toast.LENGTH_LONG).show();
        }
    };

    private TreeNode.TreeNodeLongClickListener nodeLongClickListener = new TreeNode.TreeNodeLongClickListener() {
        @Override
        public boolean onLongClick(TreeNode node, Object value) {
            IconTreeItemHolder.IconTreeItem item = (IconTreeItemHolder.IconTreeItem) value;
            Toast.makeText(Overview.this, "Long click: " + item.text, Toast.LENGTH_SHORT).show();
            return true;
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("tState", tView.getSaveState());
    }
}
