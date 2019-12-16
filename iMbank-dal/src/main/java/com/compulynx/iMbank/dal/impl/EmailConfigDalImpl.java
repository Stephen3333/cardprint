package com.compulynx.iMbank.dal.impl;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.compulynx.iMbank.dal.EmailConfigDal;
import com.compulynx.iMbank.models.EmailConfig;
import com.compulynx.iMbank.models.ObjResponse;

public class EmailConfigDalImpl implements EmailConfigDal {
	Logger logger=Logger.getLogger(EmailConfigDalImpl.class.getCanonicalName());
    private DataSource dataSource;

    public EmailConfigDalImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }

	@Override
	public ObjResponse createUpdateEmailConfig(EmailConfig emailconfig) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<EmailConfig> getAllEmailConfig() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ObjResponse updateEmailConfig(EmailConfig emailconfig) {
		// TODO Auto-generated method stub
		return null;
	}

}
