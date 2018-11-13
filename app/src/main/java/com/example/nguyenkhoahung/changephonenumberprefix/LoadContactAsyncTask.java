package com.example.nguyenkhoahung.changephonenumberprefix;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.example.nguyenkhoahung.changephonenumberprefix.util.ConvertNumberUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoadContactAsyncTask extends AsyncTask<Void, Integer, List<ContactDTO>> {
    public AsyncResponse delegate = null;
    private ProgressDialog progressDialog;
    Context context;
    Uri uri;
    int numberIndex, typeNumberIndex, idIndex, nameIndex;
    Cursor cur;
    public LoadContactAsyncTask(Context context, Uri uri) {
        this.context = context;
        this.uri = uri;
    }

    @Override
    protected List<ContactDTO> doInBackground(Void... voids) {
        List<ContactDTO> listAllContact = new ArrayList<>();
        String number = "";
        int typeNumber = 0;
        long startTime = 0;
        long startEachTask = 0;
        String[] projection = null;
        String selection = null;
        String sortOrder = null;
        String[] selectionArgs = null;
        ContentResolver cr = context.getContentResolver();
        if(uri.equals(ContactsContract.CommonDataKinds.Phone.CONTENT_URI)){
            projection = new String[] {
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                    ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.TYPE };
            selection = ContactsContract.Data.HAS_PHONE_NUMBER + " = "+"1";
            sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC";
            cur = cr.query(uri,projection, selection, selectionArgs, sortOrder);
            numberIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            typeNumberIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            idIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            nameIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
        } else if(uri.equals(Uri.parse("content://icc/adn"))){
            cur = cr.query(uri,projection, selection, selectionArgs, sortOrder);
            nameIndex = cur.getColumnIndex("name");
            numberIndex = cur.getColumnIndex("number");
            idIndex = cur.getColumnIndex(ContactsContract.Contacts._ID);
            typeNumberIndex = cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            //sortOrder = " number ASC";
        }

        try {
            ContactDTO contact;
            int i = 0;
            startTime = SystemClock.elapsedRealtime();
                while (cur.moveToNext()) {
                    if(ConvertNumberUtil.isPhoneNumberNeedConvert(cur.getString(numberIndex))) {
                        startEachTask = SystemClock.elapsedRealtime();
                        i++;
                        contact = new ContactDTO();
                        if(idIndex > -1 ){
                            contact.setContactId(cur.getInt(idIndex));
                        }
                        contact.setDisplayName(cur.getString(nameIndex));
                        contact.setPhoneNumber(Common.validateNumberPhone(cur.getString(numberIndex)));
                        contact.setMobileNetworkOperator(ConvertNumberUtil.getMobileNetworkOperatorName(contact.getPhoneNumber()));
                        if(typeNumberIndex > 0){
                            switch (cur.getInt(typeNumberIndex)) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    contact.setPhoneNumberType("Home");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    contact.setPhoneNumberType("Mobile");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    contact.setPhoneNumberType("Work");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                                    contact.setPhoneNumberType("Work Mobile");
                                    break;
                                case ContactsContract.CommonDataKinds.Phone.TYPE_OTHER:
                                    contact.setPhoneNumberType("Other");
                                    break;
                            }
                        }
                        Log.i("ChangePhoneNumber", "Contact " + i + " take " + (SystemClock.elapsedRealtime() - startEachTask) + " ms. "
                                + contact.getDisplayName() + " | " + contact.getPhoneNumber() +" . Nhà mạng "+contact.getMobileNetworkOperator());
                        listAllContact.add(contact);
                    }
                }
        } catch (Exception e) {
            Log.e("ChangePhoneNumber", "Load contact fail !");
            e.printStackTrace();
        } finally {
            if (!cur.isClosed()){
                cur.close();
            }
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
        if (progressDialog != null &&progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(context, "Finish load contact ! Total contact " + contactDTOS.size(), Toast.LENGTH_LONG).show();
    }
}
