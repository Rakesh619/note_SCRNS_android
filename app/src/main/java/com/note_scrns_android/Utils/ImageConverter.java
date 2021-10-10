package com.note_scrns_android.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class ImageConverter {
    public static byte[] convertImage2ByteArray(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
        return  stream.toByteArray();
    }
    public  static Bitmap convertByteArray2Bitmap(byte [] array){
        return BitmapFactory.decodeByteArray(array,0,array.length);
    }
}