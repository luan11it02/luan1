package com.app.lfc.scooter.Activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.app.lfc.scooter.Adapter.PostAdapter;
import com.app.lfc.scooter.Model.Post;
import com.app.lfc.scooter.R;
import com.app.lfc.scooter.Util.Suggestion;
import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import jp.wasabeef.recyclerview.animators.SlideInUpAnimator;

public class SearchActivity extends AppCompatActivity {

    FloatingSearchView searchView;
    RecyclerView recyclerPost;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Post> listPost = new ArrayList<>();
    private List<Suggestion> mSuggestions =new ArrayList<>();
    PostAdapter adapterPost;
    DatabaseReference database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initView();
        initData();
        adapterPost = new PostAdapter(this, listPost);

        layoutManager = new LinearLayoutManager(this);
        recyclerPost.setLayoutManager(layoutManager);
        recyclerPost.setItemAnimator(new SlideInUpAnimator());
        getDataPost();


        searchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {
                List<Post> filtermodelistPost = filterPost(listPost, newQuery);
                adapterPost.setfilter(filtermodelistPost);
                recyclerPost.setAdapter(adapterPost);
                if (!oldQuery.equals("") && newQuery.equals("")) {
                    searchView.clearSuggestions();
                } else {
                    searchView.showProgress();
                    searchView.swapSuggestions(getSuggestion(newQuery));
                    searchView.hideProgress();
                }
            }
        });

        searchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {
                searchView.showProgress();
                searchView.swapSuggestions(getSuggestion(searchView.getQuery()));
                searchView.hideProgress();
            }
            @Override
            public void onFocusCleared() {
            }
        });
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                Suggestion suggestion= (Suggestion) searchSuggestion;
                Toast.makeText(getApplicationContext(),"Ban vua chon "+suggestion.getBody(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSearchAction(String currentQuery) {

            }
        });

        searchView.setOnLeftMenuClickListener(new FloatingSearchView.OnLeftMenuClickListener() {
            @Override
            public void onMenuOpened() {

            }

            @Override
            public void onMenuClosed() {

            }
        });


    }

    private List<Suggestion> getSuggestion(String query){
        query = covertStringToURL(query);
        List<Suggestion> suggestions=new ArrayList<>();
        for(Suggestion suggestion: mSuggestions){
            if(covertStringToURL(suggestion.getBody()).contains(query)){
                suggestions.add(suggestion);
            }
        }
        return suggestions;
    }


    private static List<Post> filterPost(List<Post> list, String query){
        query = covertStringToURL(query);
        final List<Post> fiterModeList = new ArrayList<>();
        if (query.length() > 0) {
            for (Post model : list) {
                final String text = covertStringToURL(model.getTitle());
                if (text.contains(query)) {
                    fiterModeList.add(model);
                }
            }
        } else fiterModeList.clear();
        return fiterModeList;
    }


    private static String covertStringToURL(String str) {
        try {
            String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            return pattern.matcher(temp).replaceAll("").toLowerCase().replaceAll(" ", "-").replaceAll("đ", "d");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void getDataPost(){
        database = FirebaseDatabase.getInstance().getReference("baiviet");
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listPost.clear();
                for (int i = (int) (dataSnapshot.getChildrenCount()-1); i >= 0; i--){
                    Post post = dataSnapshot.child(String.valueOf(i)).getValue(Post.class);
                    post.setId(i);
                    listPost.add(post);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void initData(){
        mSuggestions.add(new Suggestion("lai xe"));
        mSuggestions.add(new Suggestion("tai nan"));
        mSuggestions.add(new Suggestion("toc do"));
        mSuggestions.add(new Suggestion("chat luong"));
        mSuggestions.add(new Suggestion("sh mode"));
        mSuggestions.add(new Suggestion("air blade"));
        mSuggestions.add(new Suggestion("rung dau xe"));
        mSuggestions.add(new Suggestion("keu re re"));
    }

    private void initView() {
        searchView      = findViewById(R.id.searchview);
        recyclerPost    = findViewById(R.id.list_post_search);
    }
}
