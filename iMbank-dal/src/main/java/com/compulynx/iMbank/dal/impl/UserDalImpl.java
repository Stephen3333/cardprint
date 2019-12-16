package com.compulynx.iMbank.dal.impl;

import com.compulynx.iMbank.dal.UserDal;
import com.compulynx.iMbank.dal.operations.AES;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.LoginTypes;
import com.compulynx.iMbank.models.ObjResponse;
import com.compulynx.iMbank.models.User;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDalImpl implements UserDal {

	// Logger logger = Logger.getLogger(L)

	private DataSource dataSource;

	public UserDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * check user name is exist or not return true if it exist
	 */
	public boolean checkUserName(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserByName);
			preparedStatement.setString(1, userName);

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
	 * check member is exists or not
	 * 
	 * @param userLinkedID
	 * @return true if exists
	 */
	public boolean checkMemberExists(long userLinkedID) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getMemberByLinkId);
			preparedStatement.setLong(1, userLinkedID);

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
	 * create and update user
	 */
	public ObjResponse UpdateUser(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet3 = null;
		String currentPword = "";

		try {

			connection = dataSource.getConnection();
			connection.setAutoCommit(false);
			if (user.userId == 0) {
				if (checkUserName(user.userName.trim())) {
					return new ObjResponse(201, "User Name Already Exists");
				}
				if (checkPayRollNumber(user.payrollNo)) {
					return new ObjResponse(201, "Payroll already exists");
				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.insertUser);
				// added for user code naming
				preparedStatement.setString(1, user.firstName);
				preparedStatement.setString(2, user.middleName);
				preparedStatement.setString(3, user.lastName);
				if (user.middleName.trim().equals("")) {
					user.userName = user.firstName.substring(0, 1)
							+ user.lastName;

				} else {
					user.userName = user.firstName.substring(0, 1)
							+ user.middleName.substring(0, 1) + user.lastName;

				}
				preparedStatement.setString(4, user.userName);
				// preparedStatement.setString(4, user.userFullName);
				preparedStatement.setString(5, AES.encrypt(user.userPwd));
				preparedStatement.setInt(6, user.userGroupId);
				preparedStatement.setString(7, user.userEmail);
				preparedStatement.setString(8, user.userPhone);
				preparedStatement.setLong(9, user.userLinkedID);
				preparedStatement.setBoolean(10, user.active);
				preparedStatement.setInt(11, user.createdBy);
				preparedStatement.setTimestamp(12, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setString(13, user.payrollNo);
				// preparedStatement.setString(12,user.pfId);
				preparedStatement.setInt(14, user.branchId);
				preparedStatement.setString(15, "NA");
			} else {
				// check existing pwords

				// get current pword
				preparedStatement3 = connection
						.prepareStatement(Queryconstants.getCurrentPassword);
				preparedStatement3.setInt(1, user.userId);
				resultSet3 = preparedStatement3.executeQuery();
				while (resultSet3.next()) {
					currentPword = AES.decrypt(resultSet3.getString("UsrPwd"));
				}
				if (!user.userPwd.equals(currentPword)) {
					if (!validatePassword(user.userPwd, user.userId)) {
						return new ObjResponse(
								201,
								"Password Policy Validation failed,make sure you have not used this password before and it contains alpha"
										+ ",numeric ,special characters & it has minimum length of 8 characters");
					}
					preparedStatement = connection
							.prepareStatement(Queryconstants.saveOldPassword);
					preparedStatement.setInt(1, user.userId);
					preparedStatement.setString(2, AES.encrypt(user.userPwd));
					if (preparedStatement.executeUpdate() <= 0) {
						throw new Exception(
								"Failed to Save Old Password for user id "
										+ user.userId);
					}

				}

				preparedStatement = connection
						.prepareStatement(Queryconstants.updateUser);
				System.out.println("Query##" + Queryconstants.updateUser);
				preparedStatement.setString(1, user.firstName);

				if (user.middleName == null) {
					user.userName = user.firstName.substring(0, 1)
							+ user.lastName;
					preparedStatement.setString(2, "");
				} else {
					user.userName = user.firstName.substring(0, 1)
							+ user.middleName.substring(0, 1) + user.lastName;
					preparedStatement.setString(2, user.middleName);
				}
				preparedStatement.setString(3, user.lastName);
				preparedStatement.setString(4, user.userName);
				// preparedStatement.setString(5, user.userFullName);
				preparedStatement.setString(5, AES.encrypt(user.userPwd));
				preparedStatement.setInt(6, user.userGroupId);
				preparedStatement.setString(7, user.userEmail);
				preparedStatement.setString(8, user.userPhone);
				preparedStatement.setLong(9, user.userLinkedID);
				preparedStatement.setBoolean(10, user.active);
				preparedStatement.setInt(11, user.createdBy);
				preparedStatement.setTimestamp(12, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				// changed isFirstLogin from true to false
				preparedStatement.setBoolean(13, false);
				preparedStatement.setString(14, user.payrollNo);
				// preparedStatement.setString(13,user.pfId);
				preparedStatement.setInt(15, user.branchId);
				// added time for LastPasswordModifiedOn
				preparedStatement.setTimestamp(16, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(17, user.userId);

			}

			if (preparedStatement.executeUpdate() > 0) {
				// /check for pword change
				/*
				 * if (!user.userPwd.equals(currentPword)) { if
				 * (!validatePassword(user.userPwd, user.userId)) { return new
				 * ObjResponse( 201,
				 * "Password Policy Validation failed,make sure you have not used this password before and it contains alpha"
				 * +
				 * ",numeric ,special characters & it has minimum length of 8 characters"
				 * ); } preparedStatement = connection
				 * .prepareStatement(Queryconstants.saveOldPassword);
				 * preparedStatement.setInt(1, user.userId);
				 * preparedStatement.setString(2, AES.encrypt(user.userPwd)); if
				 * (preparedStatement.executeUpdate() <= 0) { throw new
				 * Exception( "Failed to Save Old Password for user id " +
				 * user.userId); }
				 * 
				 * }
				 */
				// added for inserting loggerinfo
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertLoggerInfo);
				preparedStatement.setString(1, "user");
				preparedStatement.setInt(2, user.userId);
				preparedStatement.setString(3, user.userName);
				preparedStatement.setInt(4, user.branchId);
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
			 * 200, "Records Updated") : new ObjResponse(202,
			 * "Nothing To Update");
			 */

		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			if (sqlEx.getMessage().indexOf("Cannot insert duplicate key") != 0) {
				return new ObjResponse(201, "User Name Already Exists");
			} else {
				return new ObjResponse(203, "Exception Occurred");
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ObjResponse(203, "Exception Occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * checking pay roll no exists or not
	 * 
	 * @param payrollNo
	 * @return true if it exist
	 */
	private boolean checkPayRollNumber(String payrollNo) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserByPayRollNumber);
			preparedStatement.setString(1, payrollNo);

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
	 * validating password
	 * 
	 * @param password
	 * @param userId
	 * @return
	 */
	public boolean validatePassword(String password, int userId) {
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		boolean result = false;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getExistingPassword);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				String oldpassword = AES.decrypt(resultSet
						.getString("Password"));
				if (password.equals(oldpassword)) {
					System.out.println("in edit user,pwd equals old pwd");
					return false;
				}
			}

			result = password.matches(pattern);

		} catch (Exception ex) {

			result = false;
		}

		return result;
	}

	/**
	 * get all users by using id
	 */
	public List<User> GetAllUsers(int LinkId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		// int counter=1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUsers);
			preparedStatement.setInt(1, LinkId);

			resultSet = preparedStatement.executeQuery();

			List<User> users = new ArrayList<User>();
			while (resultSet.next()) {
				User user = new User();
				// user.counter=counter;
				user.userId = resultSet.getInt("ID");
				user.userName = resultSet.getString("UsrName");
				user.userFullName = resultSet.getString("UsrFullName");
				user.userPwd = AES.decrypt(resultSet.getString("UsrPwd"));
				user.userGroupId = resultSet.getInt("UsrGroupID");
				user.userEmail = resultSet.getString("UserEmail");
				user.userPhone = resultSet.getString("UserPhone").trim();
				user.userLinkedID = resultSet.getInt("UserLinkedID");
				user.active = resultSet.getBoolean("active");
				user.createdBy = resultSet.getInt("CreatedBy");
				user.payrollNo = resultSet.getString("payrollNumber");
				// user.pfId=resultSet.getString("pfId");
				user.branchId = resultSet.getInt("branchId");
				user.respCode = 200;
				users.add(user);
				// counter++;

			}
			return users;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get all branches
	 */
	public List<User> GetClasses() {

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getClasses);
			resultSet = preparedStatement.executeQuery();
			List<User> classes = new ArrayList<User>();
			while (resultSet.next()) {
				classes.add(new User(resultSet.getInt("ID"), resultSet
						.getString("BranchName"), 200));
			}
			return classes;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get questions
	 */
	public List<User> GetQuestions() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getQuestions);

			resultSet = preparedStatement.executeQuery();
			List<User> questions = new ArrayList<User>();
			while (resultSet.next()) {
				questions.add(new User(resultSet.getString("Question"),
						resultSet.getInt("ID")));
			}
			return questions;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get user by using id
	 */
	public User GetUserById(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserById);
			preparedStatement.setInt(1, userId);

			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				User user = new User();
				user.userId = resultSet.getInt("ID");
				user.firstName = resultSet.getString("FirstName");
				user.middleName = resultSet.getString("MiddleName");
				user.lastName = resultSet.getString("LastName");
				user.userName = resultSet.getString("UsrName");
				user.userFullName = resultSet.getString("UsrFullName");
				user.userPwd = AES.decrypt(resultSet.getString("UsrPwd"));
				user.userGroupId = resultSet.getInt("UsrGroupID");
				user.userEmail = resultSet.getString("UserEmail");
				user.userPhone = resultSet.getString("UserPhone");
				user.userLinkedID = resultSet.getInt("UserLinkedID");
				user.active = resultSet.getBoolean("active");
				user.active = resultSet.getBoolean("active");
				user.createdBy = resultSet.getInt("CreatedBy");
				user.userGroupName = resultSet.getString("GroupName");
				user.payrollNo = resultSet.getString("payrollNumber");
				// user.pfId=resultSet.getString("pfId");
				user.branchId = resultSet.getInt("branchId");
				user.respCode = 200;

				return user;
			} else {
				return new User(201);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/*
	 * Get Login Type List
	 */
	public List<LoginTypes> getLoginTypesList() {
		System.out.println("getting login types");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getLoginTypes);

			resultSet = preparedStatement.executeQuery();
			List<LoginTypes> detail = new ArrayList<LoginTypes>();
			while (resultSet.next()) {
				LoginTypes dtl = new LoginTypes();
				dtl.id = resultSet.getInt("id");
				dtl.value = resultSet.getBoolean("value");
				dtl.name = resultSet.getString("name");
				detail.add(dtl);
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
	 * get all users
	 */
	public List<User> GetAllUsers() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSet2 = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.users);
			resultSet = preparedStatement.executeQuery();
			List<User> usr = new ArrayList<User>();
			while (resultSet.next()) {
				User user = new User();
				user.counter = counter;
				user.userId = resultSet.getInt("ID");
				user.firstName = resultSet.getString("FirstName");
				user.middleName = resultSet.getString("MiddleName");
				user.lastName = resultSet.getString("LastName");
				user.userName = resultSet.getString("UsrName");
				user.userFullName = resultSet.getString("UsrFullName");
				user.userPwd = AES.decrypt(resultSet.getString("UsrPwd"));
				user.userGroupId = resultSet.getInt("UsrGroupID");
				user.userEmail = resultSet.getString("UserEmail");
				user.userPhone = resultSet.getString("UserPhone").trim();
				user.userLinkedID = resultSet.getInt("UserLinkedID");
				user.active = resultSet.getBoolean("active");
				user.createdBy = resultSet.getInt("CreatedBy");
				user.payrollNo = resultSet.getString("payrollNumber");
				// user.pfId=resultSet.getString("pfId");
				user.branchId = resultSet.getInt("branchId");
				user.respCode = 200;
				usr.add(user);
				counter++;
			}
			return usr;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	public ObjResponse resetFailAttempts(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.resetAttempts);
			preparedStatement.setInt(1, user.pwdAttempts);
			preparedStatement.setBoolean(2, user.isLocked);
			preparedStatement.setInt(3, user.createdBy);
			preparedStatement.setTimestamp(4, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setBoolean(5, true);
			preparedStatement.setString(6, user.userName);
			// System.out.println("###reached"+user.pwdAttempts);

			return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
					200, "Records Updated") : new ObjResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new ObjResponse(201, "Exception Occurred");

		}

	}

	/**
	 * unlock super user
	 * 
	 * @param user
	 * @return
	 */
	public ObjResponse unlockUser(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.unlockUser);
			preparedStatement.setBoolean(1, user.isLocked);
			preparedStatement.setTimestamp(2, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setString(3, user.userName);
			return (preparedStatement.executeUpdate() > 0) ? new ObjResponse(
					200, "Records Updated") : new ObjResponse(201,
					"Nothing To Update");
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();

			return new ObjResponse(201, "Exception Occurred");
		}

	}

	public List<User> getUserAttempts() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		int counter = 1;
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserAttempts);
			preparedStatement.setBoolean(1, true);

			resultSet = preparedStatement.executeQuery();
			List<User> users = new ArrayList<User>();
			while (resultSet.next()) {
				User user = new User();
				user.counter = counter;
				user.userId = resultSet.getInt("ID");
				user.userName = resultSet.getString("UsrName");
				user.userPwd = AES.decrypt(resultSet.getString("UsrPwd"));
				user.pwdAttempts = resultSet.getInt("PwdAttempts");
				user.isLocked = resultSet.getBoolean("isLock");
				user.respCode = 200;

				users.add(user);
				counter++;
			}
			return users;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}
}
