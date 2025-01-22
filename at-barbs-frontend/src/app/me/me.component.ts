import { Component } from '@angular/core';
import {Router} from "@angular/router";
import {
  HlmCardContentDirective,
  HlmCardDescriptionDirective,
  HlmCardDirective,
  HlmCardHeaderDirective,
  HlmCardTitleDirective
} from "@spartan-ng/ui-card-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";

@Component({
  selector: 'app-me',
  imports: [
    HlmCardDirective,
    HlmCardHeaderDirective,
    HlmCardTitleDirective,
    HlmCardDescriptionDirective,
    HlmCardContentDirective,
    HlmButtonDirective
  ],
  templateUrl: './me.component.html',
  styleUrl: './me.component.css'
})
export class MeComponent {
  constructor(private readonly router: Router) {}

  onLogOut(): void {
    window.localStorage.removeItem("auth_token");
    this.router.navigateByUrl("/");
  }
}
