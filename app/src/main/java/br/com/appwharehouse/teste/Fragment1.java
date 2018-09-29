package br.com.appwharehouse.teste;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;

import java.util.Map;

public class Fragment1 extends Fragment implements TesteAdapter.ItemClickListener {
    private RecyclerView recyclerView = null;
    private TesteAdapter adapter;
    private OnSelectedItem onSelectedItem;
    private JSONArray cache = null;
    private Map<Integer, Bitmap> icons;

    public interface OnSelectedItem {
        void onSelect(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity a = (Activity) context;
        if (a instanceof OnSelectedItem) {
            onSelectedItem = (OnSelectedItem) a;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        if (cache != null) updateRecyclerView();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (onSelectedItem != null) {
            onSelectedItem.onSelect(position);
        }
    }

    public void update(JSONArray coins, Map<Integer, Bitmap> map) {
        cache = coins;
        icons = map;
        if (recyclerView != null) updateRecyclerView();
    }

    public void updateRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(llm);
        adapter = new TesteAdapter(recyclerView.getContext(), cache, icons);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
