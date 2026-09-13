package com.jobtracker.job_tracker_backend.service;

import com.jobtracker.job_tracker_backend.dto.ApplicationRequest;
import com.jobtracker.job_tracker_backend.dto.ApplicationResponse;
import com.jobtracker.job_tracker_backend.entity.Application;
import com.jobtracker.job_tracker_backend.entity.User;
import com.jobtracker.job_tracker_backend.exception.AccessDeniedException;
import com.jobtracker.job_tracker_backend.exception.ResourceNotFoundException;
import com.jobtracker.job_tracker_backend.repository.ApplicationRepository;
import com.jobtracker.job_tracker_backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplicationServiceTest {

    @Mock
    private ApplicationRepository applicationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ApplicationService applicationService;

    private User owner;
    private User otherUser;
    private Application application;

    @BeforeEach
    void setUp() {
        owner = User.builder()
                .id(1L)
                .email("owner@example.com")
                .name("Owner")
                .password("hashed")
                .role(User.Role.USER)
                .build();

        otherUser = User.builder()
                .id(2L)
                .email("other@example.com")
                .name("Other")
                .password("hashed")
                .role(User.Role.USER)
                .build();

        application = Application.builder()
                .id(100L)
                .user(owner)
                .companyName("Google")
                .jobTitle("Backend Engineer")
                .status(Application.Status.APPLIED)
                .build();
    }

    @Test
    void create_shouldSaveApplicationWithDefaultStatus_whenStatusNotProvided() {
        ApplicationRequest request = new ApplicationRequest();
        request.setCompanyName("Amazon");
        request.setJobTitle("SDE-1");

        when(userRepository.findByEmail("owner@example.com")).thenReturn(Optional.of(owner));
        when(applicationRepository.save(any(Application.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ApplicationResponse response = applicationService.create("owner@example.com", request);

        assertThat(response.getCompanyName()).isEqualTo("Amazon");
        assertThat(response.getStatus()).isEqualTo(Application.Status.APPLIED);
        verify(applicationRepository).save(any(Application.class));
    }

    @Test
    void getById_shouldReturnApplication_whenRequestedByOwner() {
        when(applicationRepository.findById(100L)).thenReturn(Optional.of(application));

        ApplicationResponse response = applicationService.getById("owner@example.com", 100L);

        assertThat(response.getId()).isEqualTo(100L);
        assertThat(response.getCompanyName()).isEqualTo("Google");
    }

    @Test
    void getById_shouldThrowAccessDenied_whenRequestedByNonOwner() {
        when(applicationRepository.findById(100L)).thenReturn(Optional.of(application));

        assertThatThrownBy(() -> applicationService.getById("other@example.com", 100L))
                .isInstanceOf(AccessDeniedException.class)
                .hasMessageContaining("do not have access");
    }

    @Test
    void getById_shouldThrowResourceNotFound_whenApplicationDoesNotExist() {
        when(applicationRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> applicationService.getById("owner@example.com", 999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("not found");
    }

    @Test
    void delete_shouldRemoveApplication_whenRequestedByOwner() {
        when(applicationRepository.findById(100L)).thenReturn(Optional.of(application));

        applicationService.delete("owner@example.com", 100L);

        verify(applicationRepository).delete(application);
    }

    @Test
    void delete_shouldThrowAccessDenied_whenRequestedByNonOwner() {
        when(applicationRepository.findById(100L)).thenReturn(Optional.of(application));

        assertThatThrownBy(() -> applicationService.delete("other@example.com", 100L))
                .isInstanceOf(AccessDeniedException.class);

        verify(applicationRepository, never()).delete(any());
    }
}