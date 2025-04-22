package com.study.projects.percursos_van.web.controller;

import com.study.projects.percursos_van.jwt.JwtUserDetails;
import com.study.projects.percursos_van.model.Driver;
import com.study.projects.percursos_van.repository.projection.DriverProjection;
import com.study.projects.percursos_van.service.DriverService;
import com.study.projects.percursos_van.service.UserService;
import com.study.projects.percursos_van.web.controller.dto.driver.DriverCreateDTO;
import com.study.projects.percursos_van.web.controller.dto.driver.DriverResponseDTO;
import com.study.projects.percursos_van.web.controller.dto.pageable.PageableDTO;
import com.study.projects.percursos_van.web.mapper.driver.DriverMapper;
import com.study.projects.percursos_van.web.mapper.page.PageableMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<DriverResponseDTO> insert(
            @AuthenticationPrincipal JwtUserDetails details,
            @RequestBody @Valid DriverCreateDTO dto
    ) {
        Driver result = driverService.insert(DriverMapper.toDriver(dto));

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(result.getId()).toUri();
        return ResponseEntity.created(uri).body(DriverMapper.toResponseDTO(result));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<DriverResponseDTO> getById(@PathVariable Integer id){
        Driver driver = driverService.findById(id);
        return ResponseEntity.ok(DriverMapper.toResponseDTO(driver));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping
    public ResponseEntity<PageableDTO> getAll(@PageableDefault(size = 5, sort = "id") Pageable pageable,
                                              @RequestParam(required = false, name = "cnhCat") String cnhCat){
        Page<DriverProjection> result = driverService.findAll(cnhCat, pageable);
        return ResponseEntity.ok(PageableMapper.toPageableDTO(result));
    }

    @PreAuthorize("hasAuthority('DRIVER')")
    @GetMapping("/self")
    public ResponseEntity<DriverResponseDTO> getPersonalInfo(@AuthenticationPrincipal JwtUserDetails details){
        Driver driver = driverService.getByUser(userService.findById(details.getId()));
        return ResponseEntity.ok(DriverMapper.toResponseDTO(driver));
    }
}
