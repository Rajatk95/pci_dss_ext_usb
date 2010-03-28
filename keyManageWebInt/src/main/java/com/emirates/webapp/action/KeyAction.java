package com.emirates.webapp.action;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.io.File;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.codehaus.xfire.client.Client;

import com.emirates.model.Key;
import com.emirates.model.User;
import com.emirates.model.Group;
import com.emirates.service.KeyManager;
import com.emirates.service.UserManager;
import com.emirates.service.GroupManager;
import com.emirates.Constants;
import com.emirates.util.KeyStoreUtil;

public class KeyAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected final transient Log log = LogFactory.getLog(getClass());

	private KeyStoreUtil keystoreUtil;

	private Key key;

	public KeyStoreUtil getKeystoreUtil() {
		return keystoreUtil;
	}

	public void setKeystoreUtil(KeyStoreUtil keystoreUtil) {
		this.keystoreUtil = keystoreUtil;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
	
    /**
     * Grab the entity from the database before populating with request parameters
     */
    public void prepare() {
        if (getRequest().getMethod().equalsIgnoreCase("post")) {
            // prevent failures on new
            if (!"".equals(getRequest().getParameter("key.app_id"))) {
                key = keyManager.getkey(getRequest().getParameter("key.app_id"));
            }
        }
    }



	public String reintialize() {
		try {
			Client client = new Client(new URL(Constants.CITP_CRYPTO_URL));
			Object[] results = client.invoke("reintialize", new Object[] {});
			log.debug(results[0]);
			saveMessage("Reintialize Successfull ");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			saveMessage("MalformedURLException :: " + e.getMessage());
			log.error("MalformedURLException :: ", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			saveMessage("Exception :: " + e.getMessage());
			log.error("Exception :: ", e);
		}
		return list();
	}

	public String deleteKey() {
		HttpServletRequest request = getRequest();
		String id = request.getParameter("id");
		if (id != null) {
			log.debug("deleteKey :: " + id);
			keyManager.deactivateApplication(id);
		}
		try {

			Client client = new Client(new URL(Constants.CITP_SERVICE_URL));
			Object[] results = client
					.invoke("keyDeletion", new Object[] { id });
			log.debug(results[0]);

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			saveMessage("MalformedURLException :: " + e.getMessage());
			log.error("MalformedURLException :: ", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			saveMessage("Exception :: " + e.getMessage());
			log.error("Exception :: ", e);
		}
		saveMessage("Successfull Deletion Of Key For Application id : "
				+ request.getParameter("id"));
		return list();

	}

	public String regenerateAllKeys() {
		log.debug("RegenerateAllKeys Invoked !! ");
		try {

			Client client = new Client(new URL(Constants.CITP_SERVICE_URL));
			Object[] results = client.invoke("regeneratePrivateKeys",
					new Object[] {});
			log.debug(results[0]);
			saveMessage("Successfull Regeneration Of All Keys ");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			saveMessage("MalformedURLException :: " + e.getMessage());
			log.error("MalformedURLException :: ", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			saveMessage("Exception :: " + e.getMessage());
			log.error("Exception :: ", e);
		}
		return list();
	}

	public String save() {
		//log.debug("Saved the application : " + key.getApp_id());
		Certificate cert = null;
		X509Certificate xcert = null;
		try {
			cert = keystoreUtil.getCertificateFromFile(this.key.getFile());
			xcert = (X509Certificate) cert;
			this.key.setCertificate_id(xcert.getSerialNumber().toString());
		} catch (Exception e) {
			saveMessage("Exception :: " + e.getMessage());
			log.error("Exception :: ", e);
			return list();
		}
		try {
			//log.info("Old password ::" + this.key.getOldPassword());
			//log.info("New password ::" + this.key.getNewPassword());
			Client client = new Client(new URL(Constants.CITP_SERVICE_URL));
			Object[] results = client.invoke("generatePrivateKey",
					new Object[] { key.getName(), key.getCertificate_id(),
							key.getGroup_id() });
			log.debug("Result Size ::" + results.length);
			for (int i = 0; i < results.length; i++) {
				log.debug(results[0]);
			}
			keystoreUtil.saveCertificateInKeyStore(key.getName(), cert);
			saveMessage(results[0].toString());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			saveMessage("MalformedURLException :: " + e.getMessage());
			log.error("MalformedURLException :: ", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			saveMessage("Exception :: " + e.getMessage());
			log.error("Exception :: ", e);
		}
		return list();

	}

	public String cancel() {
		return "cancel";
	}


	@SkipValidation
	public String createNewKey() {
		//log.info("GroupList Size:" + this.groupList.size());
		this.key = new Key();
		this.key.setPasswordChangeRequired(true);
		return SUCCESS;
	}

	public String getGroups() {

		//this.userList = userManager.getUsers(new User());
		return SUCCESS;

	}

	public String generateNewKeys() {
		try {

			Client client = new Client(new URL(Constants.CITP_SERVICE_URL));
			Object[] results = client.invoke("generatePrivateKeys",
					new Object[] {});
			log.debug(results[0]);
			/*
			   KeyGenereationLocator loc = new KeyGenereationLocator();
			   KeyGenereationPortType client = (KeyGenereationPortType)loc.getPort(KeyGenereationPortType.class);
			   client.generatePrivateKeys();
			 */
			saveMessage("Successfull Generation Of New Keys!! ");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			saveMessage("MalformedURLException :: " + e.getMessage());
			log.error("MalformedURLException :: ", e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			saveMessage("Exception :: " + e.getMessage());
			log.error("Exception :: ", e);
		}
		return list();
	}

}
