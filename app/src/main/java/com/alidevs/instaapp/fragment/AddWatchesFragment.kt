package com.alidevs.instaapp.fragment

import android.app.Activity
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.alidevs.instaapp.R
import com.alidevs.instaapp.utils.AppPreferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.theartofdev.edmodo.cropper.CropImage
import kotlinx.android.synthetic.main.fragment_add_watches.*
import java.io.File


class AddWatchesFragment : AppCompatActivity() {

    private val TAG = "AddWatchesFragment"

    private lateinit var storageReference: StorageReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var compressedImageFile: Bitmap
    private lateinit var user_id: String
    private lateinit var utils: AppPreferences

    var SELECT_PICTURES = 1

    private var mArrayUri = ArrayList<Uri>()
    private var imageUri: Uri? = null
    private var up = 0
    private var k = 0

    private lateinit var primaryImageURI: Uri
    private lateinit var firstImageURI: Uri
    private lateinit var secondImageURI: Uri
    private lateinit var thirdImageURI: Uri
    private var primary = false
    private var first = false
    private var second = false
    private var third = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_add_watches)

        //Set Action Bar
        val actionBar = supportActionBar
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow)
        actionBar?.elevation = 4.0F
        actionBar?.setDisplayShowHomeEnabled(true)

        //Init components
        firebaseAuth = FirebaseAuth.getInstance()
        user_id = firebaseAuth.currentUser!!.uid
        firestore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference
        utils = AppPreferences(this)


        img_primary.setOnClickListener {
            primary = true
            selectImage()
        }

        img_one.setOnClickListener {
            first = true
            selectImage()
        }
        img_two.setOnClickListener {
            second = true
            selectImage()
        }
        img_three.setOnClickListener {
            third = true
            selectImage()
        }

        btn_save.setOnClickListener {
            progressBar2.visibility = View.VISIBLE
            var imgurl = ArrayList<String>()
            val filepath = FirebaseStorage.getInstance().reference.child("watches_images")

            while (up < mArrayUri.size) {
                val ref = storageReference.child("watches_post_images").child(mArrayUri[k].lastPathSegment)
                val filepath = ref.putFile(mArrayUri[k])
                filepath.addOnSuccessListener { task ->
                    ref.downloadUrl.addOnSuccessListener { downloadPhotoUrl ->
                        imgurl.add(downloadPhotoUrl.toString())
                        Toast.makeText(this, downloadPhotoUrl.toString(), Toast.LENGTH_SHORT).show()
                        Log.d("#IMAGEURL", "->${imgurl.size}")
                    }
                }
                val items = HashMap<String, Any>()
                items["image_url_primary"] = imgurl[k]
                up++
                k++
            }

           /* if (edi_brand.text.toString() != "" && edt_model.text.toString() != "" && edt_ref.text.toString() != "" &&
                edt_serial.text.toString() != "" && edt_date.text.toString() != "" && edt_comment.text.toString() != ""
            ) {
                val items = HashMap<String, Any>()
                items["image_url_primary"] = mArrayUri[0]
                items["image_url_first"] = mArrayUri[1]
                items["image_url_second"] = mArrayUri[2]
                items["image_url_third"] = mArrayUri[3]
                items["brand_name"] = edi_brand.text.toString()
                items["model"] = edt_model.text.toString()
                items["reference"] = edt_ref.text.toString()
                items["serial"] = edt_serial.text.toString()
                items["purchase_date"] = edt_date.text.toString()
                items["comments"] = edt_comment.text.toString()

                firestore.collection("MyWatches").document(user_id).set(items).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        progressBar2.visibility = View.GONE
                        Toast.makeText(this, "Post was added", Toast.LENGTH_LONG).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(this, "FireStore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "All Fields are mandatory to fill", Toast.LENGTH_LONG).show()
            }*/

            /*  firestore.collection("MyWatches").add(items)
                  .addOnCompleteListener { task ->
                      if (task.isSuccessful) {
                          progressBar2.visibility = View.GONE
                          Toast.makeText(this, "Post was added", Toast.LENGTH_LONG).show()
                      }
                  }.addOnFailureListener {
                      Toast.makeText(this, "FireStore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                  }
          } else {
              Toast.makeText(this, "All Fields are mandatory to fill", Toast.LENGTH_LONG).show()
          }*/


        }
        /* val filepath = FirebaseStorage.getInstance().reference.child("gpic");

         while (up < mArrayUri.size) {
             val randonName = UUID.randomUUID().toString()
             val primaryImgage = File(mArrayUri[k].path)
             //Primary Image
             try {
                 compressedImageFile = Compressor(this)
                     .setMaxHeight(720)
                     .setMaxWidth(720)
                     .setQuality(50)
                     .compressToBitmap(primaryImgage)
             } catch (e: IOException) {
                 e.printStackTrace()
             }
             val byte = ByteArrayOutputStream()
             compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
             val imagedata = byte.toByteArray()
             val ref = storageReference.child("watches_post_images").child("$randonName.jpg")
             val filepath = ref.putBytes(imagedata)
             filepath.addOnFailureListener {
                 Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
             }.addOnSuccessListener {
                 val downloadurl = ref.downloadUrl
                 if (it.task.isSuccessful) {
                     val newthumbfile = File(primaryImgage.path)
                     try {
                         compressedImageFile = Compressor(this)
                             .setMaxHeight(100)
                             .setMaxWidth(100)
                             .setQuality(1)
                             .compressToBitmap(newthumbfile)
                     } catch (e: IOException) {
                         e.printStackTrace()
                     }
                     val byte = ByteArrayOutputStream()
                     compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
                     val thumbdata = byte.toByteArray()
                     val uploadTask: UploadTask = storageReference.child("watches_post_images/thumbs")
                         .child("$.jpg").putBytes(thumbdata)


                     uploadTask.addOnFailureListener {
                         Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                     }.addOnSuccessListener {
                         val downloadthumburl = downloadurl.result
                         val items = HashMap<String, Any>()
                         items["image_url_primary"] = downloadurl.result.toString()
                         items["image_thumb_primary"] = downloadthumburl.toString()
                         items["image_url_first"] = downloadurl.result.toString()
                         items["image_thumb_first"] = downloadthumburl.toString()
                         items["image_url_second"] = downloadurl.result.toString()
                         items["image_thumb_second"] = downloadthumburl.toString()
                         items["image_url_third"] = downloadurl.result.toString()
                         items["image_thumb_third"] = downloadthumburl.toString()
                         items["reference"] = downloadthumburl.toString()
                         //items["timestamp"] = FieldValue.serverTimestamp()

                         firestore.collection("Posts").add(items)
                             .addOnCompleteListener { task ->
                                 if (task.isSuccessful) {
                                     Toast.makeText(this, "Post was added", Toast.LENGTH_LONG).show()
                                 }
                             }.addOnFailureListener {
                                 Toast.makeText(this, "FireStore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                             }
                     }
                 }
                 up++
                 k++

             }
         }*/

    }

    private fun selectImage() {
        CropImage.activity()
            .setMinCropResultSize(512, 512)
            .setAspectRatio(1, 1)
            .start(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {

                if (primary) {
                    primaryImageURI = result.uri
                    img_primary.visibility = View.GONE
                    val f = File(getRealPathFromURI(primaryImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    constraintLayout3.background = d
                    primary = false
                    mArrayUri.add(primaryImageURI)
                }
                if (first) {
                    firstImageURI = result.uri
                    img_one.visibility = View.GONE
                    val f = File(getRealPathFromURI(firstImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    first_img.background = d
                    first = false
                    mArrayUri.add(firstImageURI)
                }
                if (second) {
                    secondImageURI = result.uri
                    img_two.visibility = View.GONE
                    val f = File(getRealPathFromURI(secondImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    second_img.background = d
                    second = false
                    mArrayUri.add(secondImageURI)
                }
                if (third) {
                    thirdImageURI = result.uri
                    img_three.visibility = View.GONE
                    val f = File(getRealPathFromURI(thirdImageURI))
                    val d = Drawable.createFromPath(f.absolutePath)
                    third_img.background = d
                    third = false
                    mArrayUri.add(thirdImageURI)
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
                Log.d(TAG, error.toString())
            }
        }
    }

    private fun getRealPathFromURI(contentURI: Uri): String? {
        val cursor: Cursor? = contentResolver.query(contentURI, null, null, null, null)
        return if (cursor == null) { // Source is Dropbox or other similar local file path
            contentURI.path
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }


    private fun sendDataToStore() {


        /* var filepath = FirebaseStorage.getInstance().reference.child("gpic");

        while (up < mArrayUri.size()){

            filepath.child(mArrayUri.get(k).getLastPathSegment()).putFile(mArrayUri.get(k)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadURL = taskSnapshot.getDownloadUrl();
                    Toast.makeText(TestingActivity.this, downloadURL.toString(), Toast.LENGTH_SHORT).show();

                }
            });
            up++;
            k++;

        }*/

    }


    /*val randonName = UUID.randomUUID().toString()
        val primaryImgage = File(primaryImageURI.path)
        val firstImage = File(firstImageURI.path)
        val secondImage = File(secondImageURI.path)
        val ThirdImage = File(thirdImageURI.path)


        //Primary Image
        try {
            compressedImageFile = Compressor(this)
                .setMaxHeight(720)
                .setMaxWidth(720)
                .setQuality(50)
                .compressToBitmap(primaryImgage)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        val byte = ByteArrayOutputStream()
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
        val imagedata = byte.toByteArray()
        val ref = storageReference.child("watches_post_images").child("$randonName.jpg")
        val filepath = ref.putBytes(imagedata)
        filepath.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener {
            val downloadurl = ref.downloadUrl
            if (it.task.isSuccessful) {
                val newthumbfile = File(primaryImgage.path)
                try {
                    compressedImageFile = Compressor(this)
                        .setMaxHeight(100)
                        .setMaxWidth(100)
                        .setQuality(1)
                        .compressToBitmap(newthumbfile)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val byte = ByteArrayOutputStream()
                compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, byte)
                val thumbdata = byte.toByteArray()
                val uploadTask: UploadTask = storageReference.child("watches_post_images/thumbs")
                    .child("$randonName.jpg").putBytes(thumbdata)


                uploadTask.addOnFailureListener {
                    Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }.addOnSuccessListener {
                    val downloadthumburl = downloadurl.result
                    val items = HashMap<String, Any>()
                    items["image_url_primary"] = downloadurl.result.toString()
                    items["image_thumb_primary"] = downloadthumburl.toString()
                    items["image_url_first"] = downloadurl.result.toString()
                    items["image_thumb_first"] = downloadthumburl.toString()
                    items["image_url_second"] = downloadurl.result.toString()
                    items["image_thumb_second"] = downloadthumburl.toString()
                    items["image_url_third"] = downloadurl.result.toString()
                    items["image_thumb_third"] = downloadthumburl.toString()
                    items["reference"] = downloadthumburl.toString()
                    items["timestamp"] = FieldValue.serverTimestamp()

                    firestore.collection("Posts").add(items)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Post was added", Toast.LENGTH_LONG).show()
                            }
                        }.addOnFailureListener {
                            Toast.makeText(this, "FireStore Error: ${it.message}", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        }
}*/
}


