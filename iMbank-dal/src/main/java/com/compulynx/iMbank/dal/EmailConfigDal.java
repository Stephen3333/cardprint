package com.compulynx.iMbank.dal;

import java.util.List;

import com.compulynx.iMbank.models.EmailConfig;
import com.compulynx.iMbank.models.ObjResponse;

public interface EmailConfigDal {
	public ObjResponse createUpdateEmailConfig(EmailConfig emailconfig);
	public List<EmailConfig> getAllEmailConfig();
	public ObjResponse updateEmailConfig(EmailConfig emailconfig);

}
