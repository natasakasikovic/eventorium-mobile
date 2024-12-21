package com.eventorium.data.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class FileUtil {

    public static MultipartBody.Part getImageFromUri(Context context, Uri uri) throws IOException {
        File file = FileUtil.getFileFromUri(context, uri);
        if (file != null) {
            RequestBody requestBody = RequestBody.create(file, MediaType.parse("image/*"));
            return MultipartBody.Part.createFormData("images", file.getName(), requestBody);
        }
        throw new IOException("Error getting file from uri");
    }

    public static List<MultipartBody.Part> getImagesFromUris(Context context, List<Uri> uris) throws IOException {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (Uri uri : uris) {
            parts.add(getImageFromUri(context, uri));
        }
        return parts;
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
}
