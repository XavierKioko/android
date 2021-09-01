package kioko.xavier.mdevstore.tools;


import android.content.Context;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class Api {
    public static String site = "https://grabnsnack.000webhostapp.com/server/";
    private Context context;
    private Snackbar bar; //

    public Api(Context context) {
        this.context = context;
    }

    private void get(String address, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(site + address)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        Log.d("test_uri", "Address: " + site + address);
    }


    private void post(String address, Map<String, String> params, Callback callback) {
        OkHttpClient client = new OkHttpClient();
        MultipartBody.Builder requestBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            requestBuilder.addFormDataPart(entry.getKey(), entry.getValue());
        }

        RequestBody requestBody = requestBuilder.build();

        Request request = new Request.Builder()
                .post(requestBody)
                .url(site + address)
                .build();

        Call call = client.newCall(request);
        call.enqueue(callback);
        Log.d("test_uri", "Address [POST]: " + site + address);
    }


    public void toast(String text) {
        Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        toast.show();
    }


    public void loading(boolean status, ProgressBar progressBar) {
        progressBar.setVisibility(status ? ProgressBar.VISIBLE : ProgressBar.INVISIBLE);
    }


    public void loadError(View view, View.OnClickListener onClickListener) {
        bar = Snackbar.make(view, "Loading error. Check your internet connection.", Snackbar.LENGTH_INDEFINITE);
        bar.setAction("Repeat", onClickListener);
        bar.show();
    }


    public void dismissLoadError() {
        if (bar != null && bar.isShown())
            bar.dismiss();
    }


    public void getNewProducts(Callback callback) {
        get("api/products/get_new", callback);
    }


    public void getProduct(int productID, Callback callback) {
        get("api/products/get/" + productID, callback);
    }


    public void getCategories(int parentCategory, Callback callback) {
        if (parentCategory == -1) {
            get("api/categories/get", callback);
        } else {
            get("api/categories/one/" + parentCategory, callback);
        }
    }


    public void getCategoryProducts(int categoryID, Callback callback) {
        get("api/products/get_category/" + categoryID, callback);
    }


    public void search(String query, Callback callback) {
        get("api/products/search?q=" + query, callback);
    }


    public void order(String name, String phone, JSONArray cart, Callback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("phone", phone);
        params.put("cart", cart.toString());

        post("api/orders/new", params, callback);
    }

    public void checkCart(JSONArray cart, Callback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("cart", cart.toString());

        post("api/cart/check", params, callback);
    }
}