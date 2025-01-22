import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AxiosService } from "../axios.service";
import { HlmButtonDirective } from "@spartan-ng/ui-button-helm";
import { NgForOf } from "@angular/common";
import {
    HlmCardContentDirective,
    HlmCardDescriptionDirective,
    HlmCardDirective,
    HlmCardFooterDirective,
    HlmCardHeaderDirective,
    HlmCardTitleDirective
} from "@spartan-ng/ui-card-helm";

@Component({
    selector: 'app-services',
    templateUrl: './services.component.html',
    styleUrls: ['./services.component.css'],
    imports: [
        HlmButtonDirective,
        NgForOf,
        HlmCardDirective,
        HlmCardHeaderDirective,
        HlmCardTitleDirective,
        HlmCardDescriptionDirective,
        HlmCardContentDirective,
        HlmCardFooterDirective
    ]
})
export class ServicesComponent implements OnInit {
    services: any[] = []; // List of services for the current page
    totalPages = 0; // Total number of pages
    currentPage = 0; // Current page number (zero-based)
    pageSize = 5; // Number of services per page
    totalElements = 0; // Total number of elements

    isLoggedIn = false; // Authentication state

    constructor(private readonly axiosService: AxiosService,
                private readonly router: Router) {
    }

    ngOnInit(): void {
        this.checkAuthStatus();
        this.loadServices(this.currentPage); // Load the first page of services
    }

    // Load services for a specific page
    loadServices(page: number): void {
        const url = `/api/v1/services?page=${page}&size=${this.pageSize}`;
        this.axiosService
            .request('get', url, null)
            .then((response) => {
                const data = response.data;
                this.services = data.content; // Extract services for the current page
                this.totalPages = data.totalPages;
                this.totalElements = data.totalElements;
                this.currentPage = data.pageable.pageNumber;
            })
            .catch((error) => {
                console.error('Error loading services:', error);
            });
    }

    // Check authentication status
    checkAuthStatus(): void {
        const authToken = this.axiosService.getAuthToken();
        this.isLoggedIn = authToken !== null;
    }

    // Handle service selection
    handleServiceSelection(service: any): void {
        if (this.isLoggedIn) {
            // Proceed to staff selection if logged in
            this.router.navigate(['/staff-selection'], { queryParams: { serviceId: service.id } });
        } else {
            // Redirect to login if not logged in
            this.router.navigate(['/login']);
        }
    }

    // Handle pagination navigation
    goToPage(page: number): void {
        if (page >= 0 && page < this.totalPages) {
            this.loadServices(page);
        }
    }
}
