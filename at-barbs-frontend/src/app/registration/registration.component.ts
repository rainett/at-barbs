import { Component } from '@angular/core';
import {Router, RouterLink} from "@angular/router";
import {AxiosService} from "../axios.service";
import {FormsModule} from "@angular/forms";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {HlmCardDirective} from "@spartan-ng/ui-card-helm";
import {HlmLabelDirective} from "@spartan-ng/ui-label-helm";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";

@Component({
  selector: 'app-registration',
  imports: [
    FormsModule,
    HlmButtonDirective,
    RouterLink,
    HlmCardDirective,
    HlmLabelDirective,
    HlmInputDirective
  ],
  templateUrl: './registration.component.html',
  styleUrl: './registration.component.css'
})
export class RegistrationComponent {
  email: string = "";
  password: string = "";
  firstName: string = "";
  lastName: string = "";

  constructor(private readonly axiosService: AxiosService,
              private readonly router: Router) {}

  onSubmitRegistration(): void {
    this.axiosService.request(
        "POST",
        "/api/v1/auth/register",
        {
          email: this.email,
          password: this.password,
          firstName: this.firstName,
          lastName: this.lastName
        }
    ).then(response => {
      this.axiosService.setAuthToken(response.data.token);
      this.router.navigateByUrl("/");
    });
  }
}
