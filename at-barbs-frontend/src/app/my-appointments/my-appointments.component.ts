import {Component, OnInit} from '@angular/core';
import {
  HlmCardContentDirective,
  HlmCardDescriptionDirective,
  HlmCardDirective, HlmCardFooterDirective,
  HlmCardHeaderDirective,
  HlmCardTitleDirective
} from "@spartan-ng/ui-card-helm";
import {NgForOf, NgIf} from "@angular/common";
import {HlmButtonDirective} from "@spartan-ng/ui-button-helm";
import {AxiosService} from "../axios.service";
import {RouterLink} from "@angular/router";

@Component({
  selector: 'app-my-appointments',
  imports: [
    HlmCardDirective,
    HlmCardTitleDirective,
    HlmCardDescriptionDirective,
    NgIf,
    HlmCardHeaderDirective,
    HlmCardContentDirective,
    NgForOf,
    HlmButtonDirective,
    HlmCardFooterDirective,
    RouterLink
  ],
  templateUrl: './my-appointments.component.html',
  styleUrl: './my-appointments.component.css'
})
export class MyAppointmentsComponent implements OnInit {
  appointments: any[] = [];
  totalPages = 0;
  totalElements = 0;
  currentPage = 0;
  pageSize = 10;

  constructor(private readonly axiosService: AxiosService) {}

  ngOnInit(): void {
    this.loadAppointments(this.currentPage);
  }

  // Load user's appointments
  loadAppointments(page: number): void {
    const url = `/api/v1/appointments/customer?page=${page}&size=${this.pageSize}`;
    this.axiosService.request('get', url, null).then((response) => {
      this.appointments = response.data.content.map((appointment: any) => {
        const [date, time] = appointment.appointmentTime.split('T');
        return {
          ...appointment,
          appointmentTime: {
            date: date,
            time: time.slice(0, 5), // Extract hours and minutes
          },
        };
      });
      this.totalPages = response.data.totalPages;
      this.totalElements = response.data.totalElements;
      this.currentPage = response.data.number;
    }).catch((error) => {
      console.error('Error loading appointments:', error);
    });
  }

  // Pagination
  goToPage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.loadAppointments(page);
    }
  }
}
