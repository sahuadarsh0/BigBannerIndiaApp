package technited.minds.bigbannerindia;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ContactUs extends AppCompatActivity {
    TextView call, call2, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);


        call = findViewById(R.id.contact_us);
        call2 = findViewById(R.id.contact_us2);
        email = findViewById(R.id.email);

        Intent i = getIntent();

        email.setText(i.getStringExtra("email"));
        call.setText(i.getStringExtra("mobile"));
        call2.setText(i.getStringExtra("mobile2"));


        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + call.getText().toString()));
                startActivity(intent);
            }
        });

        call2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + call2.getText().toString()));
                startActivity(intent);
            }
        });


        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });


    }

    void sendEmail() {

        String[] TO = {email.getText().toString()};
        String[] NUM = {call.getText().toString()};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_PHONE_NUMBER, NUM);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Help me at");
        emailIntent.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ContactUs.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}