package com.compulynx.iMbank.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.iMbank.dal.DashBoardDal;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.DashBoard;

public class DashBoardDalImpl implements DashBoardDal {

	private DataSource dataSource;

	public DashBoardDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * get dash board details
	 */
	public List<DashBoard> GetDashBoardCountDetail() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getDashBoardDetailCount);

			resultSet = preparedStatement.executeQuery();
			List<DashBoard> detail = new ArrayList<DashBoard>();
			while (resultSet.next()) {
				detail.add(new DashBoard(resultSet.getInt("COUNTNO"), resultSet
						.getString("Name"), 200));
			}
			return detail;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	public List<DashBoard> GetTransChartDetail() {
		/*
		 * Connection connection = null; PreparedStatement preparedStatement =
		 * null; ResultSet resultSet = null; try { connection =
		 * dataSource.getConnection(); preparedStatement = connection
		 * .prepareStatement(Queryconstants.getTransChartDetail);
		 * 
		 * resultSet = preparedStatement.executeQuery(); List<DashBoard>
		 * chartDetail = new ArrayList<DashBoard>(); while (resultSet.next()) {
		 * chartDetail.add(new DashBoard(resultSet.getInt("AUTHUSER"),
		 * resultSet.getInt("CANCELUSER"), resultSet.getInt("TOTALTRANS"),
		 * resultSet .getString("month"), 200)); } return chartDetail; } catch
		 * (Exception ex) { ex.printStackTrace(); return null; } finally {
		 * DBOperations.DisposeSql(connection, preparedStatement, resultSet); }
		 */
		return null;
	}

}
