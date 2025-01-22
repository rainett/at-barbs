import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AxiosService} from "../axios.service";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {NgForOf} from "@angular/common";
import {
    HlmCardContentDirective,
    HlmCardDescriptionDirective,
    HlmCardDirective,
    HlmCardFooterDirective,
    HlmCardHeaderDirective,
    HlmCardTitleDirective
} from "@spartan-ng/ui-card-helm";

@Component({
    selector: 'app-staff-selection',
    templateUrl: './staff-selection.component.html',
    styleUrls: ['./staff-selection.component.css'],
    imports: [
        HlmButtonDirective,
        NgForOf,
        HlmCardDirective,
        HlmCardHeaderDirective,
        HlmCardTitleDirective,
        HlmCardDescriptionDirective,
        HlmCardContentDirective,
        HlmCardFooterDirective,
    ]
})
export class StaffSelectionComponent implements OnInit {
    staffList: any[] = []; // List of staff members for the current page
    totalPages = 0; // Total number of pages
    currentPage = 0; // Current page number (zero-based)
    pageSize = 5; // Number of staff per page
    totalElements = 0; // Total number of elements

    isLoggedIn = false; // Authentication state

    selectedServiceId: string | null = null; // ID of the selected service

    constructor(
        private readonly axiosService: AxiosService,
        private readonly router: Router,
        private readonly route: ActivatedRoute
    ) {
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe((params) => {
            this.selectedServiceId = params['serviceId'] || null;
            if (this.selectedServiceId) {
                this.loadStaff(this.currentPage);
            } else {
                this.router.navigate(['/services']);
            }
        });
        this.checkAuthStatus();
    }

    // Load staff for a specific page
    loadStaff(page: number): void {
        const url = `/api/v1/staff/by-service?serviceId=${this.selectedServiceId}&page=${page}&size=${this.pageSize}`;
        this.axiosService
            .request('get', url, null)
            .then((response) => {
                const data = response.data;
                this.staffList = data.content.map((staff: any) => ({
                    ...staff,
                    startTime: this.formatTime(staff.startTime),
                    endTime: this.formatTime(staff.endTime),
                }));
                this.totalPages = data.totalPages;
                this.totalElements = data.totalElements;
                this.currentPage = data.pageable.pageNumber;
            })
            .catch((error) => {
                console.error('Error loading staff:', error);
            });
    }

    checkAuthStatus(): void {
        const authToken = this.axiosService.getAuthToken();
        this.isLoggedIn = authToken !== null;
    }

    // Handle staff selection
    onSelectStaff(staff: any): void {
        if (this.isLoggedIn) {
            this.router.navigate(['/appointment-confirmation'], {
                queryParams: {serviceId: this.selectedServiceId, staffId: staff.id}
            });
        } else {
            this.router.navigate(['/login']);
        }
    }

    // Handle pagination navigation
    goToPage(page: number): void {
        if (page >= 0 && page < this.totalPages) {
            this.loadStaff(page);
        }
    }

    private formatTime(time: string): string {
        const [hours, minutes] = time.split(':');
        return `${hours}:${minutes}`;
    }
}
