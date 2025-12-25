package com.onlinetradeview.tv.cmonulty.apicalling;

public interface WebServiceListener {
	void onWebServiceActionComplete(String result, String url);
	void onWebServiceError(String result, String url);
}
