import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { FormsModule } from '@angular/forms'


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './Screens/login/login.component';
import { AddComplaintComponent } from './Screens/add-complaint/add-complaint.component';
import { ViewComplaintComponent } from './Screens/view-complaint/view-complaint.component';
import { HttpClientModule } from '@angular/common/http';
import { UserViewComponent } from './Screens/user-view/user-view.component';
import { HeaderComponent } from './common/header/header.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    AddComplaintComponent,
    ViewComplaintComponent,
    UserViewComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FontAwesomeModule,
    FormsModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
