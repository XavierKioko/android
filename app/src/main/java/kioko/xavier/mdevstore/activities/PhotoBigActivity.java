package kioko.xavier.mdevstore.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import kioko.xavier.mdevstore.R;
import kioko.xavier.mdevstore.base.BaseToolBarActivity;

public class PhotoBigActivity extends BaseToolBarActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_photo_big;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageView image = findViewById(R.id.photoBig);

        String imageURL = getIntent().getStringExtra("image");
        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.ic_menu_gallery)
                .error(R.drawable.ic_menu_camera)
                .into(image);

        setTitle("Image preview");

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
