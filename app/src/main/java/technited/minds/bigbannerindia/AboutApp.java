package technited.minds.bigbannerindia;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutApp extends AppCompatActivity {
    TextView about;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_app);

        Intent i = getIntent();
        about = findViewById(R.id.about);

        about.setText(Html.fromHtml(i.getStringExtra("about")));
    }

}