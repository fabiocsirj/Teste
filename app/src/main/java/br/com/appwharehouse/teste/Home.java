package br.com.appwharehouse.teste;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class Home extends AppCompatActivity implements Fragment1.OnSelectedItem {
    private static Home instance;
    public static Home getInstance() { return instance; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        instance = this;

        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Fragment1 frag1 = new Fragment1();
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.placeHolder, frag1);
            ft.commit();
        }

        if (savedInstanceState == null) {
            ImagesOff.clearGraph(this);
            Toast.makeText(this, "Download...", Toast.LENGTH_LONG).show();
            Repository r = Repository.getInstance(); // Start the Singleton class
            r.getTicker(getApplication());

            TesteJogScheduler.start(this); // Start Job in Background
        }
    }

    @Override
    protected void onDestroy() {
        instance = null;
        super.onDestroy();
    }

    @Override
    public void onSelect(Coin coin) {
//        Log.i("Teste", "Clicou na posição: " + coin.getName());
        Fragment2 frag2;
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Bundle bundle = new Bundle();
            bundle.putSerializable("coin", coin);
            frag2 = new Fragment2();
            frag2.setArguments(bundle);
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.placeHolder, frag2);
            ft.addToBackStack(null);
            ft.commit();
        } else {
            frag2 = (Fragment2) getFragmentManager().findFragmentById(R.id.fragment2);
            frag2.update(coin);
        }
    }

}
