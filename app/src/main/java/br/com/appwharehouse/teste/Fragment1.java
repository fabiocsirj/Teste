package br.com.appwharehouse.teste;

import android.app.Fragment;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment1 extends Fragment implements TesteAdapter.ItemClickListener {
    private RecyclerView recyclerView = null;
    private OnSelectedItem onSelectedItem;
    private CoinViewModel coinViewModel;
    private Observer<List<Coin>> onCoinsUpdate;

    public interface OnSelectedItem {
        void onSelect(Coin coin);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSelectedItem) {
            onSelectedItem = (OnSelectedItem) context;
        }
    }

    @Override
    public void onDetach() {
        onSelectedItem = null;
        super.onDetach();
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
        coinViewModel = ViewModelProviders.of((FragmentActivity) getActivity()).get(CoinViewModel.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        onCoinsUpdate = new Observer<List<Coin>>() {
            @Override
            public void onChanged(@Nullable final List<Coin> lCoins) {
                update(lCoins);
            }
        };
        coinViewModel.getAllCoins().observe((LifecycleOwner) getActivity(), onCoinsUpdate);
    }

    @Override
    public void onStop() {
        coinViewModel.getAllCoins().removeObserver(onCoinsUpdate);
        super.onStop();
    }

    @Override
    public void onItemClick(Coin coin) {
        if (onSelectedItem != null) {
            onSelectedItem.onSelect(coin);
        }
    }

    public void update(List<Coin> lCoins) {
        Map<Integer, Bitmap> icons = new HashMap<>();
        Bitmap iconDefault = BitmapFactory.decodeResource(getResources(), R.drawable.coin_of_dollar);
        ArrayList<Integer> idImageNotFound = new ArrayList<>();
        for(int i=0; i<lCoins.size(); i++) {
            int id = lCoins.get(i).getId();
            String fileName = String.valueOf(id) + ".png";
            Bitmap icon = ImagesOff.openBitmap(getActivity(), fileName);
            if (icon != null) icons.put(id, icon);
            else {
                icons.put(id, iconDefault);
                idImageNotFound.add(id);
            }
        }

        if (recyclerView != null) updateRecyclerView(lCoins, icons);

        if (idImageNotFound.size() > 0) {
            Integer[] ids = new Integer[idImageNotFound.size()];
            idImageNotFound.toArray(ids);
            Repository r = Repository.getInstance(); // Singleton class, aqui já deve estar instanciada
            r.getIcons(ids);
        }
    }

    public void updateRecyclerView(List<Coin> lCoins, Map<Integer, Bitmap> icons) {
        LinearLayoutManager llm = new LinearLayoutManager(recyclerView.getContext());
        recyclerView.setLayoutManager(llm);
        TesteAdapter adapter = new TesteAdapter(recyclerView.getContext(), lCoins, icons);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), llm.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

}
