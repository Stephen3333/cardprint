package com.compulynx.iMbank.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.chart.BarRecord;

import com.compulynx.iMbank.dal.CurrentBalanceDal;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.CardInventory;
import com.compulynx.iMbank.models.CurrentBalance;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.User;

public class CurrentBalanceDalImpl implements CurrentBalanceDal {

	Logger logger = Logger.getLogger(CurrentBalanceDalImpl.class
			.getCanonicalName());
	private DataSource dataSource;

	public CurrentBalanceDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * get all current balance of cards
	 */
	public List<CurrentBalance> getAllCurrentBalance() {
		logger.info("get currentbalance listings");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getCurrentBalance);

			resultSet = preparedStatement.executeQuery();
			List<CurrentBalance> detail = new ArrayList<CurrentBalance>();
			while (resultSet.next()) {
				CurrentBalance currentbalance = new CurrentBalance();
				currentbalance.id = resultSet.getInt("ID");
				currentbalance.branchId = resultSet.getInt("BranchId");
				currentbalance.currentBalance = resultSet
						.getString("CurrentBalance");
				currentbalance.lastUpdate = resultSet.getString("LastUpdate");

				detail.add(currentbalance);
				System.out.println("###: " + currentbalance.currentBalance);

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
	 * create and update current balance
	 */
	@Override
	public ObjResponse updateCurrentBalance(CurrentBalance currentbalance) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int userId = 0;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.updateCurrentBalance);
			preparedStatement.setInt(1, currentbalance.branchId);
			System.out.println("branch@@@" + currentbalance.branchId);

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
