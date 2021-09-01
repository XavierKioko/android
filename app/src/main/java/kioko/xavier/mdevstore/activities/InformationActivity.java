package kioko.xavier.mdevstore.activities;

import android.os.Bundle;
import android.view.MenuItem;

import kioko.xavier.mdevstore.R;
import kioko.xavier.mdevstore.base.BaseToolBarActivity;

public class InformationActivity extends BaseToolBarActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_information;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("About the app");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
