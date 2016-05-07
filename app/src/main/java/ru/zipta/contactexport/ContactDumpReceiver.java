package ru.zipta.contactexport;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ContactDumpReceiver extends BroadcastReceiver {
    public ContactDumpReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            dumpContacts(context);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void dumpContacts(Context ctx) throws IOException {

        ContentResolver cr = ctx.getContentResolver();

        Uri contactUri = ContactsContract.Contacts.CONTENT_URI;

        Cursor cur = cr.query(contactUri,null, null, null, null);

        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();

        List<MyContact> contacts = new ArrayList<>();

        while (cur.moveToNext()) {
            String contactId = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
            String name      = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) == 0)
                continue;

            Log.d("contacts", contactId + " : " + name);

            MyContact contact = new MyContact(contactId, name);

            Cursor pcur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", new String[]{contactId}, null);
            while (pcur.moveToNext()) {
                String phone = pcur.getString(pcur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                Log.d("contacts", "  " + phone);
                contact.phones.add(phone);
            }
            pcur.close();
            contacts.add(contact);
        }
        cur.close();

        File file;
        FileOutputStream outputStream;
        file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "contacts.json");
        Log.d("contacts", "path: " + file.getAbsolutePath());
        outputStream = new FileOutputStream(file);
        outputStream.write(gson.toJson(contacts).getBytes());
        outputStream.close();

    }
}
