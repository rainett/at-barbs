import {Routes} from '@angular/router';
import {WelcomeComponent} from "./welcome/welcome.component";
import {LoginComponent} from "./login/login.component";
import {RegistrationComponent} from "./registration/registration.component";
import {MeComponent} from "./me/me.component";
import {ServicesComponent} from "./services/services.component";
import {StaffSelectionComponent} from "./staff-selection/staff-selection.component";
import {AppointmentConfirmationComponent} from "./appointment-confirmation/appointment-confirmation.component";
import {MyAppointmentsComponent} from "./my-appointments/my-appointments.component";
import {AppointmentComponent} from "./appointment/appointment.component";

export const routes: Routes = [
    {path: '', component: WelcomeComponent},
    {path: 'services', component: ServicesComponent},
    {path: 'login', component: LoginComponent},
    {path: 'registration', component: RegistrationComponent},
    {path: 'me', component: MeComponent},
    {path: 'staff-selection', component: StaffSelectionComponent},
    {path: 'appointment-confirmation', component: AppointmentConfirmationComponent},
    {path: 'my-appointments', component: MyAppointmentsComponent},
    {path: 'appointments/:id', component: AppointmentComponent},
];
