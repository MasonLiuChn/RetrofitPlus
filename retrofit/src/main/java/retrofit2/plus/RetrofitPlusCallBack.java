package retrofit2.plus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by liumeng on 3/21/16.
 */
public abstract class RetrofitPlusCallBack<T> implements Callback<T> {
    @Deprecated
    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.code() >= 200 && response.code() < 300) {
            onHttpSuccess(call, response);
        } else {
            onHttpFailure(call, response);
        }
    }

    @Deprecated
    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onNetFailure(call, t);
    }

    public abstract void onHttpSuccess(Call<T> call, Response<T> response);

    public abstract void onHttpFailure(Call<T> call, Response<T> response);

    public abstract void onNetFailure(Call<T> call, Throwable t);
}
