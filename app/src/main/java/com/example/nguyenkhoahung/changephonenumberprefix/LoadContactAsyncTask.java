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

import com.example.nguyenkhoahung.changephonenumberprefix.util.ConvertNumberUtil;

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
            ContactDTO contact;
            int i = 0;
            startTime = SystemClock.elapsedRealtime();
            final int numberIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            final int typeNumberIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);
            final int idIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
            final int nameIndex = cur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                while (cur.moveToNext()) {
                    if(ConvertNumberUtil.isPhoneNumberNeedConvert(cur.getString(numberIndex))) {
                        startEachTask = SystemClock.elapsedRealtime();
                        i++;
                        contact = new ContactDTO();
                        contact.setContactId(cur.getInt(idIndex));
                        contact.setDisplayName(cur.getString(nameIndex));
                        contact.setPhoneNumber(Common.validateNumberPhone(cur.getString(numberIndex)));
                        contact.setMobileNetworkOperator(ConvertNumberUtil.getMobileNetworkOperatorName(contact.getPhoneNumber()));
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
                        }
                        Log.i("ChangePhoneNumber", "Contact " + i + " take " + (SystemClock.elapsedRealtime() - startEachTask) + " ms. "
                                + contact.getDisplayName() + " | " + contact.getPhoneNumber() +" . Nhà mạng "+contact.getMobileNetworkOperator());
                        listAllContact.add(contact);
                    }
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
        if (progressDialog != null &&progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(context, "Finish load contact ! Total contact " + contactDTOS.size(), Toast.LENGTH_LONG).show();
    }
}
