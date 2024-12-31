package com.eventorium.data.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class FileUtil {

    public static MultipartBody.Part getImageFromUri(Context context, Uri uri, String paramName) throws IOException {
        File file = FileUtil.getFileFromUri(context, uri);
        if (file != null) {
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/*"));
            return MultipartBody.Part.createFormData(paramName, file.getName(), requestBody);
        }
        throw new IOException("Error getting file from uri");
    }

    public static List<MultipartBody.Part> getImagesFromUris(Context context, List<Uri> uris, String name) throws IOException {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : uris) {
            parts.add(getImageFromUri(context, uri, name));
        }
        return parts;
    }

    public static Bitmap convertToBitmap(String base64String) {
        byte[] decodedBytes = Base64.decode(base64String, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static File getFileFromUri(Context context, Uri uri) throws IOException {
        String fileName = getFileName(context, uri);
        if (fileName == null) return null;

        File file = new File(context.getCacheDir(), fileName);
        try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
             FileOutputStream outputStream = new FileOutputStream(file)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while (true) {
                assert inputStream != null;
                if ((bytesRead = inputStream.read(buffer)) == -1) break;
                outputStream.write(buffer, 0, bytesRead);
            }
        }
        return file;
    }

    private static String getFileName(Context context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        if (cursor != null) {
            try {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                cursor.moveToFirst();
                return cursor.getString(nameIndex);
            } finally {
                cursor.close();
            }
        }
        return null;
    }

    public static Uri savePdfToDownloads(Context context, ResponseBody responseBody) throws IOException {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Downloads.DISPLAY_NAME, "price_list_report.pdf");
        values.put(MediaStore.Downloads.MIME_TYPE, "application/pdf");
        values.put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS);

        Uri pdfUri = context.getContentResolver().insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values);

        if (pdfUri != null) {
            try (InputStream inputStream = responseBody.byteStream();
                 OutputStream outputStream = context.getContentResolver().openOutputStream(pdfUri)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    assert outputStream != null;
                    outputStream.write(buffer, 0, bytesRead);
                }
                return pdfUri;
            }
        }
        return null;
    }
}
