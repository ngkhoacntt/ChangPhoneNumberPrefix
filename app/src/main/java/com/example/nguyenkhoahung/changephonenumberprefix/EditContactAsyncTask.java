package com.example.nguyenkhoahung.changephonenumberprefix;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.nguyenkhoahung.changephonenumberprefix.util.Constant;

import java.util.Arrays;
import java.util.List;

public class EditContactAsyncTask extends AsyncTask<Void, Void, List<ContactDTO>> {
    private Context context;
    private List<ContactDTO> listContact;
    private ProgressDialog progressDialog;
    public AsyncResponse delegate = null;
    public EditContactAsyncTask(Context context, List<ContactDTO> listContact) {
        this.listContact = listContact;
        this.context = context;
    }

    @Override
    protected List<ContactDTO> doInBackground(Void... voids) {
        String number;
        if(!Common.isEmptyList(listContact)){
            for (ContactDTO contact: listContact) {
                number = contact.getPhoneNumber();
                if(number.contains(Constant.PLUS_CHARACTER)){
                    contact.setCountryCode(number.substring(0,3));
                    contact.setOldAreaCode(number.substring(3,6));
                    contact.setNewAreaCode(Common.MAP_CONVERT_NUMBER.get(number.substring(3,6)));
                    contact.setMainPhoneNumber(number.substring(6,number.length()));
                } else {
                    contact.setCountryCode("0");
                    contact.setOldAreaCode(number.substring(1,4));
                    contact.setNewAreaCode(Common.MAP_CONVERT_NUMBER.get(number.substring(1,4)));
                    contact.setMainPhoneNumber(number.substring(4,number.length()));
                }
                contact.setNewPhoneNumber(new StringBuffer()
                        .append(contact.getCountryCode())
                        .append(contact.getNewAreaCode())
                        .append(contact.getMainPhoneNumber())
                        .toString());
                Log.i("EditContactAsyncTask",contact.getDisplayName() +" | "+contact.getPhoneNumber()+" | "+contact.getCountryCode()
                        +" | "+contact.getOldAreaCode()+" | "+contact.getNewAreaCode()+" | "+contact.getMainPhoneNumber()+" | "+contact.getNewPhoneNumber());
            }
        }
        return listContact;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Converting number...");
        progressDialog.show();
    }

    @Override
    protected void onPostExecute(List<ContactDTO> contactDTOS) {
        delegate.processFinish(contactDTOS);
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        Toast.makeText(context, "Finish converted ! Total contact converted " + contactDTOS.size(), Toast.LENGTH_LONG).show();
    }
}
