package com.virupawadegmail.sdhmancharpune;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.w3c.dom.Text;

public class Post_Activity extends AppCompatActivity {
    private ImageButton mSelectImage;
    private EditText titleField;
    private EditText mdesc;
    private Button buttondone;
    private ProgressDialog mProgress;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseError mDatabase;


    private static final int GALLERY_REQUEST = 1;

    public Post_Activity() {
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        EditText titleField = (EditText) findViewById(R.id.titleField);
        EditText mdesc = (EditText) findViewById(R.id.mdesc);
        Button buttondone = (Button) findViewById(R.id.buttondone);


        mStorage = FirebaseStorage.getInstance().getReference();
        mProgress = new ProgressDialog(this);


        ImageButton mSelectImage = (ImageButton) findViewById(R.id.mSelectImage);
        mSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, GALLERY_REQUEST);

            }
        });
        buttondone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startposting();
            }
        });
    }

    private void startposting() {
        String title_val = titleField.getText().toString().trim();
        String desc_val = mdesc.getText().toString().trim();
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null) {
            StorageReference filepath = mStorage.child("Blog_Image").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();

                }
            });

        }


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_REQUEST&&requestCode==RESULT_OK){
            mImageUri =data.getData();
            mSelectImage.setImageURI(mImageUri);
        }
    }

}
