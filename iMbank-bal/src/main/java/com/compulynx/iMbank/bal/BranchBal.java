package com.compulynx.iMbank.bal;

import java.util.List;

import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Printer;
import com.compulynx.iMbank.models.Regions;


public interface BranchBal {
	public Branch getClassById(int classId);
	
	public ObjResponse createUpdatePrinter(Printer printer);

	public List<Printer> getAllPrinters();

	public ObjResponse createUpdateBranch(Branch branch);

	public List<Branch> getAllBranchs();
	
	public List<Regions> getAllRegions();

}
