package com.sachin.ComplaintRedressalSystemBackend.DAO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.sachin.ComplaintRedressalSystemBackend.Model.Complaint;
import com.sachin.ComplaintRedressalSystemBackend.Model.User;
import com.sachin.ComplaintRedressalSystemBackend.jdbc.DatabaseConnection;


public class ComplaintDAO {
	
	
	private Connection connection = null;
	
	public ComplaintDAO()throws ClassNotFoundException, SQLException{
		Class.forName("com.mysql.jdbc.Driver");
		connection=DriverManager.getConnection("jdbc:mysql://localhost/complaintRedressalSystem?useSSL=false","root","12345678");
		System.out.println("connection established with database " + connection);
		
	}
	
	public Map<String,String> addCompalint(String trackingId,Complaint complaint){
		System.out.println("entered");
		String sql1 = "select id from areaList where pincode = ?;";
		String sql = "insert into complaint(trackingId,userId,name,address,pincodeId,Status,issue) values(?,?,?,?,?,?,?);";
		Map<String,String> output = new HashMap<>();
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql1);
			preparedStatement.setInt(1, complaint.getPincode());
			ResultSet result = preparedStatement.executeQuery();
			int pincode = 0;
			//System.out.println(result.next() + " : " + complaint.getPincode());
			while(result.next()) {
				pincode = result.getInt("id");
				System.out.println(pincode);
				preparedStatement =
				        connection.prepareStatement(sql);
				preparedStatement.setString(1, trackingId);
				preparedStatement.setInt(2, complaint.getUserId());
				preparedStatement.setString(3, complaint.getName());
				preparedStatement.setString(4, complaint.getAddress());
				preparedStatement.setInt(5, pincode);
				preparedStatement.setString(6, complaint.getStatus());
				preparedStatement.setString(7, complaint.getIssue());
				int inserted = preparedStatement.executeUpdate();
				System.out.println(inserted + " dao ");
				if(inserted == 1) {
					output.put("message", "Successfully added complaint");
					output.put("trackingId", trackingId);
					System.out.println("DATA ENTERED");
				}
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	
	public ResponseEntity<?> getComplaint(String role,int userId){
		
		switch (role) {
		case "ADMIN":
			return new ResponseEntity<>(this.getAdminData(),HttpStatus.OK);
		case "MANAGER":
			return new ResponseEntity<>(this.getManagerData(role,userId),HttpStatus.OK);
		case "ENGINEER":
			return new ResponseEntity<>(this.getEngineerComplain(role,userId),HttpStatus.OK);
		default:
			return new ResponseEntity<>(this.getUserData(role,userId),HttpStatus.OK);
		}
		
	}
	
	public Map<String,List<List<String>>> getManagerData(String role,int userId){
		String query1 = "select id from areaList where managerId = ?;";
		String query2 = "select complaint.name,user.name, complaint.trackingId, complaint.issue, complaint.engineerId,complaint.status from complaint join user on user.id = complaint.userId where complaint.pincodeId = ?;";
		String query3 = "select id,name from user where role='ENGINEER'";
		Map<String,List<List<String>>> output = new HashMap<>();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connection.prepareStatement(query1);
			preparedStatement.setInt(1, userId);
			ResultSet setResultSet = preparedStatement.executeQuery();
			while(setResultSet.next()) {
				int pincodeManager = setResultSet.getInt("id");
				preparedStatement = connection.prepareStatement(query2);
				preparedStatement.setInt(1, pincodeManager);
				ResultSet resultSet = preparedStatement.executeQuery();
				List<List<String>> managerView = new ArrayList<List<String>>();
				while(resultSet.next()) {
					List<String> row = new ArrayList<>();
					row.add(resultSet.getString("complaint.name"));
					row.add(resultSet.getString("user.name"));
					row.add(resultSet.getString("complaint.issue"));
					row.add(String.valueOf(resultSet.getInt("engineerId")));
					row.add(resultSet.getString("complaint.status"));
					row.add(resultSet.getString("complaint.trackingId"));
					managerView.add(row);
				}
				preparedStatement = connection.prepareStatement(query3);
				ResultSet resultSet2 = preparedStatement.executeQuery();
				List<List<String>> engineerList = new ArrayList<>();
				while(resultSet2.next()) {
					List<String> row = new ArrayList<>();
					row.add(String.valueOf(resultSet2.getInt("id")));
					row.add(resultSet2.getString("name"));
					engineerList.add(row);
				}
				output.put("managerData", managerView);
				output.put("engineerList", engineerList);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	
	
	public List<?> getUserData(String role,int userId){
		String query = "select trackingId,issue,status, feedback from complaint where userId = ?;";
		PreparedStatement preparedStatement;
		List<List<String>> output = new ArrayList<>();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, userId);
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				List<String> list = new ArrayList<>();
				list.add(result.getString("trackingId"));
				list.add(result.getString("issue"));
				list.add(result.getString("status"));
				list.add(result.getString("feedback"));
				output.add(list);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return output;
	}
	
	
	public Map<String,String> login(String username,String password){
		String query = "select id,role from user where username=? and password = ?;";
		PreparedStatement preparedStatement;
		Map<String,String> output = new HashMap<>();
		try {
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1,username);
			preparedStatement.setString(2,password);
			ResultSet result = preparedStatement.executeQuery();
			while(result.next()) {
				output.put("role", result.getString("role"));
				output.put("userId",String.valueOf(result.getInt("id")));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			 if (connection != null) try { connection.close(); } catch (SQLException ignore) {}
		}
		return output;
	}

	public String addEngineer(int id,String trackingId){
		String sql = "update complaint set engineerId = ? where trackingId = ?";
		String output = "";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, id);
			preparedStatement.setString(2,trackingId);
			boolean execute = preparedStatement.execute();
			while(execute) {
				output = "Assigned Engineer Successfully";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(output);
		return output;
	}
	
	public  Map<String,List<List<String>>> getAdminData(){
		String query1 = "select complaint.name,user.name, complaint.trackingId, complaint.issue, complaint.status, complaint.pincodeId from complaint join user on user.id = complaint.userId;";
		String query2 = "select areaList.id, user.name from areaList join user on areaList.managerId = user.id where user.role='MANAGER'";
		Map<String,List<List<String>>> output = new HashMap<>();
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(query1);
			ResultSet setResultSet = preparedStatement.executeQuery();
			List<List<String>> AdminView = new ArrayList<List<String>>();
			while(setResultSet.next()) {
				List<String> row = new ArrayList<>();
				row.add(setResultSet.getString("complaint.name"));
				row.add(setResultSet.getString("user.name"));
				row.add(setResultSet.getString("complaint.issue"));
				row.add(setResultSet.getString("complaint.status"));
				row.add(setResultSet.getString("complaint.trackingId"));
				row.add(String.valueOf(setResultSet.getInt("complaint.pincodeId")));
				AdminView.add(row);
			}
			preparedStatement = connection.prepareStatement(query2);
			ResultSet resultSet = preparedStatement.executeQuery();
			List<List<String>> ManagerList = new ArrayList<>();
			while(resultSet.next()) {
				List<String> row = new ArrayList<>();
				row.add(String.valueOf(resultSet.getInt("areaList.id")));
				row.add(resultSet.getString("user.name"));
				ManagerList.add(row);
			}
				output.put("AdminData", AdminView);
				output.put("ManagerList", ManagerList);
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}
	public String test() {
		return connection.toString();
	}
	
	
	public Map<String,List<List<String>>> getEngineerComplain(String role,int userId){
		String query1 = "select complaint.name,user.name, complaint.trackingId, complaint.issue, complaint.status, complaint.feild from complaint join user on user.id = complaint.userId where complaint.engineerId = ?;";
		Map<String,List<List<String>>> output = new HashMap<>();
		PreparedStatement preparedStatement;
		
		try {
			preparedStatement = connection.prepareStatement(query1);
			preparedStatement.setInt(1, userId);
			ResultSet setResultSet = preparedStatement.executeQuery();
			List<List<String>> engineerComplaint = new ArrayList<>();
			while(setResultSet.next()) {
				
					List<String> row = new ArrayList<>();
					row.add(setResultSet.getString("complaint.name"));
					row.add(setResultSet.getString("user.name"));
					row.add(setResultSet.getString("complaint.issue"));
					row.add(setResultSet.getString("complaint.status"));
					row.add(setResultSet.getString("complaint.feild"));
					row.add(setResultSet.getString("complaint.trackingId"));
					engineerComplaint.add(row);
				}
				output.put("engineerData", engineerComplaint);
				
			}
		 catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return output;
	}

	public String updateComplaint(String trackingId,String status,String field ) {
		System.out.println("entered");
		String sql = "update complaint set feild=? , status=? where trackingId=?;";
		System.out.println(sql);
		PreparedStatement preparedStatement;
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, field);
			preparedStatement.setString(2, status);
			preparedStatement.setString(3, trackingId);
			boolean execute = preparedStatement.execute();
			System.out.println(execute);
			if(execute) {
				return "Updated Successfull";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Updated Successfull";
	}
	
	public List<List<String>> getAllUsers(){
		String sql = "select id,name,username,role from user;";
		PreparedStatement preparedStatement;
		List<List<String>> users = new ArrayList<>();
		try {
			preparedStatement  = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next()) {
				List<String> row = new ArrayList<>();
				row.add(resultSet.getString("id"));
				row.add(resultSet.getString("name"));
				row.add(resultSet.getString("username"));
				row.add(resultSet.getString("role"));
				users.add(row);
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return users;
		
	}
	
	public String addUser(User user) {
		String sql = "insert into user(id,name,username,password,role) values(?,?,?,?,?);";
		PreparedStatement preparedStatement;
		String message = "";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1, user.getId());
			preparedStatement.setString(2, user.getName());
			preparedStatement.setString(3, user.getUsername());
			preparedStatement.setString(4, user.getPassword());
			preparedStatement.setString(5, user.getRole());
			boolean execute = preparedStatement.execute();
			if(!execute) {
				message = "User Added Successfully";
				return message;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message = "something went wrong";
		return message;
	}
	
	public String updateUser(int id,String role) {
		String sql = "update user set role=? where id=?";
		PreparedStatement preparedStatement;
		String message = "";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setString(1, role);
			preparedStatement.setInt(2,id);
			boolean execute = preparedStatement.execute();
			if(!execute) {
				message = "User Updated Successfully";
				return message;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message = "something went wrong";
		return message;
	}
	
	public String delete(int id) {
		String sql = "delete from user where id = ?; ";
		PreparedStatement preparedStatement;
		String message = "";
		try {
			preparedStatement = connection.prepareStatement(sql);
			preparedStatement.setInt(1,id);
			boolean execute = preparedStatement.execute();
			if(!execute) {
				message = "User Deleted Successfully";
				return message;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		message = "something went wrong";
		return message;
	}
	
	
	
}
