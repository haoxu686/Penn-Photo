package pennphoto.server.dbutil;

import java.sql.*;
import java.util.Arrays;

public class ConnectionManager {
	private static int poolSize = 10;
	private static DBConnector [] pool = new DBConnector [10];
	private static boolean [] allocation = new boolean [10];
	
	public static void init() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/haoxu";
			Connection connection = DriverManager.getConnection(url, "root", "cis550root");
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT MAX(photoid) AS photoid FROM photo");
			rs.next();
			DBConnector.setMaxPhotoId(rs.getInt("photoid"));
			rs = stmt.executeQuery("SELECT MAX(uid) AS userid FROM user");
			rs.next();
			DBConnector.setMaxUserId(rs.getInt("userid"));
			rs = stmt.executeQuery("SELECT MAX(cid) AS cid FROM circle");
			rs.next();
			DBConnector.setMaxCircleId(rs.getInt("cid"));
			connection.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (int i = 0; i < poolSize; i++) {
			pool[i] = new DBConnector(i);
			allocation[i] = false;
		}
	}
	
	public static void destroy() {
		for (int i = 0; i < poolSize; i++) {
			pool[i].close();
		}
	}
	
	public static synchronized DBConnector getDBConnector() {
		DBConnector result = null;
		int i = 0;
		for (i = 0; i < poolSize; i++) {
			if (!allocation[i]) {
				break;
			}
		}
		if (i < poolSize) {
			allocation[i] = true;
			result = pool[i];
		} else {
			pool = Arrays.copyOf(pool, poolSize+5);
			allocation = Arrays.copyOf(allocation, poolSize+5);
			for (int j = poolSize; j < poolSize+5; j++) {
				pool[j] = new DBConnector(j);
				allocation[j] = false;
			}
			result = pool[poolSize];
			allocation[poolSize] = true;
			poolSize += 5;
		}
		return result;
	}

	protected static void release(int index) {
		allocation[index] = false;
	}
}
