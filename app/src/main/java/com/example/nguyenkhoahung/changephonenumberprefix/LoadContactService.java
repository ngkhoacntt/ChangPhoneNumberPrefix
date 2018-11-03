package com.example.nguyenkhoahung.changephonenumberprefix;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.support.annotation.Nullable;

public class LoadContactService extends IntentService {
    public LoadContactService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        ContentResolver cr = getContentResolver();
    }
}
