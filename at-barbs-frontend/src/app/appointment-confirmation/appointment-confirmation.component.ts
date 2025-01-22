import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {AxiosService} from '../axios.service';
import {
    HlmCardContentDirective,
    HlmCardDescriptionDirective,
    HlmCardDirective,
    HlmCardHeaderDirective,
    HlmCardTitleDirective
} from "@spartan-ng/ui-card-helm";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {NgForOf, NgIf} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {HlmCalendarComponent} from "@spartan-ng/ui-calendar-helm";
import {HlmAlertDescriptionDirective, HlmAlertDirective, HlmAlertTitleDirective} from "@spartan-ng/ui-alert-helm";

@Component({
    selector: 'app-appointment-confirmation',
    templateUrl: './appointment-confirmation.component.html',
    imports: [
        HlmCardDirective,
        HlmCardTitleDirective,
        HlmCardDescriptionDirective,
        HlmButtonDirective,
        NgForOf,
        FormsModule,
        HlmCardContentDirective,
        HlmCardHeaderDirective,
        HlmCalendarComponent,
        NgIf,
        HlmAlertDirective,
        HlmAlertTitleDirective,
        HlmAlertDescriptionDirective
    ],
    styleUrls: ['./appointment-confirmation.component.css']
})
export class AppointmentConfirmationComponent implements OnInit {
    selectedService: any = null;
    selectedStaff: any = null;
    availableTimeSlots: string[] = [];

    selectedTimeSlot: string | null = null;

    selectedDate = new Date();
    minDate: Date = new Date(Date.now());
    maxDate = new Date(Date.now() + 7 * 24 * 60 * 60 * 1000 * 24);

    isTimeChecked: boolean = false; // Indicates whether the "Check Time" button has been clicked

    constructor(private readonly route: ActivatedRoute,
                private readonly router: Router,
                private readonly axiosService: AxiosService) {
    }

    ngOnInit(): void {
        this.route.queryParams.subscribe((params) => {
            const serviceId = params['serviceId'];
            const staffId = params['staffId'];

            if (serviceId && staffId) {
                this.loadServiceDetails(serviceId);
                this.loadStaffDetails(staffId);
            } else {
                this.router.navigate(['/services']);
            }
        });
    }

    loadServiceDetails(serviceId: string): void {
        const url = `/api/v1/services/${serviceId}`;
        this.axiosService.request('get', url, null).then((response) => {
            this.selectedService = response.data;
        });
    }

    loadStaffDetails(staffId: string): void {
        const url = `/api/v1/staff/${staffId}`;
        this.axiosService.request('get', url, null).then((response) => {
            this.selectedStaff = response.data;
            this.selectedStaff.startTime = this.formatTime(this.selectedStaff.startTime);
            this.selectedStaff.endTime = this.formatTime(this.selectedStaff.endTime);
        });
    }

    selectTimeSlot(slot: string): void {
        this.selectedTimeSlot = slot;
    }

    confirmAppointment(): void {
        if (!this.selectedDate || !this.selectedTimeSlot) {
            alert('Please select a date and time slot.');
            return;
        }
        const formattedDate = `${this.selectedDate.getFullYear()}-${(this.selectedDate.getMonth() + 1).toString().padStart(2, '0')}-${this.selectedDate.getDate().toString().padStart(2, '0')}`;
        const payload = {
            serviceId: this.selectedService.id,
            staffId: this.selectedStaff.id,
            date: formattedDate,
            timeSlot: this.selectedTimeSlot,
        };
        console.log(formattedDate);
        this.axiosService.request('post', '/api/v1/appointments', payload).then(() => {
            this.router.navigate(['/my-appointments']);
        });
    }

    private formatTime(time: string): string {
        const [hours, minutes] = time.split(':');
        return `${hours}:${minutes}`;
    }

    checkTime(): void {
        if (!this.selectedDate) {
            alert('Please select a date first.');
            return;
        }
        this.loadAvailableTimeSlots(this.selectedStaff.id, this.selectedDate);
        this.isTimeChecked = true;
    }

    loadAvailableTimeSlots(staffId: string, date: Date): void {
        const formattedDate = `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
        const url = `/api/v1/appointments/available-time-slots?staffId=${staffId}&date=${formattedDate}`;

        this.axiosService.request('get', url, null).then((response) => {
            this.availableTimeSlots = response.data.map((slot: any) => slot.time.slice(0, 5));
        });
    }
}
