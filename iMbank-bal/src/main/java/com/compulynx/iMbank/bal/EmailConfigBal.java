package com.compulynx.iMbank.bal;

import java.util.List;

import com.compulynx.iMbank.models.EmailConfig;
import com.compulynx.iMbank.models.ObjResponse;



public interface EmailConfigBal {
	
	public ObjResponse createUpdateEmailConfig(EmailConfig emailconfig);
	public List<EmailConfig> getAllEmailConfig();
	public ObjResponse updateEmailConfig(EmailConfig emailconfig);

}
