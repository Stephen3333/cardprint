package com.compulynx.iMbank.dal.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import org.apache.commons.mail.*;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import com.compulynx.iMbank.dal.LoginDal;
import com.compulynx.iMbank.dal.operations.AES;
import com.compulynx.iMbank.dal.operations.DBOperations;
import com.compulynx.iMbank.dal.operations.Queryconstants;
import com.compulynx.iMbank.models.Branch;
import com.compulynx.iMbank.models.CardType;
import com.compulynx.iMbank.models.LoginSession;
import com.compulynx.iMbank.models.LoginUser;
import com.compulynx.iMbank.models.Rights;
import com.compulynx.iMbank.models.RightsDetail;
import com.compulynx.iMbank.models.User;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.sql.DataSource;

public class LoginDalImpl implements LoginDal {

	// Logger logger= Logger.getLogger(LoginDalImpl.class.getName());
	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(LoginDalImpl.class);

	private DataSource dataSource;

	public LoginDalImpl(DataSource dataSource) {
		super();
		this.dataSource = dataSource;
	}

	/**
	 * get user credentials for login
	 */
	@SuppressWarnings("resource")
	public LoginUser GetUserIdManual(String userName, String password) {
		logger.info("Manual Auth");
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement preparedStatement1 = null;
		PreparedStatement preparedStatement2 = null;
		PreparedStatement preparedStatement3 = null;
		ResultSet resultSet = null;
		ResultSet resultSet1 = null;
		ResultSet resultSet2 = null;

		User user = new User();

		try {

			connection = dataSource.getConnection();

			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserCredentialManual);
			// userName =
			preparedStatement.setString(1, userName);
			preparedStatement.setString(2, AES.encrypt(password));
			preparedStatement.setBoolean(3, false);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int userId = 0;
				int branchId = 0;
				userId = resultSet.getInt("UserID");
				branchId = resultSet.getInt("branchId");
				logger.info("UserID>>" + userId);

				if (!resultSet.getBoolean("Active")) {
					return new LoginUser(201, "Sorry! Account is not active");
				}

				if (resultSet.getBoolean("isFirstLogin")) {
					return new LoginUser(203, "" + userId);
				}
				// added for getting how many times user loggedin
				preparedStatement = connection
						.prepareStatement(Queryconstants.insertLoginDetails);
				preparedStatement.setInt(1, userId);
				preparedStatement.setString(2, userName);
				preparedStatement.setTimestamp(3, new java.sql.Timestamp(
						new java.util.Date().getTime()));
				preparedStatement.setInt(4, branchId);
				preparedStatement.executeUpdate();
				// added for password expire
				String startdate = resultSet
						.getString("LastPasswordModifiedOn");
				DateTimeFormatter formatter = DateTimeFormat
						.forPattern("yyyy-MM-dd HH:mm:ss.SSS");
				DateTime dateTime = formatter.parseDateTime(startdate);
				DateTime present = new DateTime();
				// long diffInMillis = present.getMillis() -
				// dateTime.getMillis();
				System.out.println("present" + present);
				System.out.println(dateTime);
				// System.out.println("diff"+diffInMillis);
				Days expire = Days.daysBetween(dateTime, present);
				int days = expire.getDays();
				System.out.println("expire" + expire);
				System.out.println("days=" + days);
				if (days > 30) {
					return new LoginUser(203, "" + userId);
				} else if (days > 25) {

					logger.info("UserID>>" + userId);
					return new LoginUser(
							userId,
							204,
							"Your Account password is going to be expire please change password",
							branchId);
				}
				preparedStatement = connection.prepareStatement(Queryconstants.resetPasswordAttempts);
			    preparedStatement.setString(1, userName);
			    preparedStatement.executeUpdate();
			    
				return new LoginUser(userId, 200, "success", branchId);
			} else {
				// System.out.println("pword" +
				// AES.decrypt("dbrjzE1A7vN-fGa826_Llw"));
				// added for user blocking after 3 wrong password attempts
				preparedStatement1 = connection
						.prepareStatement(Queryconstants.getUserDetails);
				preparedStatement1.setString(1, userName);
				resultSet1 = preparedStatement1.executeQuery();
				if (resultSet1.next()) {
					user.userId = resultSet1.getInt("ID");
					user.userName = resultSet1.getString("UsrName");
					user.userPwd = AES.decrypt(resultSet1.getString("UsrPwd"));
					user.pwdAttempts = resultSet1.getInt("PwdAttempts");
					// }
					// for(int i=0; i<= 3; i++){
					if (user.userPwd != password) {
						System.out
								.println("password" + password + user.userPwd);

						connection = dataSource.getConnection();
						preparedStatement = connection
								.prepareStatement(Queryconstants.updateAttempts);
						// preparedStatement.setInt(1, user.pwdAttempts+1);
						preparedStatement.setString(1, user.userName);
						preparedStatement.executeUpdate();
						// return new LoginUser(201,"login attempt failed at ");
					}

					// }
				}
				preparedStatement2 = connection
						.prepareStatement(Queryconstants.selectAttempts);
				preparedStatement2.setString(1, userName);
				resultSet2 = preparedStatement2.executeQuery();
				if (resultSet2.next()) {
					user.pwdAttempts = resultSet2.getInt("PwdAttempts");

					if (resultSet2.getInt("PwdAttempts") > 4) {
						preparedStatement2 = connection
								.prepareStatement(Queryconstants.updateUserLock);
						preparedStatement2.setBoolean(1, true);
						preparedStatement2.setString(2, user.userName);
						preparedStatement2.executeUpdate();
						if (preparedStatement2.executeUpdate() <= 0) {
							throw new Exception("Failed to lock user account"
									+ user.userId);
						}

						return new LoginUser(201,
								"Your User Account is locked please contact administration!");
					} else {
						return new LoginUser(201,
								"Oops! Your account password is wrong please enter correct password");
					}
				}

				return new LoginUser(201,
						"Oops! You specified invalid login credentials, Please try again.");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(202, "Server error occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * forgot password function if you click forgot password button it will send
	 * random number to mail, by using that random number you can login
	 */
	public LoginUser ForgotPassword(String userName) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		String password = "";
		String emailAddress = "";
		logger.info("In Forgot Password");
		String aToZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"; // 36 letter.
		Random rand = new Random();
		StringBuilder res = new StringBuilder();
		for (int i = 0; i < 8; i++) {
			int randIndex = rand.nextInt(aToZ.length());
			res.append(aToZ.charAt(randIndex));
		}
		String randid = res.toString();
		try {
			System.out.println("rand###"
					+ randid);
			System.out.println("encrypt rand###"
					+ AES.encrypt(randid));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserDetails);
			preparedStatement.setString(1, userName);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				int userId = 0;
				int branchId = 0;
				userId = resultSet.getInt("ID");
				password = resultSet.getString("UsrPwd");
				branchId = resultSet.getInt("branchId");
				emailAddress = resultSet.getString("UserEmail");
				if (!resultSet.getBoolean("Active")) {
					return new LoginUser(201, "Sorry! Account is not active");
				}
				if (emailAddress.trim().length() > 0) {
					if (SendPasswordmail(userName, randid, emailAddress)) {
						logger.info("Email sent");
						PreparedStatement preparedStatement1 = connection
								.prepareStatement(Queryconstants.updatePassword);
						preparedStatement1.setString(1, AES.encrypt(randid));
						preparedStatement1.setBoolean(2, true);
						preparedStatement1.setString(3, emailAddress);
						preparedStatement1.executeUpdate();
						System.out.println("randid##" + randid);
						System.out.println("encrypt rand###"
								+ AES.encrypt(randid));
						return new LoginUser(userId, 200, "success", branchId);
					}

					return new LoginUser(201,
							"Sorry! Error Occured,Contact Administrator");
				} else {
					return new LoginUser(201,
							"Sorry! You specified Invalid email Address,Contact Administrator");
				}
			} else {
				// System.out.println("pword" +
				// AES.decrypt("dbrjzE1A7vN-fGa826_Llw"));
				return new LoginUser(201,
						"Oops! You specified An Invalid User Name , Please try again.");
			}

		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(202, "Server error occurred");
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}

	}

	/**
	 * send password to mail
	 * 
	 * @param userName
	 * @param randomid
	 * @param emailaddress
	 * @return
	 */
	public boolean SendPasswordmail(String userName, String randomid,
			String emailaddress) {
		logger.info("In Send Mail");
		boolean result = false;
		try {
			org.apache.commons.mail.Email email = new org.apache.commons.mail.SimpleEmail();
			/*
			 * email.setHostName("192.168.13.2"); email.setSmtpPort(25);
			 * email.setAuthenticator(new DefaultAuthenticator("sirisha.voleti",
			 * "sairam")); //email.set email.setSSLOnConnect(false);
			 * email.setFrom("sirisha.voleti@compulynx.com");
			 * email.setSubject("iPrint Password"); email.setMsg("Dear " +
			 * userName + " ,your iPrint Password:  " + password);
			 * email.addTo(emailaddress); email.send(); result=true;
			 */
			File configDir = new File(System.getProperty("catalina.base"),
					"conf");
			File configFile = new File(configDir, "email.properties");
			InputStream stream = new FileInputStream(configFile);
			Properties props = new Properties();
			props.load(stream);
			System.out.println("props" + props);
			String Hostname = props.getProperty("HostName");
			String port = props.getProperty("Port");
			String username = props.getProperty("username");
			String password = props.getProperty("password");
			String from = props.getProperty("setFrom");
			String sub = props.getProperty("setSubject");

			email.setHostName(Hostname);
			email.setSslSmtpPort(port);
			// email.setAuthenticator(new DefaultAuthenticator(username,
			// password));
			email.setSSLOnConnect(false);
			email.setFrom(from);
			email.setSubject(sub);
			email.setMsg("Dear " + userName
					+ " ,your iPrint temporary password:  " + randomid);
			email.addTo(emailaddress);
			email.send();
			result = true;
			System.out.println("hostname" + Hostname);
			System.out.println("port" + port);
			System.out.println("from" + from);
			System.out.println("username" + username);
			System.out.println("sub" + sub);

		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		return result;
	}

	/*
	 * Kush Comment's Method should only be called if the user has a Right to
	 * View > 1. Hence Validate User Credentials Is Called First
	 */
	public LoginSession GetUserAssgnRightsList(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			connection = dataSource.getConnection();
			// logger.info("Query>>"+Queryconstants.getUserGrpRights);

			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserGrpRights);
			preparedStatement.setInt(1, userId);
			// System.out.println("User Id: " + userId);
			resultSet = preparedStatement.executeQuery();
			List<Rights> objlist = new ArrayList<Rights>();
			LoginSession loginSession = new LoginSession();

			// Using to check Previous Header id if the ID's are the same, if
			// not create new header object
			int headerId = 0;
			Rights objRights = null;
			while (resultSet.next()) {
				// The Values Remain the Same, No Harm Reassigning
				loginSession.setLinkId(resultSet.getInt("LinkId"));
				loginSession.setSessionId(resultSet.getInt("ID"));
				loginSession.setUserClassID(resultSet.getInt("UserClassID"));
				loginSession.setUserGroupId(resultSet.getInt("UserGroupID"));
				// loginSession.setLinkName(resultSet.getString("LinkName"));
				loginSession.setSessionName(resultSet.getString("UsrName"));
				loginSession.setSessionFullName(resultSet
						.getString("UsrFullName"));
				loginSession.setIsFirstLogin(resultSet
						.getBoolean("isFirstLogin"));
				loginSession.setBranchName(resultSet.getString("Branchname"));
				// loginSession.setLinkExtInfo(resultSet.getString("LinkExtInfo")
				// );
				// System.out.println("Comapring Header Id : " + headerId +
				// " to : " + resultSet.getInt("HeaderID"));
				if (!(headerId == resultSet.getInt("HeaderID"))) {
					// Avoiding adding Null Object On First loop
					if (!(objRights == null)) {
						objlist.add(objRights);
					}
					objRights = new Rights(resultSet.getString("HeaderName"),
							resultSet.getString("HeaderIconCss"),
							resultSet.getString("HeaderIconColor"));
				}
				if (!(objRights == null)) {
					objRights.rightsList.add(new RightsDetail(resultSet
							.getString("RightDisplayName"), resultSet
							.getString("RightShortCode"), resultSet
							.getString("RightViewName"), resultSet
							.getString("RightName"), resultSet
							.getBoolean("AllowAdd"), resultSet
							.getBoolean("AllowEdit"), resultSet
							.getBoolean("AllowDelete"), resultSet
							.getBoolean("AllowView"), resultSet
							.getInt("RightMaxWidth")));
				}
				headerId = resultSet.getInt("HeaderID");
				// System.out.println("HeaderId Set to: " + headerId);
			}
			if (!(objRights == null)) {
				objlist.add(objRights);
			}
			loginSession.setRightsList(objlist);
			loginSession.setRespCode(200);
			/*
			 * //added loginSession.setRespCode(204);
			 */
			return loginSession;
		} catch (Exception ex) {
			System.out.println(ex);
			return new LoginSession(404);
		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
	}

	/**
	 * get branches by using user
	 */
	public List<Branch> GetUserBranches(int userId) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Branch> branchList = new ArrayList<Branch>();
		try {
			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.getUserLoginBranches);
			preparedStatement.setInt(1, userId);
			resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				Branch branch = new Branch();
				branch.branchId = resultSet.getInt("ID");
				branch.branchCode = resultSet.getString("BranchCode");
				branch.branchName = resultSet.getString("BranchName");
				branchList.add(branch);
				System.out.println("branchName: " + branch.branchName);
			}

		} catch (Exception ex) {
			System.out.println(ex);

		} finally {
			DBOperations.DisposeSql(connection, preparedStatement, resultSet);
		}
		return branchList;

	}

	/**
	 * reset password
	 */
	public LoginUser resetPassword(User user) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			// //check pword policy
			if (!validatePassword(user.userPwd, user.userId)) {
				return new LoginUser(
						201,
						"Password Policy Validation failed,make sure you have not used this password before and it contains alpha"
								+ ",numeric ,special characters & it has minimum length of 8 characters");
			}

			connection = dataSource.getConnection();
			preparedStatement = connection
					.prepareStatement(Queryconstants.updateUserPassword);
			preparedStatement.setString(1, AES.encrypt(user.userPwd));
			preparedStatement.setBoolean(2, false);
			preparedStatement.setTimestamp(3, new java.sql.Timestamp(
					new java.util.Date().getTime()));
			preparedStatement.setInt(4, user.userId);

			/*
			 * return preparedStatement.executeUpdate() > 0 ? new
			 * LoginUser(200,"Password Reset Successfully!!") : new
			 * LoginUser(201,"Password Not Reset!!");
			 */
			if (preparedStatement.executeUpdate() > 0) {
				preparedStatement = connection
						.prepareStatement(Queryconstants.saveOldPassword);
				preparedStatement.setInt(1, user.userId);
				preparedStatement.setString(2, AES.encrypt(user.userPwd));
				if (preparedStatement.executeUpdate() <= 0) {
					throw new Exception(
							"Failed to Save Old Password for user id "
									+ user.userId);
				}

				return new LoginUser(200, "Password Reset Successfully!!");
			} else {
				return new LoginUser(201, "Password Not Reset!!");
			}
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
			return new LoginUser(202, "Server Error occurred, Please try again");
		} catch (Exception ex) {
			ex.printStackTrace();
			return new LoginUser(202, "Server Error occurred, Please try again");
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
		// String pattern =
		// "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+!=]).{8,}";
		String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}";
		System.out.println("password entered is: " + password);
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
					System.out.println("password equals old password: "
							+ oldpassword);
					return false;
				}
			}

			result = password.matches(pattern);
			System.out.println("result: " + result);

		} catch (Exception ex) {

			result = false;
		}

		return result;
	}

}
