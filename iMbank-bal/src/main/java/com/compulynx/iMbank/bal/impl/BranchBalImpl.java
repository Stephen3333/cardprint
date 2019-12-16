package com.compulynx.iMbank.bal.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.compulynx.iMbank.bal.BranchBal;
import com.compulynx.iMbank.dal.impl.BranchDalImpl;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Printer;
import com.compulynx.iMbank.models.Regions;



public class BranchBalImpl implements BranchBal{
	Logger logger= Logger.getLogger(BranchBalImpl.class.getCanonicalName());

    @Autowired
    BranchDalImpl branchDal;

	public Branch getClassById(int classId) {
		// TODO Auto-generated method stub
		 return branchDal.getClassById(classId);
	}

	public List<Printer> getAllPrinters() {
		// TODO Auto-generated method stub
		return branchDal.getAllPrinters();
	}

	public ObjResponse createUpdateBranch(Branch branch) {
		// TODO Auto-generated method stub
		return branchDal.createUpdateBranch(branch);
	}

	public List<Branch> getAllBranchs() {
		// TODO Auto-generated method stub
		return branchDal.getAllBranchs();
	}

	public ObjResponse createUpdatePrinter(Printer printer) {
		// TODO Auto-generated method stub
		 return branchDal.createUpdatePrinter(printer);
	}
	public List<Regions> getAllRegions() {
        return branchDal. getAllRegions();
    }

	public ObjResponse createUpdateRegion(Regions region) {
		// TODO Auto-generated method stub
		return branchDal.createUpdateRegion(region);
	}


}
