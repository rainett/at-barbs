import {Component, Input} from '@angular/core';
import {NgIf} from "@angular/common";
import {RouterLink} from "@angular/router";
import {HlmButtonDirective} from '@spartan-ng/ui-button-helm';

@Component({
    selector: 'app-header',
    imports: [
        NgIf,
        RouterLink,
        HlmButtonDirective
    ],
    templateUrl: './header.component.html',
    styleUrl: './header.component.css'
})
export class HeaderComponent {
    @Input() pageTitle!: string;
    @Input() logoSrc!: string;

    isLoggedIn(): boolean {
        return window.localStorage.getItem("auth_token") !== null;
    }
}
