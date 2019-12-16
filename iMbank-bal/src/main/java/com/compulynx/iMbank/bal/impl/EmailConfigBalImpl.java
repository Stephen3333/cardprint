package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.iMbank.bal.EmailConfigBal;
import com.compulynx.iMbank.dal.impl.CardInventoryDalImpl;
import com.compulynx.iMbank.dal.impl.EmailConfigDalImpl;
import com.compulynx.iMbank.models.EmailConfig;
import com.compulynx.iMbank.models.ObjResponse;

public class EmailConfigBalImpl implements EmailConfigBal{
	@Autowired
    EmailConfigDalImpl emailconfigDal;

	@Override
	public ObjResponse createUpdateEmailConfig(EmailConfig emailconfig) {
		return emailconfigDal.createUpdateEmailConfig(emailconfig);
	}

	@Override
	public List<EmailConfig> getAllEmailConfig() {
		return emailconfigDal.getAllEmailConfig();
	}

	@Override
	public ObjResponse updateEmailConfig(EmailConfig emailconfig) {
		return emailconfigDal.updateEmailConfig(emailconfig);
	}

}
