import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { UserService } from '../../core/services/user.service';
import { UserProfile } from '../../core/models/user-profile.interface';

const PASSWORD_PATTERN =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.scss'],
})
export class ProfileComponent implements OnInit {
  profileForm: FormGroup;
  profile: UserProfile | null = null;
  errorMessage = '';
  successMessage = '';
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private userService: UserService,
    private authService: AuthService,
    private router: Router
  ) {
    this.profileForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.pattern(PASSWORD_PATTERN)]],
    });
  }

  ngOnInit(): void {
    this.loadProfile();
  }

  loadProfile(): void {
    this.userService.getProfile().subscribe({
      next: (profile) => {
        this.profile = profile;
        this.profileForm.patchValue({
          username: profile.username,
          email: profile.email,
        });
      },
      error: (err) => {
        this.errorMessage = err.error?.message || 'Impossible de charger le profil';
      },
    });
  }

  onSubmit(): void {
    if (this.profileForm.invalid) {
      return;
    }
    this.isSubmitting = true;
    this.errorMessage = '';
    this.successMessage = '';

    const formValue = this.profileForm.value;
    const usernameChanged = formValue.username !== this.profile?.username;
    const emailChanged = formValue.email !== this.profile?.email;

    const request = {
      username: formValue.username,
      email: formValue.email,
      ...(formValue.password ? { password: formValue.password } : {}),
    };

    this.userService.updateProfile(request).subscribe({
      next: () => {
        this.successMessage = 'Profil mis à jour';
        this.isSubmitting = false;
        this.profileForm.patchValue({ password: '' });

        if (usernameChanged || emailChanged) {
          this.successMessage += ' — reconnexion requise.';
          setTimeout(() => this.logout(), 1500);
        } else {
          this.loadProfile();
        }
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || 'Erreur lors de la mise à jour';
        this.isSubmitting = false;
      },
    });
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate(['/']);
  }
}
