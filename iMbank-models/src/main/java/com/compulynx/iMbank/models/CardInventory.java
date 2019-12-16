package com.compulynx.iMbank.models;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CardInventory {
	 public int id;
	    public String batchSize;
	    public String batchNum;
	    public int branchId;
	    public int cardtypeId;
	    public String status;
	    public String createdOn;
	    public int createdBy;
		public String receivedBy;
		public String receivedOn;
		public String comments;
		
				
		public CardInventory(int id, String batchSize, String batchNum,
				int branchId, int cardtypeId, String status,
				String createdOn, int createdBy,String receivedBy,String receivedOn,String comments) {
			super();
			this.id = id;
			this.batchSize = batchSize;
			this.batchNum = batchNum;
			this.branchId = branchId;
			this.cardtypeId = cardtypeId;
			this.status = status;
			this.createdOn = createdOn;
			this.createdBy = createdBy;
			this.receivedBy = receivedBy;
			this.receivedOn = receivedOn;
			this.comments = comments;
		}
		
		public CardInventory() {
			super();
			// TODO Auto-generated constructor stub
		}

}
