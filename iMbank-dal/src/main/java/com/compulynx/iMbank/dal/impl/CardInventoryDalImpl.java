package com.compulynx.iMbank.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.compulynx.iMbank.dal.CardInventoryDal;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.CardReject;
import com.compulynx.iMbank.models.CardType;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.User;

public class CardInventoryDalImpl implements CardInventoryDal {
	Logger logger = Logger.getLogger(CardInventoryDalImpl.class
			.getCanonicalName());
	private DataSource dataSource;

	public CardInventoryDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * create and update card inventory
	 */
	public ObjResponse createUpdateCardInventory(CardInventory cardinventory) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// String regName=getBranchName(branch.regionId);
			connection = dataSource.getConnection();
			System.out.println("####branchid: " + cardinventory.id);
			if (cardinventory.id == 0) {

				preparedStatement = connection
						.prepareStatement(Queryconstants.createCardInventory);
				preparedStatement.setInt(1,
						Integer.valueOf(cardinventory.batchSize));
				preparedStatement.setString(2, cardinventory.batchNum);
				preparedStatement.setInt(3, cardinventory.branchId);
				preparedStatement.setInt(4, cardinventory.cardtypeId);
				preparedStatement.setString(5, "false");
				preparedStatement.setInt(6, cardinventory.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(8, cardinventory.receivedBy);
				preparedStatement.setString(9, cardinventory.receivedOn);
				preparedStatement.setString(10, cardinventory.comments);
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardInventory);
				preparedStatement.setString(1, cardinventory.batchSize);
				preparedStatement.setString(2, cardinventory.batchNum);
				preparedStatement.setInt(3, cardinventory.branchId);
				preparedStatement.setInt(4, cardinventory.cardtypeId);
				preparedStatement.setString(5, cardinventory.status);
				preparedStatement.setInt(6, cardinventory.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(8, cardinventory.id);
			}
			return (preparedStatement.execute()) ? new ObjResponse(200,
					"Records Updated") : new ObjResponse(201,
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
	 * checking batch size
	 * 
	 * @param batchSize
	 * @return
	 */
	private boolean checkBatchSize(String batchSize) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkBatchSize);
			preparedStatement.setString(1, batchSize);

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
	 * get all card inventory
	 */
	@Override
	public List<CardInventory> getAllCardInventory() {
		logger.info("get cardinventory listings");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			// preparedStatement =
			// connection.prepareStatement(Queryconstants.getCardInventory);
			preparedStatement = connection
					.prepareStatement(Queryconstants.CardInventory);

			resultSet = preparedStatement.executeQuery();
			List<CardInventory> detail = new ArrayList<CardInventory>();
			while (resultSet.next()) {
				CardInventory cardinventory = new CardInventory();
				cardinventory.id = resultSet.getInt("ID");
				cardinventory.batchSize = resultSet.getString("BatchSize");
				cardinventory.batchNum = resultSet.getString("BatchNumber");
				cardinventory.branchId = resultSet.getInt("BranchId");
				cardinventory.cardtypeId = resultSet.getInt("CardTypeId");
				cardinventory.status = resultSet.getString("Status");
				cardinventory.createdBy = resultSet.getInt("CreatedBy");
				cardinventory.createdOn = resultSet.getString("CreatedOn");
				cardinventory.receivedBy = resultSet.getString("ReceivedBy");
				cardinventory.receivedOn = resultSet.getString("ReceivedOn");
				cardinventory.comments = resultSet.getString("Comments");

				detail.add(cardinventory);
				System.out.println("###: " + cardinventory.status);

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
	 * update card inventory
	 */
	@Override
	public ObjResponse updateCardInventoryReceive(CardInventory cardinventory) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (cardinventory.id == 0) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.createCardInventory);
				preparedStatement.setString(1, cardinventory.batchSize);
				preparedStatement.setString(2, cardinventory.batchNum);
				preparedStatement.setInt(3, cardinventory.branchId);
				preparedStatement.setInt(4, cardinventory.cardtypeId);
				preparedStatement.setBoolean(5, false);
				preparedStatement.setInt(6, cardinventory.createdBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(8, cardinventory.receivedBy);
				preparedStatement.setString(9, cardinventory.receivedOn);
				preparedStatement.setString(10, cardinventory.comments);
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardInventoryReceive);
				preparedStatement.setString(1, cardinventory.batchSize);
				preparedStatement.setString(2, cardinventory.batchNum);
				preparedStatement.setInt(3, cardinventory.branchId);
				preparedStatement.setInt(4, cardinventory.cardtypeId);
				preparedStatement.setBoolean(5, true);
				preparedStatement.setString(6, cardinventory.receivedBy);
				preparedStatement.setTimestamp(7, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(8, cardinventory.comments);
				preparedStatement.setInt(9, cardinventory.id);
			}
			return (preparedStatement.execute()) ? new ObjResponse(200,
					"Records Updated") : new ObjResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new ObjResponse(201, "Exception Occurred");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(201, "Exception Occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get all card reject
	 */
	public List<CardReject> getAllCardRejects() {
		logger.info("get cardreject listings");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getCardRejection);

			resultSet = preparedStatement.executeQuery();
			List<CardReject> detail = new ArrayList<CardReject>();
			while (resultSet.next()) {
				CardReject cardreject = new CardReject();
				cardreject.id = resultSet.getInt("ID");
				cardreject.cardNum = resultSet.getInt("CardNum");
				cardreject.rejectedBy = resultSet.getString("RejectedBy");
				cardreject.branchId = resultSet.getInt("BranchId");
				cardreject.reason = resultSet.getString("Reason");
				cardreject.rejectedOn = resultSet.getString("RejectedOn");

				detail.add(cardreject);
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
	 * update card inventory reject
	 */
	public ObjResponse updateCardInventoryReject(CardReject cardreject) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			if (cardreject.id == 0) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.createCardRejection);
				preparedStatement.setInt(1, cardreject.cardNum);
				preparedStatement.setString(2, cardreject.rejectedBy);
				preparedStatement.setInt(3, cardreject.branchId);
				preparedStatement.setString(4, cardreject.reason);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));

			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardRejection);
				preparedStatement.setInt(1, cardreject.cardNum);
				preparedStatement.setString(2, cardreject.rejectedBy);
				preparedStatement.setInt(3, cardreject.branchId);
				preparedStatement.setString(4, cardreject.reason);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));

				preparedStatement.setInt(6, cardreject.id);
			}
			return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
					200, "Records Updated") : new ObjResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new ObjResponse(201, "Exception Occurred");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(201, "Exception Occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * update card stock
	 */
	public ObjResponse updateCardStock(CardInventory cardinventory) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.updateCardStock);
			preparedStatement.setString(1, cardinventory.batchSize);
			preparedStatement.setInt(2, cardinventory.branchId);

			return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
					200, "Records Updated") : new ObjResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new ObjResponse(201, "Exception Occurred");

		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(201, "Exception Occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}
}
