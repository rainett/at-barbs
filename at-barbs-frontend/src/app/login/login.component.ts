import {Component} from '@angular/core';
import {FormsModule} from "@angular/forms";
import {Router, RouterLink} from "@angular/router";
import {AxiosService} from "../axios.service";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {HlmCardDirective} from "@spartan-ng/ui-card-helm";
import {HlmLabelDirective} from "@spartan-ng/ui-label-helm";
import {HlmInputDirective} from "@spartan-ng/ui-input-helm";

@Component({
    selector: 'app-login',
    imports: [
        FormsModule,
        RouterLink,
        HlmButtonDirective,
        HlmCardDirective,
        HlmLabelDirective,
        HlmInputDirective
    ],
    templateUrl: './login.component.html',
    styleUrl: './login.component.css'
})
export class LoginComponent {
    email: string = "";
    password: string = "";

    constructor(private readonly axiosService: AxiosService,
                private readonly router: Router) {
    }

    onSubmitLogin(): void {
        this.axiosService.request(
            "POST",
            "/api/v1/auth/login",
            {
                email: this.email,
                password: this.password
            }
        ).then(response => {
            this.axiosService.setAuthToken(response.data.token);
            this.router.navigateByUrl("/");
        });
    }
}
