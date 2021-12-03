import { Component, OnInit } from '@angular/core';
import { user } from '../../model/user'
import { ComplaintServiceService } from 'src/app/Service/complaint-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  username: string = ""
  password: string = ""
  response : any = {"role" : "","userId" : ""}

  constructor(private service:ComplaintServiceService, private router :Router) { }

  ngOnInit(): void { 
    var role = localStorage.getItem("ROLE")
    if(role === 'MANAGER' || role === 'ENGINEER' || role === 'ADMIN'){
      this.router.navigateByUrl('viewComplaint');
    }
    if(role === 'USER'){
      this.router.navigateByUrl('addComplalint')
    }
  }

  
  onSubmit = () => {
    
    
    
    this.service.login(this.username,this.password).subscribe(result => {
      this.response = JSON.parse(JSON.stringify(result))
      const role = this.response["role"];
      const userId = this.response["userId"];
      if(role === undefined || userId === undefined){
        alert("check your username or password")
        location.reload();
      }else{
        localStorage.setItem("ROLE",role);
        localStorage.setItem("userId",userId);
        this.service.session();
        if(role === 'USER')
          this.router.navigateByUrl("addComplaint")
        else
          this.router.navigateByUrl("viewComplaint")
      }
    });
    
    
    
    }
    
  

  //TODO: implementation left
  forgetPassword = () => {
    console.log("enter the id and give a code")
  }

}
