import {Component, OnInit} from '@angular/core';
import {
    HlmCardContentDirective,
    HlmCardDescriptionDirective,
    HlmCardDirective, HlmCardFooterDirective,
    HlmCardHeaderDirective,
    HlmCardTitleDirective
} from "@spartan-ng/ui-card-helm";
import {ActivatedRoute, Router} from "@angular/router";
import {AxiosService} from "../axios.service";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {NgIf} from "@angular/common";

@Component({
    selector: 'app-appointment',
    imports: [
        HlmCardContentDirective,
        HlmCardDescriptionDirective,
        HlmCardDirective,
        HlmCardHeaderDirective,
        HlmCardTitleDirective,
        HlmButtonDirective,
        HlmCardFooterDirective,
        NgIf
    ],
    templateUrl: './appointment.component.html',
    styleUrl: './appointment.component.css'
})
export class AppointmentComponent implements OnInit {
    appointment: any = null;
    service: any = null;
    staff: any = null;

    constructor(
        private readonly route: ActivatedRoute,
        private readonly router: Router,
        private readonly axiosService: AxiosService
    ) {
    }

    ngOnInit(): void {
        const appointmentId = this.route.snapshot.paramMap.get('id');
        if (appointmentId) {
            this.loadInitialData(appointmentId);
        } else {
            this.router.navigate(['/appointments']);
        }
    }

    async loadInitialData(appointmentId: string): Promise<void> {
        try {
            await this.loadAppointmentDetails(appointmentId);
            if (this.appointment) {
                await this.loadServiceDetails(this.appointment.serviceId);
                await this.loadStaffDetails(this.appointment.staffId);
            }
        } catch (error) {
            console.error('Error loading initial data:', error);
            await this.router.navigate(['/appointments']);
        }
    }

    async loadAppointmentDetails(id: string): Promise<void> {
        const url = `/api/v1/appointments/${id}`;
        const response = await this.axiosService.request('get', url, null);
        response.data.date = response.data.appointmentTime.split('T')[0];
        response.data.time = response.data.appointmentTime.split('T')[1];
        this.appointment = response.data;
    }

    async loadServiceDetails(serviceId: string): Promise<void> {
        const url = `/api/v1/services/${serviceId}`;
        const response = await this.axiosService.request('get', url, null);
        this.service = response.data;
    }

    async loadStaffDetails(staffId: string): Promise<void> {
        const url = `/api/v1/staff/${staffId}`;
        const response = await this.axiosService.request('get', url, null);
        this.staff = response.data;
        this.staff.startTime = this.formatTime(this.staff.startTime);
        this.staff.endTime = this.formatTime(this.staff.endTime);
    }


    private formatTime(time: string): string {
        const [hours, minutes] = time.split(':');
        return `${hours}:${minutes}`;
    }

    onCancelAppointment() {
        const url = `/api/v1/appointments/cancel/${this.appointment.id}`;
        this.axiosService.request('put', url, null).then(() => {
            this.router.navigate(['/my-appointments']);
        }).catch((error) => {
            console.error('Error canceling appointment:', error);
            alert('Failed to cancel the appointment. Please try again later.');
        });
    }
}
