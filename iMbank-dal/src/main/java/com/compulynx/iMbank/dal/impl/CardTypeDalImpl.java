package com.compulynx.iMbank.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;

import com.compulynx.iMbank.dal.CardTypeDal;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.CardType;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Printer;
import com.compulynx.iMbank.models.PrinterType;

public class CardTypeDalImpl implements CardTypeDal {

	Logger logger = Logger.getLogger(CardTypeDalImpl.class.getCanonicalName());
	private DataSource dataSource;

	public CardTypeDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * create and update card type
	 */

	public ObjResponse createUpdateCardType(CardType cardtype) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {

			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (cardtype.id == 0) {

				// validate
				if (checkName(cardtype.name)) {
					return new ObjResponse(201,
							"Oops!! format name Already Exists, Please Try again");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.createCardType);
				preparedStatement.setString(1, cardtype.code);
				preparedStatement.setString(2, cardtype.name);
				preparedStatement.setString(3, cardtype.description);
				preparedStatement.setInt(4, cardtype.xCoordinates);
				preparedStatement.setInt(5, cardtype.yCoordinates);
				preparedStatement.setString(6, cardtype.fontname);
				preparedStatement.setInt(7, cardtype.fontsize);
				preparedStatement.setInt(8, cardtype.fontcolor);

			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateCardType);
				preparedStatement.setString(1, cardtype.code);
				preparedStatement.setString(2, cardtype.name);
				preparedStatement.setString(3, cardtype.description);
				preparedStatement.setInt(4, cardtype.xCoordinates);
				preparedStatement.setInt(5, cardtype.yCoordinates);
				preparedStatement.setString(6, cardtype.fontname);
				preparedStatement.setInt(7, cardtype.fontsize);
				preparedStatement.setInt(8, cardtype.fontcolor);
				preparedStatement.setInt(9, cardtype.id);

			}
			if (preparedStatement.executeUpdate() > 0) {
				// added for inserting loggerinfo
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertLoggerInfo);
				preparedStatement.setString(1, "cardtype");
				preparedStatement.setInt(2, cardtype.id);
				preparedStatement.setString(3, cardtype.name);
				preparedStatement.setInt(4, 0);
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
	 * check card type name is exist or not
	 * 
	 * @param Name
	 * @return true if it exits
	 */
	private boolean checkName(String Name) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkName);
			preparedStatement.setString(1, Name);

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
	 * get all card types
	 */
	public List<CardType> getAllCardType() {
		//logger.info("get cardtype listings");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getCardType);

			resultSet = preparedStatement.executeQuery();
			List<CardType> detail = new ArrayList<CardType>();
			while (resultSet.next()) {
				CardType cardtype = new CardType();
				cardtype.counter = counter;
				cardtype.id = resultSet.getInt("ID");
				cardtype.code = resultSet.getString("Code");
				cardtype.name = resultSet.getString("Name");
				cardtype.description = resultSet.getString("Description");
				cardtype.xCoordinates = resultSet.getInt("XCoordinates");
				cardtype.yCoordinates = resultSet.getInt("YCoordinates");
				cardtype.fontname = resultSet.getString("FontName");
				cardtype.fontsize = resultSet.getInt("FontSize");
				cardtype.fontcolor = resultSet.getInt("FontColor");

				detail.add(cardtype);
				counter++;
				//System.out.println("###cardtype name: " + cardtype.name);

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
	 * get all printer types
	 */
	public List<PrinterType> getAllPrinterType() {
		//logger.info("get printertype listings");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getPrinterType);
			resultSet = preparedStatement.executeQuery();
			List<PrinterType> detail = new ArrayList<PrinterType>();
			while (resultSet.next()) {
				PrinterType printertype = new PrinterType();
				printertype.counter = counter;
				printertype.id = resultSet.getInt("id");
				printertype.printerName = resultSet.getString("printerName");
				printertype.description = resultSet.getString("description");
				detail.add(printertype);
				counter++;
				/*System.out.println("###printertype name: "
						+ printertype.printerName);*/

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
	 * create and update printer type
	 */
	public ObjResponse createUpdatePrinterType(PrinterType printertype) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		ResultSet resultSet = null;
		try {

			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (printertype.id == 0) {

				// validate
				if (checkPrinterName(printertype.printerName)) {
					return new ObjResponse(201,
							"Oops!! printer name Already Exists, Please Try again");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.createPrinterType);
				preparedStatement.setString(1, printertype.printerName);
				preparedStatement.setString(2, printertype.description);

			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updatePrinterType);
				preparedStatement.setString(1, printertype.printerName);
				preparedStatement.setString(2, printertype.description);
				preparedStatement.setInt(3, printertype.id);

			}
			if (preparedStatement.executeUpdate() > 0) {
				// added for inserting loggerinfo
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.insertLoggerInfo);
				preparedStatement1.setString(1, "printertype");
				preparedStatement1.setInt(2, printertype.id);
				preparedStatement1.setString(3, printertype.printerName);
				preparedStatement1.setInt(4, 0);
				preparedStatement1.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement1.executeUpdate();
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

	private boolean checkPrinterName(String printerName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.checkPrinterName);
			preparedStatement.setString(1, printerName);

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

}
