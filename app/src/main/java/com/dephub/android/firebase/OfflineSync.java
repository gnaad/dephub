package com.dephub.android.firebase;

import com.google.firebase.database.FirebaseDatabase;

class OfflineSync extends android.app.Application {

    @Override
    public void onCreate() {
        super.onCreate( );
        FirebaseDatabase.getInstance( ).setPersistenceEnabled(true);
        FirebaseDatabase.getInstance( ).getReference( ).keepSynced(true);
    }
}