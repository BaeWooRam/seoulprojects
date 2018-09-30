package com.example.myapplication.fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.News_JSONTaskAndParsor;
import com.example.myapplication.R;
import com.example.myapplication.AddPostActivity;
import com.example.myapplication.PostContentActivity;
import com.example.myapplication.adapter.RecyclerAdapter;
import com.example.myapplication.adapter.RecyclerItemClickListener;
import com.example.myapplication.bin.InfoBoardBin;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Container_NewsFragment extends Fragment implements View.OnClickListener{

    public static final String INTENT_QUERY = "query";


    private View mLayout;
    public RecyclerAdapter adapter;
    RecyclerView recyclerView;

    String search_content="";
    ImageButton search_btn;
    EditText search_text;
    ArrayList<InfoBoardBin> newslist;
    News_JSONTaskAndParsor newsJson;

    public Container_NewsFragment() {
        // Required empty public constructor
    }
    public static Container_NewsFragment newInstance() {
        Container_NewsFragment fragment = new Container_NewsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        newslist = new ArrayList<InfoBoardBin>();

        mLayout= inflater.inflate(R.layout.fragment_container__news,container,false);

        search_btn=(ImageButton)mLayout.findViewById(R.id.search_btn);
        search_text=(EditText)mLayout.findViewById(R.id.search_text);

        mLayout.findViewById(R.id.search_btn).setOnClickListener(this);
        mLayout.findViewById(R.id.add_post).setOnClickListener(this);
        mLayout.findViewById(R.id.search_filter).setOnClickListener(this);

        recyclerView = mLayout.findViewById(R.id.recycler_view);
        adapter = new RecyclerAdapter();
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setAdapter(adapter);

        newsJson = new News_JSONTaskAndParsor(recyclerView,newslist,adapter);

        newsJson.execute(News_JSONTaskAndParsor.INFOBOARD_ALL_URL);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(mLayout.getContext(),recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        GoToActivity(PostContentActivity.class,((TextView)view.findViewById(R.id.num)).getText().toString());
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {
                    }
                })
        );

        return mLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.search_btn:
                search_content=search_text.getText().toString();
                if(search_content.equals("") || search_content.equals(" ")) {
                    Toast.makeText(mLayout.getContext(), "검색어 입력을 해주세요", Toast.LENGTH_SHORT).show();
                }else{
                    newsJson = new News_JSONTaskAndParsor(recyclerView,newslist,adapter);
                    newsJson.execute(News_JSONTaskAndParsor.INFOBOARD_SEARCH_URL+search_content);
                }
                break;
            case R.id.add_post:
                Toast.makeText(mLayout.getContext(),"테스트 아이템 클릭",Toast.LENGTH_SHORT).show();
                GoToActivity(AddPostActivity.class, "");
                break;
            case R.id.search_filter:
                final List<String> ListItems = new ArrayList<>();
                ListItems.add("음악");
                ListItems.add("미술");
                ListItems.add("공예");
                ListItems.add("연극");
                ListItems.add("사진");
                ListItems.add("전체보기");
                final CharSequence[] items = ListItems.toArray(new String[ ListItems.size()]);

                final List SelectedItems = new ArrayList();
                int defaultItem =0;
                SelectedItems.add(defaultItem);

                AlertDialog.Builder builder = new AlertDialog.Builder(mLayout.getContext());
                builder.setTitle("필터");
                builder.setSingleChoiceItems(items, defaultItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelectedItems.clear();
                        SelectedItems.add(which);
                    }
                });
                builder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if(!SelectedItems.isEmpty()){
                                    int index = (int) SelectedItems.get(0);

                                    if(index==5){
                                        newsJson = new News_JSONTaskAndParsor(recyclerView,newslist,adapter);
                                        newsJson.execute(News_JSONTaskAndParsor.INFOBOARD_ALL_URL);
                                        Toast.makeText(mLayout.getContext(),
                                                "ALL 선택했습니다." , Toast.LENGTH_LONG)
                                                .show();
                                    }else{
                                        newsJson = new News_JSONTaskAndParsor(recyclerView,newslist,adapter);
                                        newsJson.execute(News_JSONTaskAndParsor.INFOBOARD_CATEGORY_URL+index);
                                        Toast.makeText(mLayout.getContext(),
                                                index+"을 선택했습니다." , Toast.LENGTH_LONG)
                                                .show();
                                    }

                                }
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();

                break;
        }
    }
    private void GoToActivity(Class gotoActivity,String query){
        Intent intent = new Intent(mLayout.getContext(), gotoActivity);
        intent.putExtra(INTENT_QUERY,query);
        startActivity(intent);
    }
}
