package com.sachin.ComplaintRedressalSystemBackend.controller;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sachin.ComplaintRedressalSystemBackend.TokenGenrator;
import com.sachin.ComplaintRedressalSystemBackend.Model.Complaint;
import com.sachin.ComplaintRedressalSystemBackend.Model.User;
import com.sachin.ComplaintRedressalSystemBackend.Service.CompliantService;

@RestController

public class ComplaintController {

	@Autowired
	private CompliantService compliantService; 
	
	
	@GetMapping(path = "/viewComplaint/{role}/{userId}",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.ALL_VALUE)
	public ResponseEntity<?> getComplaint(@PathVariable(name="role") String role, @PathVariable(name="userId") int userId ){
		return new ResponseEntity<>(compliantService.getComplaint(role,userId),HttpStatus.OK);
	}
	
	@GetMapping(path="/allUsers",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllUsers(){
		return new ResponseEntity<>(this.compliantService.getAllUsers(),HttpStatus.OK);
	}
	
	@GetMapping(path="/getAll",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAdminTable(){
		return new ResponseEntity<>(this.compliantService.getAdminTable(),HttpStatus.OK);
	}
	
	@PostMapping(path="/addUser",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addUser(@RequestBody User user){
		String[] name = user.getName().split(" ");
		if(name.length == 1) {
			user.setUsername(name[0]+"."+"kumar");
			user.setPassword(TokenGenrator.passwordGenrator(name[0]));
		}else {
		String fpart = name[0];
		String spart = name[1];
		user.setUsername(fpart+"."+spart);
		user.setPassword(TokenGenrator.passwordGenrator(fpart));
	}
		return new ResponseEntity<>(this.compliantService.addUser(user),HttpStatus.OK);
	}
	
	
	@PostMapping(path="/updateUser",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUser(@RequestBody User user){
		return new ResponseEntity<>(this.compliantService.updateUser(user.getId(),user.getRole()),HttpStatus.OK);
	}
		
	@DeleteMapping(path="/deleteUser/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") int id){
		return new ResponseEntity<>(this.compliantService.delete(id),HttpStatus.OK);
	}
	
	
	@PostMapping(path="/{trackingId}/{status}/{field}",produces = MediaType.APPLICATION_PROBLEM_JSON_VALUE)
	public ResponseEntity<?> updateComplaint(@PathVariable("trackingId") String trackingId,@PathVariable("status") String status,@PathVariable("field") String feild ){
		return new ResponseEntity<>(this.compliantService.updateComplaint(trackingId, status, feild),HttpStatus.OK);
	}
	
	@PostMapping(path ="/addEngineer/{id}/{trackingId}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addEngineer(@PathVariable("id") Integer id,@PathVariable("trackingId") String trackingId){
		return new ResponseEntity<>(this.compliantService.addEngineer(id, trackingId),HttpStatus.OK);
	}
	
	@CrossOrigin
	@PostMapping(path="/login",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> login(@RequestBody User user) {
		return new ResponseEntity<>(compliantService.login(user.getUsername(), user.getPassword()),HttpStatus.OK);
	}
	
	
	@CrossOrigin
	@PostMapping(path="/addComplaint",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addComplaint(@RequestBody Complaint complaint) {
		System.out.println(complaint.getName() + " : " + complaint.getStatus());
		String trackingNumber = TokenGenrator.usingUUID().substring(0, 10);
		Map<String, String> out = this.compliantService.addComplaint(trackingNumber,complaint);
		System.out.println(out);
		return new ResponseEntity<>(out,HttpStatus.OK);
	}
	
	
	
	@GetMapping(path="/test",produces = MediaType.ALL_VALUE)
	public String getTested() throws ClassNotFoundException, SQLException {
		return this.compliantService.test();
	}
}
