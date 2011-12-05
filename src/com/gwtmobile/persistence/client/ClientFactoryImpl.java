package com.gwtmobile.persistence.client;


public class ClientFactoryImpl {

	private DbConn dbConn = new DbConn();

	public ClientFactoryImpl() {
		
	}

	/**
	 * @return the dbconn
	 */
	public DbConn getDbconn() {
		return dbConn;
	}

	/**
	 * @param dbconn the dbconn to set
	 */
	public void setDbconn(DbConn dbconn) {
		dbConn = dbconn;
	}
	
}
