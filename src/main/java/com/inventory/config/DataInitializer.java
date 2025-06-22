package com.inventory.config;

import com.inventory.entity.DayOfWeek;
import com.inventory.entity.ProductStatus;
import com.inventory.entity.Role;
import com.inventory.entity.Tag;
import com.inventory.repository.DayOfWeekRepository;
import com.inventory.repository.ProductStatusRepository;
import com.inventory.repository.RoleRepository;
import com.inventory.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ProductStatusRepository productStatusRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private DayOfWeekRepository dayOfWeekRepository;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("admin"));
            roleRepository.save(new Role("employee"));
            roleRepository.save(new Role("owner"));
        }

        // Inicializar estados de productos
        if (productStatusRepository.count() == 0) {
            productStatusRepository.save(new ProductStatus("activo"));
            productStatusRepository.save(new ProductStatus("próximo a vencer"));
            productStatusRepository.save(new ProductStatus("vencido"));
        }

        // Inicializar tags predeterminadas
        if (tagRepository.count() == 0) {
            tagRepository.save(new Tag("Frutas"));
            tagRepository.save(new Tag("Verduras"));
            tagRepository.save(new Tag("Lácteos"));
            tagRepository.save(new Tag("Carnes"));
            tagRepository.save(new Tag("Bebidas"));
            tagRepository.save(new Tag("Snacks"));
        }

        // Inicializar días de la semana
        if (dayOfWeekRepository.count() == 0) {
            dayOfWeekRepository.save(new DayOfWeek("Lunes"));
            dayOfWeekRepository.save(new DayOfWeek("Martes"));
            dayOfWeekRepository.save(new DayOfWeek("Miércoles"));
            dayOfWeekRepository.save(new DayOfWeek("Jueves"));
            dayOfWeekRepository.save(new DayOfWeek("Viernes"));
            dayOfWeekRepository.save(new DayOfWeek("Sábado"));
            dayOfWeekRepository.save(new DayOfWeek("Domingo"));
        }
    }
}