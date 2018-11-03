package com.example.nguyenkhoahung.changephonenumberprefix;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
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
        try {
            ContentResolver cr = context.getContentResolver();
            Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                    null, null, null, null);
            ContactDTO contact = null;
            DataDTO data = null;
            int i = 0;
            if (cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        contact = new ContactDTO();
                        contact.setContactId(cur.getInt(cur.getColumnIndex(ContactsContract.Contacts._ID)));
                        contact.setDisplayName(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                        // get the phone number
                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{contact.getContactId() + ""}, null);
                        while (pCur.moveToNext()) {
                            data = new DataDTO();
                            String number = pCur.getString(
                                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            data.setDataValue(Common.validateNumberPhone(number));
                            int typeNumber = pCur.getInt(
                                    pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                            switch (typeNumber) {
                                case ContactsContract.CommonDataKinds.Phone
                                        .TYPE_HOME:
                                    data.setDataType("Home");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone
                                        .TYPE_MOBILE:
                                    data.setDataType("Mobile");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone
                                        .TYPE_WORK:
                                    data.setDataType("Work");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone
                                        .TYPE_WORK_MOBILE:
                                    data.setDataType("Work Mobile");
                                    break;
                            }
                            contact.getPhoneList().add(data);
                        }
                        pCur.close();
                    }
                    listAllContact.add(contact);
                }
            }
        } catch (Exception e) {
            Log.e("ChangePhoneNumber", "Load contact fail !");
        }
        Log.i("ChangePhoneNumber", "Total contact number: " + listAllContact.size());
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
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        MainActivity.TOTAL_CONTACT = contactDTOS.size();
        Toast.makeText(context, "Finish load contact ! Total contact " + contactDTOS.size(), Toast.LENGTH_LONG).show();
    }
}
