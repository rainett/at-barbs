package com.rainett.atbarbsbackend.init;

import com.rainett.atbarbsbackend.model.Service;
import com.rainett.atbarbsbackend.model.users.Customer;
import com.rainett.atbarbsbackend.model.users.Staff;
import com.rainett.atbarbsbackend.model.users.User;
import com.rainett.atbarbsbackend.repository.CustomerRepository;
import com.rainett.atbarbsbackend.repository.ServiceRepository;
import com.rainett.atbarbsbackend.repository.StaffRepository;
import com.rainett.atbarbsbackend.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class DataInitializer {
    private static final String DEFAULT_PASSWORD = "sappwird";
    private static final String[] FIRST_NAMES =
            {"John", "Jane", "Michael", "Emily", "David", "Sophia", "William", "Olivia", "James",
                    "Emma"};
    private static final String[] LAST_NAMES =
            {"Smith", "Johnson", "Brown", "Davis", "Miller", "Wilson", "Moore", "Taylor",
                    "Anderson", "Thomas"};
    private static final String[] SPECIALIZATIONS =
            {"Barber", "Stylist", "Therapist", "Hairdresser", "Masseuse", "Nail Technician",
                    "Makeup Artist", "Aesthetician"};
    private static final Set<String> USED_EMAILS = new HashSet<>();

    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final StaffRepository staffRepository;
    private final ServiceRepository serviceRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initializeDatabase() {
        initializeCustomers();
        initializeServices();
        initializeStaff();
    }

    private void initializeCustomers() {
        if (userRepository.count() > 0) {
            return;
        }
        List<Customer> customers = Stream.generate(() -> generateUser(Customer::new))
                .limit(10)
                .toList();
        customerRepository.saveAll(customers);
    }

    private void initializeStaff() {
        if (staffRepository.count() > 0) {
            return;
        }
        List<Staff> staff = Stream.generate(this::generateStaff)
                .limit(10)
                .toList();
        staffRepository.saveAll(staff);
    }

    private void initializeServices() {
        if (serviceRepository.count() > 0) {
            return;
        }
        List<Service> services = new ArrayList<>();

        services.add(new Service(null, "Haircut",
                "A professional haircut tailored to your style.", 30, 25.00));
        services.add(new Service(null, "Beard Trim",
                "A precise beard trim for a sharp look.", 15, 15.00));
        services.add(new Service(null, "Hair Wash",
                "Relaxing shampoo and conditioning service.", 10, 10.00));
        services.add(new Service(null, "Haircut + Beard Trim",
                "A combination of haircut and beard styling.", 45, 35.00));
        services.add(new Service(null, "Coloring",
                "Professional hair coloring service.", 60, 50.00));
        services.add(new Service(null, "Scalp Treatment",
                "A soothing scalp massage and treatment.", 20, 30.00));
        services.add(new Service(null, "Shave",
                "A clean, close shave for a polished look.", 20, 20.00));
        services.add(new Service(null, "Kids Haircut",
                "A fun and professional haircut for children.", 25, 20.00));

        serviceRepository.saveAll(services);
    }

    private Staff generateStaff() {
        List<Service> allServices = serviceRepository.findAll();
        Staff staff = generateUser(Staff::new);
        staff.setSpecialization(getRandomFromArray(SPECIALIZATIONS));
        staff.setStartTime(LocalTime.of(8, 0));
        staff.setEndTime(LocalTime.of(17, 0));

        List<Service> assignedServices = getRandomSubset(allServices);
        staff.setServices(assignedServices);

        return staff;
    }

    private List<Service> getRandomSubset(List<Service> services) {
        int subsetSize = ThreadLocalRandom.current().nextInt(1, services.size() + 1);
        Collections.shuffle(services);
        return services.subList(0, subsetSize);
    }

    private <T extends User> T generateUser(Supplier<T> userSupplier) {
        T user = userSupplier.get();
        while (true) {
            String firstName = getRandomFromArray(FIRST_NAMES);
            String lastName = getRandomFromArray(LAST_NAMES);
            String email = generateEmail(firstName, lastName);
            if (!userRepository.existsByEmail(email) && !USED_EMAILS.contains(email)) {
                user.setFirstName(firstName);
                user.setLastName(lastName);
                user.setEmail(email);
                USED_EMAILS.add(user.getEmail());
                break;
            }
        }
        user.setPasswordHash(passwordEncoder.encode(DEFAULT_PASSWORD));
        return user;
    }

    private static String generateEmail(String firstName, String lastName) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com";
    }

    private static String getRandomFromArray(String[] array) {
        return array[ThreadLocalRandom.current().nextInt(array.length)];
    }
}
