package com.compulynx.iMbank.dal.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.compulynx.iMbank.dal.UserGroupDal;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.Regions;
import com.compulynx.iMbank.models.Rights;
import com.compulynx.iMbank.models.RightsDetail;
import com.compulynx.iMbank.models.UserGroup;

public class UserGroupDalImpl implements UserGroupDal {

	private DataSource dataSource;

	public UserGroupDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * create and update user group
	 */
	@SuppressWarnings("resource")
	public ObjResponse UpdateUserGroup(UserGroup group) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet rs = null;
		int groupId = 0;
		try {
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);

			if (group.groupId == 0) {

				if (checkGroupByCode(group.groupCode, group.groupId)) {
					return new ObjResponse(201,
							"Oops!! Group Code Already Exists");
				} else if (checkGroupByName(group.groupName, group.groupId)) {
					return new ObjResponse(201, "Group Name is Already Exists");
				} else {
					preparedStatement = connection.prepareStatement(
							Queryconstants.insertUserGroup,
							Statement.RETURN_GENERATED_KEYS);
					preparedStatement.setString(1, group.groupCode);
					preparedStatement.setString(2, group.groupName);
					// preparedStatement.setInt(3, group.groupClassId);
					preparedStatement.setInt(3, group.groupLinkId);
					preparedStatement.setBoolean(4, group.active);
					preparedStatement.setInt(5, group.createdBy);
					preparedStatement.setTimestamp(6, new java.sql.Timestamp(
							new java.util.Date().getTime()));
				}

			} else {

				if (checkGroupByCode(group.groupCode, group.groupId)) {
					return new ObjResponse(201,
							"Oops!! Group Code Already Exists");
				}

				if (checkGroupByName(group.groupName, group.groupId)) {
					return new ObjResponse(201, "Group Name is Already Exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateUserGroup);
				preparedStatement.setString(1, group.groupCode);
				preparedStatement.setString(2, group.groupName);
				preparedStatement.setBoolean(3, group.active);
				preparedStatement.setInt(4, group.createdBy);
				preparedStatement.setTimestamp(5, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(6, group.groupId);
				groupId = group.groupId;

				
			}
			if (preparedStatement.executeUpdate() > 0) {

				// Dispose
				if (group.groupId == 0) {
					rs = preparedStatement.getGeneratedKeys();
					rs.next();
					groupId = rs.getInt(1);
				}
				DBOperations.DisposeSql(preparedStatement, rs);
				preparedStatement = connection
						.prepareStatement(Queryconstants.deleteGroupRights);
				preparedStatement.setInt(1, groupId);
				preparedStatement.executeUpdate();

				DBOperations.DisposeSql(preparedStatement);
				// insert rights
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertGroupRights);
				for (int i = 0; i < group.rights.size(); i++) {
					preparedStatement.setInt(1, group.rights.get(i).rightId);
					preparedStatement.setInt(2, groupId);
					preparedStatement.setBoolean(3,
							group.rights.get(i).rightView);
					preparedStatement.setBoolean(4,
							group.rights.get(i).rightAdd);
					preparedStatement.setBoolean(5,
							group.rights.get(i).rightEdit);
					preparedStatement.setBoolean(6,
							group.rights.get(i).rightDelete);
					preparedStatement.setInt(7, group.rights.get(i).createdBy);
					preparedStatement.setTimestamp(8, new java.sql.Timestamp(
							new java.util.Date().getTime()));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception("Failed to Insert Right Id "
								+ group.rights.get(i).rightId);
					}
				}
				// added for inserting loggerinfo
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertLoggerInfo);
				preparedStatement.setString(1, "group");
				preparedStatement.setInt(2, group.groupId);
				preparedStatement.setString(3, group.groupName);
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

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			/*
			 * if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") !=
			 * 0) { return new ObjResponse(201, "Group Name Already Exists"); }
			 * else { return new ObjResponse(202,
			 * "Oops!! Server Error Occurred"); }
			 */
			return new ObjResponse(202, "Oops!! Server Error Occurred");
		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return new ObjResponse(202, "Oops!! Server Error Occurred");
		} finally {
			DBOperations.DisposeSql(rs);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checking group name is exist or not
	 * 
	 * @param groupName
	 * @param id
	 * @return true if it exists
	 */
	public boolean checkGroupByName(String groupName, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupByName);
			preparedStatement.setString(1, groupName);
			preparedStatement.setInt(2, id);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * check group by using code
	 * 
	 * @param groupCode
	 * @param id
	 * @return true if it exists
	 */
	public boolean checkGroupByCode(String groupCode, int id) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupByCode);
			preparedStatement.setString(1, groupCode);
			preparedStatement.setInt(2, id);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get groups
	 */
	public List<UserGroup> GetGroups(int groupLink) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroups);
			preparedStatement.setInt(1, groupLink);

			resultSet = preparedStatement.executeQuery();
			List<UserGroup> groups = new ArrayList<UserGroup>();
			while (resultSet.next()) {
				UserGroup objGroup = new UserGroup();
				objGroup.counter = counter;
				objGroup.groupId = resultSet.getInt("ID");
				objGroup.groupCode = resultSet.getString("groupCode");
				objGroup.groupName = resultSet.getString("groupName");
				objGroup.groupClassId = resultSet.getInt("groupClassID");
				objGroup.groupLinkId = resultSet.getInt("groupLinkID");
				objGroup.active = resultSet.getBoolean("Active");
				objGroup.createdBy = resultSet.getInt("createdBy");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getGroupById);
				preparedStatement.setInt(1, objGroup.groupId);
				preparedStatement.setInt(2, objGroup.groupId);
				preparedStatement.setInt(3, objGroup.groupId);

				resultSet2 = preparedStatement.executeQuery();
				List<RightsDetail> rightList = new ArrayList<RightsDetail>();
				while (resultSet2.next()) {
					rightList
							.add(new RightsDetail(
									resultSet2.getInt("RightID"),
									resultSet2.getString("RightDisplayName"),
									resultSet2.getBoolean("RightAdd"),
									resultSet2.getBoolean("RightEdit"),
									resultSet2.getBoolean("RightDelete"),
									resultSet2.getBoolean("RightView"),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2.getBoolean("AllowAdd")),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2
													.getBoolean("AllowEdit")),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2
													.getBoolean("AllowDelete")),
									resultSet2.getBoolean("AllowView"), 200));
				}
				objGroup.rights = rightList;
				groups.add(objGroup);
				counter++;
			}

			return groups;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get groups by using id
	 */
	public UserGroup GetGroupById(int groupId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getGroupById);
			preparedStatement.setInt(1, groupId);
			preparedStatement.setInt(2, groupId);
			preparedStatement.setInt(3, groupId);
			preparedStatement.setInt(4, groupId);
			preparedStatement.setInt(5, groupId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				return new UserGroup(resultSet.getInt("ID"),
						resultSet.getString("groupCode"),
						resultSet.getString("groupName"),
						resultSet.getInt("groupClassID"),
						resultSet.getInt("groupLinkID"),
						resultSet.getBoolean("active"),
						resultSet.getInt("createdBy"), 200);
			} else {
				return new UserGroup(201, "Group Not Found");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new UserGroup(202, "Oops!! Server Error Occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get rights
	 */
	public List<RightsDetail> GetRights() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRights);
			resultSet = preparedStatement.executeQuery();
			List<RightsDetail> rights = new ArrayList<RightsDetail>();
			while (resultSet.next()) {
				rights.add(new RightsDetail(resultSet.getInt("ID"), resultSet
						.getString("RightDisplayName"), resultSet
						.getBoolean("AllowView"), ((resultSet
						.getBoolean("AllowView") == false) ? false : resultSet
						.getBoolean("AllowAdd")), ((resultSet
						.getBoolean("AllowView") == false) ? false : resultSet
						.getBoolean("AllowEdit")), ((resultSet
						.getBoolean("AllowView") == false) ? false : resultSet
						.getBoolean("AllowDelete")), 200

				));
			}
			return rights;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * get rights
	 */
	public List<RightsDetail> GetAllRights() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int count=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getRights);
			resultSet = preparedStatement.executeQuery();
			List<RightsDetail> rights = new ArrayList<RightsDetail>();
			while (resultSet.next()) {
				rights.add(new RightsDetail(resultSet.getInt("ID"),
						resultSet
						.getString("rightname"),
						resultSet
						.getString("rightviewname"),
						resultSet
						.getString("RightDisplayName"),
						resultSet
						.getInt("rightheaderid"), 200,count

				));
				count++;
			}
			return rights;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * get active groups
	 */
	public List<UserGroup> getActiveGroups(int groupLink) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getActiveGroups);
			preparedStatement.setInt(1, groupLink);
			preparedStatement.setBoolean(2, true);

			resultSet = preparedStatement.executeQuery();
			List<UserGroup> groups = new ArrayList<UserGroup>();
			while (resultSet.next()) {
				UserGroup objGroup = new UserGroup();
				objGroup.groupId = resultSet.getInt("ID");
				objGroup.groupCode = resultSet.getString("groupCode");
				objGroup.groupName = resultSet.getString("groupName");
				objGroup.groupClassId = resultSet.getInt("groupClassID");
				objGroup.groupLinkId = resultSet.getInt("groupLinkID");
				objGroup.active = resultSet.getBoolean("Active");
				objGroup.createdBy = resultSet.getInt("createdBy");
				preparedStatement = connection
						.prepareStatement(Queryconstants.getGroupById);
				preparedStatement.setInt(1, objGroup.groupId);
				preparedStatement.setInt(2, objGroup.groupId);
				preparedStatement.setInt(3, objGroup.groupId);

				resultSet2 = preparedStatement.executeQuery();
				List<RightsDetail> rightList = new ArrayList<RightsDetail>();
				while (resultSet2.next()) {
					rightList
							.add(new RightsDetail(
									resultSet2.getInt("RightID"),
									resultSet2.getString("RightDisplayName"),
									resultSet2.getBoolean("RightAdd"),
									resultSet2.getBoolean("RightEdit"),
									resultSet2.getBoolean("RightDelete"),
									resultSet2.getBoolean("RightView"),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2.getBoolean("AllowAdd")),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2
													.getBoolean("AllowEdit")),
									((resultSet2.getBoolean("AllowView") == false) ? false
											: resultSet2
													.getBoolean("AllowDelete")),
									resultSet2.getBoolean("AllowView"), 200));
				}
				objGroup.rights = rightList;
				groups.add(objGroup);
			}
			return groups;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(resultSet2);
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * get all regions
	 */
	public List<Rights> getHeaders() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getHeaders);
			resultSet = preparedStatement.executeQuery();
			List<Rights> details = new ArrayList<Rights>();
			while (resultSet.next()) {
				Rights region = new Rights();
				region.headerId = resultSet.getInt("ID");
				region.headerName = resultSet.getString("headerName");
			
				details.add(region);
			}
		
			return details;
		} catch (Exception e) {
			e.printStackTrace();
			return null;

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
	/**
	 * create and update Rights
	 */
	public ObjResponse UpdateRight(RightsDetail right) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			
			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			
			if (right.rightId == 0) {

				// validate

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertRights);
				preparedStatement.setString(1, right.rightName);
				preparedStatement.setBoolean(2, true);
				preparedStatement.setBoolean(3, true);
				preparedStatement.setBoolean(4, true);
				preparedStatement.setBoolean(5, true);
				preparedStatement.setInt(6, 4);
				preparedStatement.setString(7, right.rightViewName);
				preparedStatement.setInt(8, right.headerId);
				preparedStatement.setString(9, right.rightDisplayName);
				preparedStatement.setString(10, "RGT");
				preparedStatement.setInt(11, 0);
				preparedStatement.setInt(12, right.createdBy);
				preparedStatement.setTimestamp(13, new java.sql.Timestamp(
						new java.util.Date().getTime()));
			
			} else {
				preparedStatement = connection
						.prepareStatement(Queryconstants.updateRights);
				preparedStatement.setString(1, right.rightName);
				preparedStatement.setBoolean(2, true);
				preparedStatement.setBoolean(3, true);
				preparedStatement.setBoolean(4, true);
				preparedStatement.setBoolean(5, true);
				preparedStatement.setInt(6, 4);
				preparedStatement.setString(7, right.rightViewName);
				preparedStatement.setInt(8, right.headerId);
				preparedStatement.setString(9, right.rightDisplayName);
				preparedStatement.setString(10, "RGT");
				preparedStatement.setInt(11, 0);
				preparedStatement.setInt(12, right.createdBy);
				preparedStatement.setTimestamp(13, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(14, right.rightId);
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
}
