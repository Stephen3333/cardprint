package com.compulynx.iMbank.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.compulynx.iMbank.dal.BranchDal;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Printer;
import com.compulynx.iMbank.models.Regions;

public class BranchDalImpl implements BranchDal {
	Logger logger = Logger.getLogger(BranchDalImpl.class.getCanonicalName());
	private DataSource dataSource;

	public BranchDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * get branch by name
	 */

	public Branch getClassById(int classId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			logger.info("Class Id>>" + classId);
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchName);
			preparedStatement.setInt(1, classId);
			resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				Branch branch = new Branch();
				branch.branchId = resultSet.getInt("ID");
				branch.branchName = resultSet.getString("BranchName");
				branch.respCode = 200;
				branch.respMessage = "success";
				return branch;
			}
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get all printer
	 */
	public List<Printer> getAllPrinters() {
		//logger.info("get printer listings");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getPrinters);

			resultSet = preparedStatement.executeQuery();
			List<Printer> detail = new ArrayList<Printer>();
			while (resultSet.next()) {
				Printer printer = new Printer();
				printer.counter = counter;
				printer.id = resultSet.getInt("id");
				printer.serialNumber = resultSet.getString("serialNumber");
				printer.branchId = resultSet.getInt("branchId");
				printer.ipAddress = resultSet.getString("ipAddress");
				printer.branchName = resultSet.getString("branchName");
				printer.status = resultSet.getBoolean("status");
				detail.add(printer);
				counter++;
				//System.out.println("###: " + printer.status);

			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * create and update printer details
	 */
	public ObjResponse createUpdatePrinter(Printer printer) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String brName = getBranchName(printer.branchId);
			logger.info("###branch name in getbranch: " + brName);
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (printer.id == 0) {

				// validate
				if (checkPrinterCode(printer.serialNumber, printer.id)) {
					return new ObjResponse(201,
							"Oops!! Printer serial number Already Exists, Please Try again");
				}
				if (checkPrinterIp(printer.ipAddress, printer.id)) {
					return new ObjResponse(
							201,
							"Oops!! Another Printer Has been registered with the same Ip Address, Please Try again");
				}
				preparedStatement = connection
						.prepareStatement(Queryconstants.createPrinter);
				preparedStatement.setString(1, printer.serialNumber);
				preparedStatement.setInt(2, printer.branchId);
				preparedStatement.setString(3, brName);
				preparedStatement.setString(4, printer.ipAddress);
				preparedStatement.setString(5, printer.createdBy);
				preparedStatement.setBoolean(6, printer.status);

			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateProduct);
				// preparedStatement.setString(1, company.productCode);
				preparedStatement.setString(1, printer.serialNumber);
				preparedStatement.setString(2, printer.ipAddress);
				preparedStatement.setInt(3, printer.branchId);
				preparedStatement.setString(4, brName);
				preparedStatement.setString(5, printer.createdBy);
				preparedStatement.setTimestamp(6, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setBoolean(7, printer.status);
				preparedStatement.setInt(8, printer.id);

			}
			if (preparedStatement.executeUpdate() > 0) {
				// added for inserting loggerinfo
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertLoggerInfo);
				preparedStatement.setString(1, "printer");
				preparedStatement.setInt(2, printer.id);
				preparedStatement.setString(3, printer.serialNumber);
				preparedStatement.setInt(4, printer.branchId);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.executeUpdate();
				connection.commit();
				return new ObjResponse(200, "Records Updated");
			} else {
				connection.rollback();
				return new ObjResponse(202, "Nothing To Update");
			}
			/*
			 * return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
			 * 200, "Records Updated") : new ObjResponse(201,
			 * "Nothing To Update");
			 */

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new ObjResponse(202, "Exception Occurred");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(202, "Exception Occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * check branch name is exist or not using
	 * 
	 * @param branchId
	 * @return
	 */
	private String getBranchName(int branchId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String branchName = "";
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchName);
			preparedStatement.setInt(1, branchId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				branchName = resultSet.getString("BranchName");

			}

		} catch (Exception e) {

		}

		return branchName;
	}

	/**
	 * validate printer serial number
	 * 
	 * @param serialNumber
	 * @param id
	 * @return
	 */
	public boolean checkPrinterCode(String serialNumber, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPrinterByCode);
			preparedStatement.setString(1, serialNumber);
			preparedStatement.setInt(2, id);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * validate printer ip address
	 * 
	 * @param ipAddress
	 * @param id
	 * @return
	 */
	public boolean checkPrinterIp(String ipAddress, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPrinterByIp);
			preparedStatement.setString(1, ipAddress);
			preparedStatement.setInt(2, id);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * create and update branch
	 */
	public ObjResponse createUpdateBranch(Branch branch) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			String regName = getBranchName(branch.regionId);
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			System.out.println("####branchid: " + branch.branchId);
			if (branch.branchId == 0) {

				// validate
				if (checkBranchByCode(branch.branchCode, branch.branchId)) {
					return new ObjResponse(201,
							"Oops!! Branch Code Already Exists, Please Try again");
				}

				if (checkBranchByName(branch.branchName, branch.branchId)) {
					return new ObjResponse(201,
							"Oops!! Branch Name Already Exists, Please Try again");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertBranches);
				preparedStatement.setString(1, branch.branchName);
				preparedStatement.setString(2, branch.createdBy);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(4, branch.branchCode);
				preparedStatement.setString(5, branch.branchAddress);
				preparedStatement.setInt(6, branch.regionId);
				// preparedStatement.setString(7, regName);
				preparedStatement.setBoolean(7, branch.status);
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateBranches);
				preparedStatement.setString(1, branch.branchName);
				preparedStatement.setString(2, branch.branchCode);
				preparedStatement.setString(3, branch.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(5, branch.branchAddress);
				preparedStatement.setInt(6, branch.regionId);
				// preparedStatement.setString(7, regName);
				preparedStatement.setBoolean(7, branch.status);
				preparedStatement.setInt(8, branch.branchId);
				System.out.println("branch id###" + branch.branchId);

			}
			if (preparedStatement.executeUpdate() > 0) {
				// added for inserting loggerinfo
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertLoggerInfo);
				preparedStatement.setString(1, "branch");
				preparedStatement.setInt(2, branch.branchId);
				preparedStatement.setString(3, branch.branchCode);
				preparedStatement.setInt(4, branch.branchId);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.executeUpdate();
				connection.commit();
				return new ObjResponse(200, "Records Updated");
			} else {
				connection.rollback();
				return new ObjResponse(202, "Nothing To Update");
			}
			/*
			 * return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
			 * 200, "Records Updated") : new ObjResponse(201,
			 * "Nothing To Update");
			 */
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new ObjResponse(202, "Exception Occurred,Please Try again");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(202, "Exception Occurred, Please Try again");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checking brach name is exist or not
	 * 
	 * @param branchName
	 * @param id
	 * @return true if exits
	 */
	private boolean checkBranchByName(String branchName, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchByName);
			preparedStatement.setString(1, branchName);
			preparedStatement.setInt(2, id);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checking branch code is exist or not
	 * 
	 * @param branchCode
	 * @param id
	 * @return true if exists
	 */
	private boolean checkBranchByCode(String branchCode, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchByCode);
			preparedStatement.setString(1, branchCode);
			preparedStatement.setInt(2, id);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get all branches
	 */
	public List<Branch> getAllBranchs() {
		//logger.info("get BRANCHED");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchs);
			resultSet = preparedStatement.executeQuery();
			List<Branch> detail = new ArrayList<Branch>();
			while (resultSet.next()) {
				Branch branch = new Branch();
				branch.counter = counter;
				branch.branchId = resultSet.getInt("ID");
				branch.branchName = resultSet.getString("BranchName");
				branch.branchCode = resultSet.getString("BranchCode");
				branch.regionId = resultSet.getInt("RegionId");
				// branch.regionName=resultSet.getString("RegionName");
				branch.branchAddress = resultSet.getString("BranchAddress");
				branch.status = resultSet.getBoolean("status");
				detail.add(branch);
				counter++;
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get all regions
	 */
	public List<Regions> getAllRegions() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRegions);
			resultSet = preparedStatement.executeQuery();
			List<Regions> details = new ArrayList<Regions>();
			while (resultSet.next()) {
				Regions region = new Regions();
				region.id = resultSet.getInt("ID");
				region.regionName = resultSet.getString("Name");
				region.active = resultSet.getBoolean("Active");

				details.add(region);
			}
			System.out.println("###id: " + details.get(1).id);
			System.out.println("###size: " + details.size());
			return details;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * create and update region
	 */
	@Override
	public ObjResponse createUpdateRegion(Regions region) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			connection = dataSource.getConnection();
			System.out.println("####region name: " + region.regionName);
			if (region.id == 0) {

				// validate

				if (checkRegionByName(region.regionName, region.id)) {
					return new ObjResponse(201,
							"Oops!! Region Name Already Exists, Please Try again");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertRegions);
				preparedStatement.setString(1, region.regionName);
				preparedStatement.setBoolean(2, region.active);
				preparedStatement.setString(3, region.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				System.out.println("###Name: " + region.regionName);
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateRegions);
				preparedStatement.setString(1, region.regionName);
				preparedStatement.setBoolean(2, region.active);
				preparedStatement.setString(3, region.createdBy);
				preparedStatement.setTimestamp(4, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(5, region.id);

			}
			return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
					200, "Records Updated") : new ObjResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new ObjResponse(202, "Exception Occurred,Please Try again");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(202, "Exception Occurred, Please Try again");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * check region name is exist or not
	 * 
	 * @param regionName
	 * @param id
	 * @return true if it exists
	 */
	private boolean checkRegionByName(String regionName, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRegionByName);
			preparedStatement.setString(1, regionName);
			preparedStatement.setInt(2, id);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return true;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get branch status
	 */
	public List<Branch> getBranchsByStatus() {
		logger.info("get BRANCHED");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getBranchsByStatus);
			preparedStatement.setBoolean(1, true);
			resultSet = preparedStatement.executeQuery();
			List<Branch> detail = new ArrayList<Branch>();
			while (resultSet.next()) {
				Branch branch = new Branch();
				branch.counter = counter;
				branch.branchId = resultSet.getInt("ID");
				branch.branchName = resultSet.getString("BranchName");
				branch.branchCode = resultSet.getString("BranchCode");
				branch.regionId = resultSet.getInt("RegionId");
				// branch.regionName=resultSet.getString("RegionName");
				branch.branchAddress = resultSet.getString("BranchAddress");
				branch.status = resultSet.getBoolean("status");
				detail.add(branch);
				counter++;
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get region status
	 */
	public List<Regions> getRegionsByStatus() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRegionsByStatus);
			preparedStatement.setBoolean(1, true);
			resultSet = preparedStatement.executeQuery();
			List<Regions> details = new ArrayList<Regions>();
			while (resultSet.next()) {
				Regions region = new Regions();
				region.id = resultSet.getInt("ID");
				region.regionName = resultSet.getString("Name");
				region.active = resultSet.getBoolean("Active");

				details.add(region);
			}
			System.out.println("###id: " + details.get(1).id);
			System.out.println("###size: " + details.size());
			return details;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}
}
