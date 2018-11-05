package com.example.nguyenkhoahung.changephonenumberprefix;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadContactAsyncTask extends AsyncTask<Void, Integer, List<ContactDTO>> {
    public AsyncResponse delegate = null;
    ProgressDialog progressDialog;
    Context context;

    public LoadContactAsyncTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<ContactDTO> doInBackground(Void... voids) {
        List<ContactDTO> listAllContact = new ArrayList<>();
        String number = "";
        int typeNumber = 0;
        long startTime = 0;
        long startEachTask = 0;
        final String[] PROJECTION = new String[] {
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.TYPE
        };
        String selection = ContactsContract.Data.HAS_PHONE_NUMBER + " = "+"1";
        try {
            Cursor pCur;
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    PROJECTION, selection, null, null);
            ContactDTO contact = null;
            DataDTO data = null;
            int i = 0;
            startTime = SystemClock.elapsedRealtime();
            final int numberIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int typeNumberIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            final int idIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            final int nameIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                while (cur.moveToNext()) {
                    startEachTask = SystemClock.elapsedRealtime();
                        i++;
                        contact = new ContactDTO();
                        contact.setContactId(cur.getInt(idIndex));
                        contact.setDisplayName(cur.getString(nameIndex));
                        data = new DataDTO();
                        data.setDataValue(cur.getString(numberIndex));
                        typeNumber = cur.getInt(typeNumberIndex);
                            switch (typeNumber) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    data.setDataType("Home");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    data.setDataType("Mobile");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    data.setDataType("Work");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                    data.setDataType("Work Mobile");
                                    break;
                            }
                            contact.getPhoneList().add(data);
                        // get the phone number
                        pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone.TYPE},
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "+contact.getContactId(),
                                null, null);
                        while (pCur.moveToNext()) {
                            data = new DataDTO();
                            data.setDataValue(pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                            typeNumber = pCur.getInt(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            switch (typeNumber) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    data.setDataType("Home");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    data.setDataType("Mobile");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    data.setDataType("Work");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                    data.setDataType("Work Mobile");
                                    break;
                            }
                            contact.getPhoneList().add(data);
                        }
                        pCur.close();
                        Log.i("ChangePhoneNumber", "Contact "+i+" take "+(SystemClock.elapsedRealtime() - startEachTask) + " ms. "+contact.getDisplayName()+" has "+contact.getPhoneList().size()+" number");
                    listAllContact.add(contact);
                    }
                cur.close();
        } catch (Exception e) {
            Log.e("ChangePhoneNumber", "Load contact fail !");
        }
        Log.i("ChangePhoneNumber", "Total contact number: " + listAllContact.size());
        Log.i("ChangePhoneNumber", "Total time: " + (SystemClock.elapsedRealtime() - startTime) + " ms");
        return listAllContact;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading contact...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<ContactDTO> contactDTOS) {
        delegate.processFinish(contactDTOS);
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
        Toast.makeText(context, "Finish load contact ! Total contact " + contactDTOS.size(), Toast.LENGTH_LONG).show();
    }
}
