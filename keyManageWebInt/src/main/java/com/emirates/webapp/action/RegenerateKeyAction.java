package com.emirates.webapp.action;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.interceptor.validation.SkipValidation;
import org.codehaus.xfire.client.Client;

import com.emirates.Constants;
import com.emirates.model.Key;


public class RegenerateKeyAction extends BaseAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1484764165268302659L;

	protected final transient Log log = LogFactory.getLog(getClass());
	
    private Key key;
    public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}
	
	public String cancel() {
		return "cancel";
	}

	@SkipValidation
	public String prepareForRegeneration() {
		HttpServletRequest request = getRequest();
		String appid = request.getParameter("id");
		this.key = keyManager.getkey(appid);
		this.key.setPasswordChangeRequired(true);
		this.key.setRegenerationMode(true);
		return SUCCESS;
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

	
	public String regenerateKey() {
		log.debug("RegenerateKeys Invoked !! ");
		try {
			log.info("Old password ::" + this.key.getOldPassword());
			log.info("New password ::" + this.key.getNewPassword());
			log.info("Regeneration Of Key  ::" + key.getName());
			try {
				Client client = new Client(new URL(Constants.CITP_SERVICE_URL));
				Object[] results = client.invoke("regeneratePrivateKey",
						new Object[] { key.getName() });
				log.debug("Result Size ::" + results.length);
				for (int i = 0; i < results.length; i++) {
					log.debug(results[0]);
				}
				saveMessage("Successfull Regeneration Of Key For Application id : "
						+ key.getApp_id());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				saveMessage("MalformedURLException :: " + e.getMessage());
				log.error("MalformedURLException :: ", e);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				saveMessage("Exception :: " + e.getMessage());
				log.error("Exception :: ", e);
			}
		} catch (Exception e) {
			// TODO: handle exception
			saveMessage("Exception :: " + e.getMessage());
			log.error("Exception :: ", e);
		}
		return list();
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
    /*
    private String callWebservice(String appId) throws MalformedURLException, Exception{
        
        //Create a metadata of the service      
        Service serviceModel = new ObjectServiceFactory().create(KeyGeneration.class);        
        log.debug("callSoapServiceLocal(): got service model." );
   
        //Create a proxy for the deployed service
        XFire xfire = XFireFactory.newInstance().getXFire();
        XFireProxyFactory factory = new XFireProxyFactory(xfire);      
    
        String serviceUrl = "http://localhost:9080/citpservices/services/KeyGenereation";
        
        KeyGeneration client = null;
        try {
            client = (KeyGeneration) factory.create(serviceModel, serviceUrl);
        } catch (MalformedURLException e) {
            log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());
        }    
               
        //Invoke the service
        String serviceResponse = "";
        try { 
            serviceResponse = client.regeneratePrivateKey(appId);
       } catch (Exception e){
            log.error("WsClient.callWebService(): EXCEPTION: " + e.toString());                 
            serviceResponse = e.toString();
        }        
        log.debug("WsClient.callWebService(): status=" + serviceResponse);            
  

        //Return the response
        return serviceResponse;

    }*/



}
