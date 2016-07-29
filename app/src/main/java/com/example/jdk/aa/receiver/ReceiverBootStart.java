package com.example.jdk.aa.receiver;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.jdk.aa.service.ServiceDatabaseBackup;

public class ReceiverBootStart extends BroadcastReceiver {
	
	@Override
	public void onReceive(Context p_Context, Intent p_Intent) {
		Intent _Intent = new Intent(p_Context, ServiceDatabaseBackup.class);
		p_Context.startService(_Intent);
	}
}
