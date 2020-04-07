package au.edu.unsw.infs3634.cryptobag;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3634.cryptobag.Entities.CoinService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import au.edu.unsw.infs3634.cryptobag.Entities.Coin;
import au.edu.unsw.infs3634.cryptobag.Entities.CoinLoreResponse;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private CoinAdapter mAdapter;
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (findViewById(R.id.detail_container) != null) {
            mTwoPane = true;
        }

        RecyclerView mRecyclerView = findViewById(R.id.rvList);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        Gson gson = new Gson();
//        CoinLoreResponse response = gson.fromJson(CoinLoreResponse.json, CoinLoreResponse.class);
//        List<Coin> coins = response.getData();

        mAdapter = new CoinAdapter(this, new ArrayList<Coin>(), mTwoPane);
        mRecyclerView.setAdapter(mAdapter);

        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.coinLore.net").addConverterFactory(GsonConverterFactory.create()).build();

        CoinService service = retrofit.create(CoinService.class);
        Call<CoinLoreResponse> coinsCall = service.getCoins();

        coinsCall.enqueue(new Callback<CoinLoreResponse>(){
            @Override
            public void onResponse(Call<CoinLoreResponse> call, Response<CoinLoreResponse> response){
                Log.d(TAG, "OnResponse: SUCCESS");
                List<Coin> coins = response.body().getData();
                ((CoinAdapter) mAdapter).setCoins(coins);
            }
            @Override
            public void onFailure(Call<CoinLoreResponse> call, Throwable t){
                Log.d(TAG, "onFailure: FAILURE");
            }
        });
    }
}
