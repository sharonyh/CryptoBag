package au.edu.unsw.infs3634.cryptobag;

import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.edu.unsw.infs3634.cryptobag.Entities.Coin;
import au.edu.unsw.infs3634.cryptobag.Entities.CoinLoreResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private boolean mTwoPane;
    private CoinAdapter mAdapter;
    private String TAG = "MainActivity";
    private CoinDatabase mDb;

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
        mAdapter = new CoinAdapter(this, new ArrayList<Coin>(), mTwoPane);
        mRecyclerView.setAdapter(mAdapter);


        //build Database
        mDb = Room.databaseBuilder(getApplicationContext(), CoinDatabase.class, "coin-database").build();

        //execute AsyncTask
        new GetCoinDBTask().execute();
        new GetCoinTask().execute();
    }

    private class GetCoinTask extends AsyncTask<Void, Void, List<Coin>> {
        @Override
        protected List<Coin> doInBackground(Void... voids){
            try{

                Log.d(TAG, "doInBackground: SUCCESS");
                //create Retrofit instance & parse the retrieved JSON using GSON deserializer
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.coinlore.com")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                //get service & call object for the request
                CoinService service = retrofit.create(CoinService.class);
                Call<CoinLoreResponse> coinsCall = service.getCoins();

                Response<CoinLoreResponse> coinResponse = coinsCall.execute();
                List<Coin> coins = coinResponse.body().getData();


                mDb.coinDao().deleteAll(mDb.coinDao().getCoins().toArray(new Coin[mDb.coinDao().getCoins().size()]));


                mDb.coinDao().insertAll(coins.toArray(new Coin[coins.size()]));

                return coins;
            }

            catch(IOException e){
                Log.d(TAG, "onFailure: FAILURE");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Coin> coins){
            Log.d(TAG, "onPostExecute: SUCCESS");
            mAdapter.setCoins(coins);

        }



    }

    private class GetCoinDBTask extends AsyncTask<Void, Void, List<Coin>> {
        @Override
        protected List<Coin> doInBackground(Void... voids) {
            return mDb.coinDao().getCoins();
        }

        @Override
        protected void onPostExecute(List<Coin> coins) {
            mAdapter.setCoins(coins);
        }
    }
}

