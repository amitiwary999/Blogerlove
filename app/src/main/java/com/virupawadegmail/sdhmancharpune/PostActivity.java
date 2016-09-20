package com.virupawadegmail.sdhmancharpune;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class PostActivity extends AppCompatActivity {
    private ImageButton mSelectImage;
    private EditText titleField;
    private EditText mdesc;
    private Button buttondone;
    private ProgressDialog mProgress;
    private Uri mImageUri = null;
    private StorageReference mStorage;
    private DatabaseReference mDatabase;


    private static final int GALLERY_REQUEST = 1;

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activitypost);

            EditText titleField = (EditText) findViewById(R.id.titleField);
            EditText mdesc = (EditText) findViewById(R.id.mdesc);
            Button buttondone = (Button) findViewById(R.id.buttondone);
            mDatabase=FirebaseDatabase.getInstance().getReference().child("Blog");
          ImageButton mSelectImage=(ImageButton) findViewById(R.id.mSelectImage);




           mStorage = FirebaseStorage.getInstance().getReference();
            mProgress = new ProgressDialog(this);



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
                   startPosting();
                }
            });
        }

    private void startPosting() {
        mProgress.setMessage("Posting to Blog....");

         final String title_val = titleField.getText().toString().trim();
         final String desc_val = mdesc.getText().toString().trim();
        if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(desc_val) && mImageUri != null){
            StorageReference filepath = mStorage.child("Blog_Images").child(mImageUri.getLastPathSegment());
            filepath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    DatabaseReference newPost=mDatabase.push();


                    newPost.child("title").setValue(title_val);
                    newPost.child("desc").setValue(desc_val);
                    newPost.child("image").setValue(downloadUrl.toString());


                    mProgress.dismiss();
                    startActivity(new Intent(PostActivity.this,MainActivity.class));


                }
            });

        }
        }











    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            Uri uri = data.getData();

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                ImageView imageView = (ImageView) findViewById(R.id.mSelectImage);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}




