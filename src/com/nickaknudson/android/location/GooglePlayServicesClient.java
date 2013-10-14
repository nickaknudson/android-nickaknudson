/**
 * 
 */
package com.nickaknudson.android.location;

import com.nickaknudson.mva.callbacks.PersistentCallback;
import com.nickaknudson.mva.clients.PersistentClient;

/**
 * @author nick
 *
 */
public class GooglePlayServicesClient implements PersistentClient {

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.PersistentClient#connect(com.nickaknudson.mva.callbacks.PersistentCallback)
	 */
	@Override
	public void connect(PersistentCallback callback) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.PersistentClient#addConnectCallback(com.nickaknudson.mva.callbacks.PersistentCallback)
	 */
	@Override
	public boolean add(PersistentCallback callback) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.PersistentClient#removeConnectCallback(com.nickaknudson.mva.callbacks.PersistentCallback)
	 */
	@Override
	public boolean remove(PersistentCallback callback) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.PersistentClient#disconnect()
	 */
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.PersistentClient#isConnected()
	 */
	@Override
	public boolean isConnected() {
		// TODO Auto-generated method stub
		return false;
	}

}
