package com.fo0.lmp.ui.utils;

import java.io.IOException;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class UtilsSSLCertExpiry {

	// usage : SSLCertExpiry host <cn>
	// does a http://host request.
	// takes an optional cn variable (which defaults to host)

	public static int check(String cn) throws IOException {

		// without a trust manager, i was having problems of
		// sun.security.validator.ValidatorException: PKIX path building failed:
		// sun.security.provider.certpath.SunCertPathBuilderException: unable to find
		// valid certification path to requested target
		// the code below was taken from
		// http://stackoverflow.com/questions/7443235/getting-java-to-accept-all-certs-over-https
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub

			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			System.out.println("could not install trust manager.. continuing here; it may not be necessary");
		}
		
		int daysleft = 0;
		URL url = new URL("https://" + cn);
		HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
		conn.connect();
		Certificate[] certs = conn.getServerCertificates();
		for (Certificate c : certs) {
			// System.out.println(c.getType());
			// System.out.println(c.toString());
			X509Certificate xc = (X509Certificate) c; // we should really check the type beore doing this typecast..
			String dn = xc.getSubjectDN().getName();
			if (dn.contains(cn)) {
				Date expiresOn = xc.getNotAfter();
				Date now = new Date();
				daysleft = (int) ((expiresOn.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
//				System.out.println(cn + " certificate expires on :" + expiresOn + ".. only "
//						+ (expiresOn.getTime() - now.getTime()) / (1000 * 60 * 60 * 24) + " days to go");
			}
		}
		return daysleft;
	}
}