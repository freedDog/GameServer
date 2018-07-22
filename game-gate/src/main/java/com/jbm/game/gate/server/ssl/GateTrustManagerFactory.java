package com.jbm.game.gate.server.ssl;

import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509TrustManager;

/**
 * 网关信任管理工厂
 * @author JiangBangMing
 *
 * 2018年7月21日 下午2:53:45
 */
public class GateTrustManagerFactory extends TrustManagerFactorySpi{
	
	static final X509TrustManager X509=new X509TrustManager() {
		
		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[0];
		}
		
		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
			// TODO Auto-generated method stub
			
		}
	};
	
	static final TrustManager[] X509_MANAGERS=new TrustManager[] {X509};
	
	@Override
	protected TrustManager[] engineGetTrustManagers() {
		return X509_MANAGERS;
	}
	
	@Override
	protected void engineInit(KeyStore ks) throws KeyStoreException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void engineInit(ManagerFactoryParameters spec) throws InvalidAlgorithmParameterException {
		// TODO Auto-generated method stub
		
	}
	
}
