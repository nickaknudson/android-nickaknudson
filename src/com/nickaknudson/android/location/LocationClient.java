/**
 * 
 */
package com.nickaknudson.android.location;

import java.lang.reflect.Type;

import com.google.gson.reflect.TypeToken;
import com.nickaknudson.mva.callbacks.ReceiveCallback;
import com.nickaknudson.mva.clients.ReceiveClient;

/**
 * @author nick
 *
 */
public class LocationClient implements ReceiveClient<Location> {

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.ReceiveClient#receive(com.nickaknudson.mva.callbacks.ReceiveCallback)
	 */
	@Override
	public void receive(ReceiveCallback<Location> callback) {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.ReceiveClient#addReceiveCallback(com.nickaknudson.mva.callbacks.ReceiveCallback)
	 */
	@Override
	public boolean add(ReceiveCallback<Location> callback) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.ReceiveClient#removeReceiveCallback(com.nickaknudson.mva.callbacks.ReceiveCallback)
	 */
	@Override
	public boolean remove(ReceiveCallback<Location> callback) {
		// TODO Auto-generated method stub
		return false;
	}

	public Type getType() {
		return getTypeToken().getType();
	}

	/* (non-Javadoc)
	 * @see com.nickaknudson.mva.clients.ModelClient#getTypeToken()
	 */
	@Override
	public TypeToken<Location> getTypeToken() {
		return new TypeToken<Location>(){};
	}

}
