package com.sachin.ComplaintRedressalSystemBackend.Service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sachin.ComplaintRedressalSystemBackend.DAO.ComplaintDAO;
import com.sachin.ComplaintRedressalSystemBackend.Model.Complaint;
import com.sachin.ComplaintRedressalSystemBackend.Model.User;

@Service
public class CompliantService {
	
	
	ComplaintDAO complaintDAO = null;


	public ResponseEntity<?> getComplaint(String role,int userId){
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(complaintDAO.getComplaint(role,userId),HttpStatus.OK);
	}
	
	public Map<String,String> login(String username,String password){
		
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.complaintDAO.login(username,password);
	}
	
	public Map<String,String> addComplaint(String trackingId,Complaint complaint){
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.complaintDAO.addCompalint(trackingId,complaint);
	}
	
	public ResponseEntity<?> addEngineer(int id,String trackingId){
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(this.complaintDAO.addEngineer(id, trackingId),HttpStatus.OK);
	}
	
	public String test() throws ClassNotFoundException, SQLException {
		complaintDAO = new ComplaintDAO();
		return this.complaintDAO.test();
	}
	
	public ResponseEntity<?> getAdminTable(){
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(this.complaintDAO.getComplaint("ADMIN", 1),HttpStatus.OK);
	}
	
	
	public ResponseEntity<?> updateComplaint(String trackinId,String status,String field){
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseEntity<>(this.complaintDAO.updateComplaint(trackinId,status,field),HttpStatus.OK);
	}
	
	public List<List<String>> getAllUsers(){
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.complaintDAO.getAllUsers();
	}
	
	public String addUser(User user) {
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.complaintDAO.addUser(user);
	}
	
	public String updateUser(int id,String role) {
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.complaintDAO.updateUser(id,role);
	}
	
	public String delete(int id) {
		try {
			complaintDAO = new ComplaintDAO();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.complaintDAO.delete(id);
	}
	
	
	
}
