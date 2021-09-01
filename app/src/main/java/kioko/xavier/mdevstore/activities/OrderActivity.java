package kioko.xavier.mdevstore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import kioko.xavier.mdevstore.R;
import kioko.xavier.mdevstore.base.BaseToolBarActivity;
import kioko.xavier.mdevstore.tools.Api;
import kioko.xavier.mdevstore.tools.Shop;

public class OrderActivity extends BaseToolBarActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Order");

        Button orderButton = findViewById(R.id.orderButtonSend);

        orderButton.setOnClickListener(v -> {
            Api api = new Api(getApplicationContext());
            Shop shop = new Shop(getApplicationContext());
            EditText nameEdit = findViewById(R.id.orderPersonName);
            EditText phoneEdit = findViewById(R.id.orderPersonPhone);
            String name = nameEdit.getText()
                    .toString()
                    .trim();
            String phone = phoneEdit.getText()
                    .toString()
                    .trim();


            boolean isEmpty = false;
            if (name.isEmpty()) {
                nameEdit.setError("Enter your name");
                isEmpty = true;
            }
            if (phone.isEmpty()) {
                phoneEdit.setError("Enter your phone number");
                isEmpty = true;
            }
            if (isEmpty) return;

            api.loading(true, findViewById(R.id.orderProgressBar));

            api.order(name, phone, shop.getCart(), new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> {
                        api.loading(false, findViewById(R.id.orderProgressBar));
                        AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                        builder.setTitle("Error")
                                .setMessage("Checkout error, please try later")
                                .setCancelable(false)
                                .setNegativeButton("OK",
                                        (dialog, id) -> dialog.cancel());
                        AlertDialog alert = builder.create();
                        alert.show();
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    runOnUiThread(() -> {
                        api.loading(false, findViewById(R.id.orderProgressBar));
                        try {
                            String resp = response.body().string();
                            if (resp.equals("ok")) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                                builder.setTitle("Order is processed")
                                        .setMessage("Your order has been completed, the manager will contact you soon")
                                        .setCancelable(false)
                                        .setNegativeButton("OK",
                                                (dialog, id) -> {
                                                    dialog.cancel();
                                                    Intent i = new Intent(OrderActivity.this, MainActivity.class);
                                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                    startActivity(i);
                                                    shop.clearCart();
                                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                                builder.setTitle("Error")
                                        .setMessage("Checkout error, please try later")
                                        .setCancelable(false)
                                        .setNegativeButton("OK",
                                                (dialog, id) -> dialog.cancel());
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } catch (Exception e) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OrderActivity.this);
                            builder.setTitle("Error")
                                    .setMessage("Checkout error, please try later")
                                    .setCancelable(false)
                                    .setNegativeButton("OK",
                                            (dialog, id) -> dialog.cancel());
                            AlertDialog alert = builder.create();
                            alert.show();
                        }
                    });
                }
            });

        });
    }
}
