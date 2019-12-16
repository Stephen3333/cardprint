package com.compulynx.iMbank.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.compulynx.iMbank.dal.CardPrintDal;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.CardPrint;
import com.compulynx.iMbank.models.ObjResponse;

public class CardPrintDalImpl implements CardPrintDal {
	Logger logger = Logger.getLogger(CardPrintDalImpl.class.getCanonicalName());
	private DataSource dataSource;

	public CardPrintDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * gets all accounts from the db
	 * 
	 * @param userId
	 * @return list of accounts
	 */
	public List<CardPrint> getAccDetails(String userId) {
		logger.info("get Acc");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getCard);
			resultSet = preparedStatement.executeQuery();
			List<CardPrint> detail = new ArrayList<CardPrint>();
			while (resultSet.next()) {
				CardPrint cardprint = new CardPrint();
				cardprint.id = resultSet.getInt("ID");
				cardprint.cardNumber = resultSet.getString("CardNumber");
				cardprint.accountName = resultSet.getString("AccountName");
				cardprint.accountNumber = resultSet.getString("AccountNumber");
				cardprint.printedBy = resultSet.getInt("PrintedBy");
				cardprint.printerSerialNo = resultSet
						.getString("PrinterSerialNo");
				cardprint.printStatus = resultSet.getBoolean("PrintStatus");
				cardprint.branchPrinted = resultSet.getInt("BranchPrinted");
				cardprint.datePrinted = resultSet.getString("DatePrinted");
				cardprint.cardFormatId = resultSet.getString("CardFormatId");
				cardprint.printerId = resultSet.getString("PrinterTypeId");
				// cardprint.usrId =resultSet.getInt("UsrId");
				detail.add(cardprint);
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
	 * update card print status in db after printing
	 * 
	 * @param cardprint
	 * @return success
	 */
	public ObjResponse createUpdateCard(CardPrint cardprint) {
		System.out.println("Create Update Card##"+cardprint.accountName);
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			System.out
					.println("checking card exists: " + cardprint.accountName);
			if (cardprint.id == 0) {
				/*
				 * if (checkCardExists(cardprint.accountName)) { return new
				 * ObjResponse(201,
				 * "Oops!! This Account Name Has Already Been Printed, Do You want To Reprint?"
				 * ); }
				 */
				preparedStatement = connection
						.prepareStatement(Queryconstants.createCard);
				preparedStatement.setString(1, cardprint.cardNumber);
				preparedStatement.setString(2, cardprint.accountName);
				preparedStatement.setString(3, cardprint.accountNumber);
				preparedStatement.setInt(4, cardprint.printedBy);
				// preparedStatement.setString(5,"R");
				preparedStatement.setString(5, cardprint.printerSerialNo);
				preparedStatement.setBoolean(6, cardprint.printStatus);
				preparedStatement.setInt(7, cardprint.branchPrinted);
				preparedStatement.setTimestamp(8, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(9, cardprint.cardFormatId);
				preparedStatement.setString(10, cardprint.printerId);

			} else {

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCard);
				preparedStatement.setString(1, cardprint.cardNumber);
				preparedStatement.setString(2, cardprint.accountName);
				preparedStatement.setString(3, cardprint.accountNumber);
				preparedStatement.setInt(4, cardprint.printedBy);
				// preparedStatement.setString(5, "1");
				preparedStatement.setString(5, cardprint.printerSerialNo);
				preparedStatement.setBoolean(6, cardprint.printStatus);
				preparedStatement.setInt(7, cardprint.branchPrinted);
				preparedStatement.setTimestamp(8, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(9, cardprint.cardFormatId);
				preparedStatement.setString(10, cardprint.printerId);
				preparedStatement.setInt(11, cardprint.id);

				/*// added for inserting loggerinfo
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertLoggerInfo);
				preparedStatement.setString(1, "cardprint");
				preparedStatement.setInt(2, cardprint.id);
				preparedStatement.setString(3, cardprint.accountName);
				preparedStatement.setInt(4, cardprint.branchPrinted);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.executeUpdate();*/
				

			}
			if (preparedStatement.executeUpdate() > 0) {
				connection.commit();
				return new ObjResponse(200, "Records Updated");
			} else {
				connection.rollback();
				return new ObjResponse(202, "Nothing To Update");
			}
			/*
			 * return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
			 * 200, "Card Print Saved Successfully") : new ObjResponse( 201,
			 * "Nothing To Save");
			 */
		}

		catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new ObjResponse(202, "Exception Occurred");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(202, "Exception Occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

}
