package com.taxifind.kts.base;

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
import com.taxifind.kts.POJOs.Municipalities;
import com.taxifind.kts.POJOs.Provinces;
import com.taxifind.kts.POJOs.Value;
import com.taxifind.kts.POJOs.ValueMun;
import com.taxifind.kts.POJOs.ValueProv;
import com.taxifind.kts.taxifind.model.TreeNode;
import com.taxifind.kts.taxifind.view.AndroidTreeView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Overview extends AppCompatActivity{
    private AndroidTreeView tView;
    private ApiInterface apiInterface;
    private Countries countries;
    private ProgressBar spinner;
    private Provinces provinces;
    private Municipalities municipalities;
    private static int level = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        initToolBar();
        spinner=(ProgressBar)findViewById(R.id.progressBar);
        spinner.setVisibility(View.VISIBLE);

        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

        Call<Countries> call = apiInterface.getCountries();

        //Test

        call.enqueue(new Callback<Countries>() {
            @Override
            public void onResponse(Call<Countries> call, Response<Countries> response) {
                spinner.setVisibility(View.GONE);
                countries = response.body();

                ViewGroup containerView = (ViewGroup) findViewById(R.id.container);

                List<Value> amazwe = countries.getValue();
                TreeNode root = TreeNode.root();

                for (Value izwe : amazwe) {
                    TreeNode countryRoot = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_laptop, izwe.getName()));
                    root.addChildren(countryRoot);
                }

                tView = new AndroidTreeView(getApplicationContext(), root);
                tView.setDefaultAnimation(true);
                tView.setDefaultContainerStyle(R.style.TreeNodeStyleCustom);
                tView.setDefaultViewHolder(IconTreeItemHolder.class);
                tView.setDefaultNodeClickListener(nodeClickListener);
                tView.setDefaultNodeLongClickListener(nodeLongClickListener);

                containerView.addView(tView.getView());
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
            if(level == 0)
            {
                Toast.makeText(getApplicationContext(), "Level " + level, Toast.LENGTH_LONG).show();;
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<Provinces> call = apiInterface.getProvinces(0);

                call.enqueue(new Callback<Provinces>() {
                    @Override
                    public void onResponse(Call<Provinces> call, Response<Provinces> response) {
                        Toast.makeText(getApplicationContext(), "Wait Kanti", Toast.LENGTH_LONG).show();
                        provinces = response.body();
                    }

                    @Override
                    public void onFailure(Call<Provinces> call, Throwable t) {
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                if(provinces != null) {
                    Toast.makeText(getApplicationContext(), "Iyaxaka lento", Toast.LENGTH_LONG).show();
                        spinner.setVisibility(View.GONE);
                        List<ValueProv> provs = provinces.getValueProv();
                        for (ValueProv prov : provs) {
                            TreeNode province = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, prov.getName()));
                            fillDownloadsFolder(province);
                            node.addChildren(province);
                            level++;
                        }

                }
            }
            else if(level == 1)
            {
                Toast.makeText(getApplicationContext(), "Level " + level, Toast.LENGTH_LONG).show();;
                apiInterface = ApiClient.getApiClient().create(ApiInterface.class);

                Call<Municipalities> call = apiInterface.getMunicipalities(0);

                call.enqueue(new Callback<Municipalities>() {
                    @Override
                    public void onResponse(Call<Municipalities> call, Response<Municipalities> response) {
                        spinner.setVisibility(View.GONE);
                        municipalities = response.body();
                    }

                    @Override
                    public void onFailure(Call<Municipalities> call, Throwable t) {
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(), t.toString(), Toast.LENGTH_LONG).show();
                    }
                });

                if(municipalities != null) {
                    List<ValueMun> muns = municipalities.getValueMun();
                    for (ValueMun mun : muns) {
                        TreeNode municipalities = new TreeNode(new IconTreeItemHolder.IconTreeItem(R.string.ic_folder, mun.getName()));
                        fillDownloadsFolder(municipalities);
                        node.addChildren(municipalities);
                        level++;
                    }
                }
            }
            else{
                Toast.makeText(getApplicationContext(), "Level " + level, Toast.LENGTH_LONG).show();
            }
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
