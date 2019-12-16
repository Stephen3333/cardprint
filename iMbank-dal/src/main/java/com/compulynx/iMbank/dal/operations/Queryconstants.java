package com.compulynx.iMbank.dal.operations;

public class Queryconstants {
	// Login
	public static String getPasswordDate = "Select ID As UserID,Active,isFirstLogin,branchId,LastPasswordModifiedOn From U_UserMaster Where UsrName = ? And UsrPwd = ? And isLock=? And LastPasswordModifiedOn=?";
	public static String getUserCredentialManual = "Select ID As UserID,Active,isFirstLogin,branchId,LastPasswordModifiedOn From U_UserMaster Where UsrName = ? And UsrPwd = ? And isLock=?";
	// public static String getUserAttempts =
	// "Select ID,UsrName,UsrPwd, PwdAttempts,case isLock when 'true' then 'locked' when 'false' then 'unlocked' end as isLock From U_UserMaster Where isLock=?";
	public static String getUserAttempts = "Select ID,UsrName,UsrPwd, PwdAttempts, isLock From U_UserMaster Where isLock=?";
	public static String getUserBioManual = "Select ID As UserID,Active  From U_UserMaster Where UsrName = ?";
	public static String getUserDetails = "Select * From U_UserMaster Where UsrName = ? ";
	public static String getUserData = "Select * From U_UserMaster  ";
	public static String getHeaders="select * from U_MenuHeaderMaster";
	public static String insertRights="insert into u_rightsmaster ( RightName, AllowView, AllowAdd, AllowEdit, AllowDelete, RightsClassID, RightViewName, RightHeaderID, RightDisplayName, RightShortCode, RightMaxWidth, CreatedBy, CreatedOn) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateRights="update u_rightsmaster set  RightName=?, AllowView=?, AllowAdd=?, AllowEdit=?, AllowDelete=?, RightsClassID=?, RightViewName=?, RightHeaderID=?, RightDisplayName=?, RightShortCode=?, RightMaxWidth=?, CreatedBy=?, CreatedOn=? where id=?";

	// login details
	public static String insertLoginDetails = "insert into  dbo.LoginDetails (UserId,UserName,Date,BranchId) values(?,?,?,?)";

	// password attempts
	public static String updateAttempts = "UPDATE [dbo].[U_UserMaster] SET [PwdAttempts] = PwdAttempts+1 WHERE UsrName = ? ";

	public static String resetAttempts = "UPDATE [dbo].[U_UserMaster] SET PwdAttempts = ?, isLock = ? ,modifiedBy=?, modifiedOn=?, isFirstLogin=? WHERE UsrName = ?";

	public static String unlockUser = "UPDATE [dbo].[U_UserMaster] SET isLock = ?, modifiedOn=? WHERE UsrName = ?";

	public static String selectAttempts = "SELECT PwdAttempts FROM [dbo].[U_UserMaster]  WHERE UsrName = ?";

	public static String updateUserLock = "UPDATE [dbo].[U_UserMaster] SET isLock=? WHERE UsrName=?";
	public static String resetPasswordAttempts = "update [dbo].[U_UserMaster] SET [PwdAttempts] = 0 where UsrName=?";
	public static String getUserGrpRights = "Select Um.ID, Um.UsrName, FirstName+' '+MiddleName+' '+ LastName as UsrFullName,Um.isFirstLogin, Ug.ID As UserGroupID,Ug.GroupLinkID As LinkId\n"
			+ ",Ug.GroupClassID As UserClassID, Hm.ID As HeaderID,Hm.HeaderName,Hm.HeaderIconCss,Hm.HeaderIconColor,\n"
			+ " Rm.RightDisplayName,Rm.RightShortCode,Rm.RightViewName,Rm.RightName,\n"
			+ " Ur.AllowView,Ur.AllowEdit,Ur.AllowAdd,Ur.AllowDelete,Rm.RightMaxWidth,b.branchname \n"
			+ " From U_UserAssignedRights Ur Inner Join U_RightsMaster Rm On Ur.RightID = Rm.ID \n"
			+ " Inner join U_UserMaster Um On Um.UsrGroupID = Ur.GroupId \n"
			+ "  Inner join U_MenuHeaderMaster Hm on Hm.ID = Rm.RightHeaderID \n"
			+ "  Inner Join U_UserGroupsMaster Ug on Ug.ID = Um.UsrGroupID "
			+ " Inner join BranchMaster b on b.id=um.branchid "
			+ "  And Ug.ID = Ur.GroupId Where Um.ID = ? And Ur.AllowView = 1 Order By Hm.HeaderPos";//

	// Users
	public static String insertUser = "INSERT INTO [dbo].[U_UserMaster] ([FirstName],[MiddleName],[LastName],[UsrName],[UsrPwd],[UsrGroupID],[UserEmail],[UserPhone] ,[UserLinkedID],[Active],[CreatedBy],[CreatedOn],[payrollNumber],[branchId],[UsrFullName]) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	public static String updateUser = "UPDATE [dbo].[U_UserMaster] SET [FirstName]=?,[MiddleName]=?,[LastName]=?, [UsrName]=?,[UsrPwd]=?,[UsrGroupID]=?,[UserEmail]=?,[UserPhone]=? ,[UserLinkedID]=?,[Active]=?,[modifiedBy]=?,[modifiedOn]=?,[isFirstLogin]=?,[payrollNumber]=?,[branchId]=? ,[LastPasswordModifiedOn]=? WHERE ID=? ";
	// public static String updateUserPassword =
	// "UPDATE [dbo].[U_UserMaster] SET [UsrPwd]=?,[isFirstLogin]=? WHERE ID=? ";
	public static String updateUserPassword = "UPDATE [dbo].[U_UserMaster] SET [UsrPwd]=?,[isFirstLogin]=?,[LastPasswordModifiedOn]=? WHERE ID=? ";
	public static String updateUserEnr = "UPDATE [U_UserMaster] SET [UserBioID]=? WHERE ID=? ";
	public static String getUserByName = "Select ID From U_UserMaster Where UsrName =?";
	public static String getUserByPayRollNumber = "Select ID from U_UserMaster where payrollNumber = ?";
	public static String getMemberByLinkId = "Select ID From U_UserMaster Where UserLinkedID =?";
	public static String insertUserBranches = " insert into [U_UserAssignedBranches] ([UserId],[BranchId],[IsActive]) values(?,?,?)";
	public static String deleteUserBranches = "delete  from  [U_UserAssignedBranches] where [UserId]=?";
	// removed group classid near to where condition
	public static String getUserById = " SELECT a.[ID],[FirstName],[MiddleName],[LastName],[UsrName],[UsrFullName],[UsrPwd],[UsrGroupID],[UserEmail],[UserPhone], B.[GroupName] "
			+ ", [UserSecretQuestionID],[UserSecretAns],[UserLinkedID],A.[Active],A.[CreatedBy],a.[payrollNumber],a.[branchId] "
			+ ", A.[CreatedOn],C.ID as classId FROM [dbo].[U_UserMaster] A,U_UserGroupsMaster B,BranchMaster C  "
			+ " where A.UsrGroupID=B.ID  AND A.ID=? ";
	public static String getUserLoginBranches = "select A.UserId,A.BranchId,B.ID,B.BranchCode,B.BranchName from U_UserAssignedBranches A ,BranchMaster B WHERE A.UserId=? and A.IsActive=1 AND B.ID=A.BranchId";

	public static String getUserAssignedBranches = "SELECT B.BranchName,u.BranchId as [BranchId],u.isActive as [isChecked] "
			+ "  FROM U_UserMaster UM "
			+ " INNER JOIN  U_UserAssignedBranches U ON U.UserId=UM.ID "
			+ " right JOIN BranchMaster B ON B.ID=U.BranchId "
			+ " WHERE UM.ID=? "
			+ "union all "
			+ " SELECT B.BranchName,b.Id as [BranchId],0 as [isChecked] "
			+ " FROM U_UserMaster UM, BranchMaster B where "
			+ " UM.ID=? and B.Id not in(select BranchId from U_UserAssignedBranches WHERE U_UserAssignedBranches.UserId= ?)";

	public static String getUsers = "SELECT a.[ID],[UsrName],[UsrFullName],[UsrPwd],[UsrGroupID],[UserEmail],ISNULL([UserPhone],'0') AS UserPhone"
			+ ",[UserSecretQuestionID],[UserSecretAns],[UserLinkedID],a.[Active],a.[CreatedBy]"
			+ ",a.[CreatedOn],c.ID as classId ,a.[payrollNumber],a.[branchId] FROM [dbo].[U_UserMaster] a,U_UserGroupsMaster b,BranchMaster c "
			+ "where a.UsrGroupID=b.ID and b.GroupClassID=c.ID and   b.GroupLinkID=? "; // and
																						// a.active=1";

	public static String users = "select * from U_UserMaster";
	public static String getLoginTypes = "SELECT * from M_LoginTypes";

	public static String getExistingPassword = "select * from [U_PwordHistory] where UserId=?";

	public static String saveOldPassword = "insert into U_PwordHistory (UserId,Password) values(?,?)";

	public static String getCurrentPassword = "select UsrPwd from [U_UserMaster] where ID=?";

	public static String getMemberUsersByBrokerId = "SELECT a.[ID],[UsrName],[UsrFullName],[UsrPwd],[UsrGroupID],[UserEmail],[UserPhone] "
			+ ",[UserSecretQuestionID],[UserSecretAns],[UserBioLogin],[UserLinkedID],[UserBioID],a.[Active],a.[CreatedBy] "
			+ ",a.[CreatedOn],c.ID as classId FROM [dbo].[U_UserMaster] a,U_UserGroupsMaster b,BranchMaster c  "
			+ "where a.UsrGroupID=b.ID and b.GroupClassID=c.ID AND b.GroupClassID=? and UserLinkedID IN (Select  Mm.ID as MemberID "
			+ "from M_MemberMaster Mm "
			+ "inner join C_CoverCategoryMaster ct on ct.ID=mm.CategoryID "
			+ "inner join C_CoverMaster cv on cv.ID=ct.CategoryCoverID  "
			+ "inner join M_BrokerMaster b on b.ID=cv.CoverBrokerID "
			+ "	Where  CV.CoverBrokerID=?) ";
	public static String getClasses = "SELECT [ID],[BranchName] FROM [dbo].[BranchMaster]";
	public static String getQuestions = "SELECT [ID],[Question] FROM [dbo].[U_QuestionMaster]";
	// User Groups
	public static String insertUserGroup = "INSERT INTO [dbo].[U_UserGroupsMaster] ([GroupCode],[GroupName],[GroupLinkID],[Active],[CreatedBy],[CreatedOn]) VALUES (?,?,?,?,?,?)";
	public static String updateUserGroup = "UPDATE [dbo].[U_UserGroupsMaster] Set [GroupCode] = ?,[GroupName] = ?,[Active] = ?,[modifiedBy] = ?, [modifiedOn] = ? Where [ID] = ?";

	public static String getGroupByName = "Select [ID] From [dbo].[U_UserGroupsMaster] Where [GroupName] = ? and id <> ?";

	public static String getGroupByCode = "Select [ID] From [dbo].[U_UserGroupsMaster] Where [GroupCode] = ? and id <> ?";

	// public static String getGroupById =
	// "SELECT U.RightID,R.RightDisplayName,R.AllowView,R.AllowAdd,R.AllowEdit,R.AllowDelete, "
	// +
	// " U.AllowView AS [RightView],U.AllowAdd AS [RightAdd],U.AllowEdit AS [RightEdit],U.AllowDelete AS [RightDelete] "
	// + " FROM U_UserGroupsMaster G "
	// + " INNER JOIN U_UserAssignedRights U ON U.GroupId=G.ID "
	// + " INNER JOIN U_RightsMaster R ON R.ID=U.RightID "
	// + " WHERE G.ID=?";

	public static String getGroupById = "    SELECT U.RightID,R.RightDisplayName,R.AllowView,R.AllowAdd,R.AllowEdit,R.AllowDelete,"
			+ "U.AllowView AS [RightView],U.AllowAdd AS [RightAdd],U.AllowEdit AS [RightEdit],U.AllowDelete AS [RightDelete] "
			+ "FROM U_UserGroupsMaster G "
			+ "INNER JOIN U_UserAssignedRights U ON U.GroupId=G.ID "
			+ "right JOIN U_RightsMaster R ON R.ID=U.RightID "
			+ "WHERE G.ID=?  "
			+ "union all "
			+ "SELECT r.Id,R.RightDisplayName,R.AllowView,R.AllowAdd,R.AllowEdit,R.AllowDelete,"
			+ "0 AS [RightView],0 AS [RightAdd],0 AS [RightEdit],0 AS [RightDelete] "
			+ "FROM U_UserGroupsMaster G ,U_RightsMaster R "
			+ "WHERE G.ID=?  and r.Id not in(select rightid from U_UserAssignedRights WHERE U_UserAssignedRights.GroupId=?)     ";

	public static String getGroups = "Select [ID],[GroupCode],[GroupName],[GroupClassID],[GroupLinkID],[Active],[CreatedBy] From [dbo].[U_UserGroupsMaster] Where   [GroupLinkID] =?";
	public static String getActiveGroups = "Select [ID],[GroupCode],[GroupName],[GroupClassID],[GroupLinkID],[Active],[CreatedBy] From [dbo].[U_UserGroupsMaster] Where  [GroupLinkID] =? and Active = ?";
	public static String deleteGroupRights = "DELETE FROM U_UserAssignedRights WHERE GroupId=?";

	// Rights
	public static String getRights = "SELECT *,[ID],[RightDisplayName],[AllowView],[AllowAdd],[AllowEdit],[AllowDelete],[RightsClassID],[CreatedBy],[CreatedOn] FROM [dbo].[U_RightsMaster]";
	public static String insertGroupRights = "INSERT INTO [dbo].[U_UserAssignedRights] ([RightID],[GroupId] ,[AllowView],[AllowAdd] ,[AllowEdit],[AllowDelete],[CreatedBy],[CreatedOn]) VALUES (?,?,?,?,?,?,?,?)";
	public static String updateGroupRights = "UPDATE [dbo].[U_UserAssignedRights] SET [AllowView]=?,[AllowAdd]=? ,[AllowEdit]=?,[AllowDelete]=?,[CreatedBy]=?,[CreatedOn]=? WHERE [RightID]=? AND [GroupId]=?";

	public static String getallAccounts = "Select * from MST_SUPPLIER where verified <> 'R'";

	public static String getVerifiedAccounts = "Select * from MST_SUPPLIER where verified = 'Y'";

	public static String getSingleAccount = "Select * from MST_SUPPLIER where id = ?";

	public static String insertAccount = "INSERT INTO [MST_SUPPLIER] ([idNumber] ,[middleName],[firstName] ,[lastName] ,[address],[postalCode] ,"
			+ "[email] ,[mobile],[supplierCode],[createdBy]) VALUES(?,?,?,?,?,?,?,?,?,?)";

	public static String updateSupplierInfo = "UPDATE MST_SUPPLIER SET [idNumber] = ?,[middleName] = ?,[firstName] = ? ,[lastName] = ? ,[address] = ?,[postalCode] = ?,[email] = ? ,[mobile] = ?,modifiedBy=?,modifiedOn=? where id= ?";

	public static String getDashBoardDetailCount = "SELECT 'USERS' AS NAME,COUNT(U.ID) AS COUNTNO FROM U_UserMaster U"
			+ " UNION "
			+ " SELECT 'CARDS' AS NAME,COUNT(C.ID) AS COUNTNO FROM CardPrint C ";

	public static String getBranchName = "SELECT [ID],[BranchName] FROM [BranchMaster] where ID = ?";

	public static String getUserBranchId = "select branchCode from BranchMaster where id in (select branchId from U_UserMaster where ID=?)";

	public static String getProductListingsByClassId = "select * from M_ProductMaster where classId = ?";

	public static String getProductListings = "select * from M_ProductMaster";

	public static String getPrinters = "select * from M_PrinterMaster";

	public static String getPrinterById = "select BranchName from [BranchMaster] where id=?";

	// Branch
	/* public static String getBranchs="select * from [BranchMaster] "; */
	public static String getBranchs = "SELECT B.ID, BranchName,B.CreatedBy, B.CreatedOn, BranchCode,BranchAddress, RegionId,status  From BranchMaster B,M_Region R Where B.RegionId=r.Id";
	public static String insertBranches = "INSERT INTO [BranchMaster]([BranchName],[CreatedBy],[CreatedOn],[BranchCode],[BranchAddress], [RegionId],[status]) VALUES (?,?,?,?,?,?,?)";
	public static String getUserBranches = "SELECT  [ID] ,[BranchName],[CreatedBy] ,[CreatedOn],[BranchCode],[modifiedBy],[modifiedOn] ,[BranchAddress],[RegionId],[status] FROM [BranchMaster]";
	public static String updateBranches = "UPDATE BranchMaster  SET BranchName = ? ,BranchCode = ?,modifiedBy=?,modifiedOn=?,BranchAddress=?, RegionId =?  ,status=? WHERE id = ?";
	public static String getBranchByCode = "select * from [BranchMaster] where BranchCode=? AND id <> ?";
	public static String getBranchByName = "select * from [BranchMaster] where BranchName=? AND id <> ?";

	public static String checkPrinterByCode = "SELECT * FROM  [M_PrinterMaster] WHERE serialNumber=? and id <> ?";
	public static String checkPrinterByIp = "SELECT * FROM  [M_PrinterMaster] WHERE ipAddress=? and id <> ?";
	public static String checkCardByName = "SELECT * FROM  [CardPrint] WHERE AccountName=?";
	public static String checkUserBranch = "select * from U_UserMaster where ID=? and branchId=?";
	public static String createPrinter = "INSERT INTO [M_PrinterMaster]([serialNumber],[branchId],[branchName],[ipAddress],[createdBy],[status]) VALUES(?,?,?,?,?,?)";

	public static String getCard = "select * from [CardPrint]";
	public static String createCard = "insert into [CardPrint] ([CardNumber],[AccountName],[AccountNumber],[PrintedBy],[PrinterSerialNo],[PrintStatus],[BranchPrinted],[DatePrinted],[CardFormatId],[PrinterTypeId]) VALUES(?,?,?,?,?,?,?,?,?,?)";

	public static String updateCard = "UPDATE [CardPrint] SET [CardNumber]=?, [AccountName]=?, [AccountNumber]=?, [PrintedBy]=?,[PrinterSerialNo]=?, [PrintStatus]=?, [BranchPrinted]=?,[DatePrinted]=?, [CardFormatId]=?,[PrinterTypeId]=?  WHERE id=?";
	// added
	public static String getCardByStock = "select a.ID ,CardNumber,AccountName,AccountNumber,PrintedBy,PrinterSerialNo,PrintStatus,BranchPrinted,DatePrinted,CardFormatId from CardPrint A "
			+ "UNION"
			+ "select r.ID,CardNum,RejectedBy,BranchId,Reason,RejectedOn from CardRejection R";
	// added
	public static String getCardByBranch = "select a.ID, CardNumber,AccountName,AccountNumber,PrintedBy,PrinterSerialNo,PrintStatus,BranchPrinted,DatePrinted,CardFormatId from CardPrint A"
			+ "UNION" + "select b.ID,BranchName,BranchCode from BranchMaster B";

	public static String updateProduct = "UPDATE [M_PrinterMaster]  SET [serialNumber] = ?,[ipAddress]=?,[branchId] = ? ,[branchName]=?,modifiedBy=?,modifiedOn=?,status=? WHERE id= ?";

	public static String getUnverifiedSuppliers = "select * from MST_SUPPLIER where bioId <> 0 and verified='N'";

	public static String updateRejectStatus = "UPDATE MST_SUPPLIER SET bioId=0 , verified=?, rejectedBy=?, rejectedOn= ? where id= ?";

	public static String updateVerifiedStatus = "UPDATE MST_SUPPLIER SET  verified=?, verifiedBy=?, verifiedOn= ? where id= ?";

	public static String updateOverrideStatus = "UPDATE MST_SUPPLIER SET  verified=?, verifiedBy=?, verifiedOn= ?,override='Y',overrideBy =?,overrideOn=? where id= ?";

	public static String saveRejectDetails = "INSERT INTO [DTL_REJECTED]([supplierId],[rejectedBy],[bioId]) VALUES (?,?,?)";

	// CardType
	public static String createCardType = "insert into [CardTypeMaster] ([Code],[Name],[Description],[XCoordinates],[YCoordinates],[FontName],[FontSize],[FontColor]) VALUES(?,?,?,?,?,?,?,?)";
	public static String updateCardType = "UPDATE [CardTypeMaster]  SET [Code] = ?,[Name]=?,[Description]=?,[XCoordinates]=?,[YCoordinates]=?,[FontName]=?,[FontSize]=?,[FontColor]=? WHERE id= ?";
	public static String checkName = "Select ID from CardTypeMaster where Name = ?";
	public static String getCardType = "select ID,Code,Name,Description,XCoordinates,YCoordinates,FontName,FontSize,FontColor from CardTypeMaster";

	// CardInventory
	public static String createCardInventory = "insert into [CardInventoryManagement] ([BatchSize],[BatchNumber],[BranchId],[CardTypeId],[Status],[CreatedBy],[CreatedOn],[ReceivedBy],[ReceivedOn],[Comments]) VALUES(?,?,?,?,?,?,?,?,?,?)";
	public static String updateCardInventory = "UPDATE [CardInventoryManagement]  SET [BatchSize] = ?,[BatchNumber]=?,[BranchId] = ? ,[CardTypeId]=?,[Status]=?,[CreatedBy]=?,[CreatedOn]=? WHERE id= ?";
	public static String checkBatchSize = "select ID from CardInventoryManagement where BatchSize = ?";
	public static String getCardInventory = "select * from CardInventoryManagement";
	public static String CardInventory = " select id,BatchSize,BatchNumber,BranchId,CardTypeId,case status when 0 then 'pending' else 'received' end  as status,CreatedBy,CreatedOn,ReceivedBy,ReceivedOn,Comments from CardInventoryManagement";

	public static String updateCardStock = "UPDATE [CardInventoryManagement]  SET [BatchSize]= ? WHERE [BranchId]=?";

	// CardRejection

	public static String createCardRejection = "insert into [CardRejection]([CardNum],[RejectedBy],[BranchId],[Reason],[RejectedOn]) VALUES(?,?,?,?,?)";
	public static String updateCardRejection = "UPDATE [CardRejection] SET [CardNum]=?,[RejectedBy] = ?,[BranchId]=?,[Reason]=?,[RejectedOn]=? WHERE id=?";
	public static String getCardRejection = "select * from CardRejection";

	// CardReceiving

	public static String updateCardInventoryReceive = "UPDATE [CardInventoryManagement]  SET [BatchSize] = ?,[BatchNumber]=?,[BranchId] = ? ,[CardTypeId]=?,[Status]=? ,[ReceivedBy]=?,[ReceivedOn]=?,[Comments]=? WHERE id= ?";

	// CurrentBalance

	public static String getCurrentBalance = "select * from CurrentBalance";

	public static String createCurrentBalance = "insert into [CurrentBalance] ([BranchId],[CurrentBalance],[LastUpdate]) VALUES(?,?,?)";

	public static String updateCurrentBalance = "UPDATE CurrentBalance SET CurrentBalance=(CurrentBalance-1) WHERE BranchId=?";

	// Config

	public static String getConfig = "select Id,HostName,Port,Username,Password,Email,Sessiontimeout from Config";
	public static String createConfig = "insert into [Config] ([Id],[Hostname],[Port],[Username],[Password],[Email],[Sessiontimeout]) VALUES(?,?,?,?,?,?,?)";
	public static String updateConfig = "UPDATE Config SET [Hostname]=?,[Port]=?,[Username]=?,[Password]=?,[Email]=?,[Sessiontimeout]=? WHERE id=?";

	public static String updatePassword = "UPDATE [dbo].[U_UserMaster] SET [UsrPwd]=?,[isFirstLogin]=? WHERE UserEmail=? ";

	public static String updateExpirePassword = "update dbo.U_UserMaster set isFirstLogin=?, LastPasswordModifiedOn=? where ID=?";

	public static String getPrinterType = "select * from dbo.PrinterType";

	public static String createPrinterType = "insert into [PrinterType]([printerName],[description]) values(?,?)";

	public static String updatePrinterType = "update [PrinterType] set [printerName]=?,[description]=? where id=?";

	public static String checkPrinterName = "Select ID from PrinterType where printerName = ?";

	public static String updateAccountInfo = "UPDATE MST_Account SET [idNumber] = ?,[middleName] = ?,[firstName] = ? ,[lastName] = ? ,[address] = ?,[postalCode] = ?,[email] = ? ,[mobile] = ?,modifiedBy=?,modifiedOn=?,accNumber=?,status=? where id= ?";

	public static String updateAccountStatus = "UPDATE MST_Account SET status=? where accNumber= ?";

	public static String validateCurCode = "select count(*) as count from MST_Account where curCode=? and id <> ?";

	public static String validateAccountNumber = "select count(*) as count from MST_Account where accNumber=? and id <> ?";

	public static String getAccountNumber = "select * from MST_Account where accNumber=? ";

	public static String getAccStatus = "select * from MST_Account where accNumber=? and status=?";

	public static String getUnverifiedCustomers = "select * from MST_Account where verified='N'";

	public static String updateAccStatus = "UPDATE MST_Account SET status=? where accNumber= ?";

	// Activity
	public static String insertActivity = "insert into [log_activity] ([activity_name],[reason],[accNum],[createdBy],[createdOn]) values(?,?,?,?,?)";
	public static String updateActivity = "update [log_activity] set [activity_name]=?,[reason]=?,[accNum]=?,[createdBy]=?,[createdOn]=? where id=?";

	// regions
	public static String getRegionsByStatus = "select ID,Name, Active,CreatedBy, CreatedOn from M_Region where Active=? ";
	public static String getRegions = "select ID,Name, Active,CreatedBy, CreatedOn from M_Region ";
	public static String insertRegions = "INSERT INTO [M_Region] ([Name],[Active],[CreatedBy],[CreatedOn]) VALUES(?,?,?,?)";
	public static String updateRegions = "UPDATE M_Region SET Name = ?,Active = ?,CreatedBy = ?,CreatedOn = ? WHERE id=?";
	public static String getRegionByName = "select * from [M_Region] where Name=? AND id <> ?";

	public static String getBranchsByStatus = "SELECT B.ID, B.BranchName,B.CreatedBy, B.CreatedOn, B.BranchCode,B.BranchAddress, B.RegionId,B.status  From BranchMaster B,M_Region R Where B.RegionId=r.Id and B.status=?";

	// loggerinfo

	public static String insertLoggerInfo = "insert into Logger_Info (CategoryName,Column_Id,Column_Name,branchId,date) values(?,?,?,?,?)";

}