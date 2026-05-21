import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';

const PASSWORD_PATTERN =
  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,}$/;

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss'],
})
export class RegisterComponent {
  registerForm: FormGroup;
  errorMessage = '';
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: [
        '',
        [Validators.required, Validators.pattern(PASSWORD_PATTERN)],
      ],
    });
  }

  onSubmit(): void {
    if (this.registerForm.invalid) {
      return;
    }
    this.isSubmitting = true;
    this.errorMessage = '';

    this.authService.register(this.registerForm.value).subscribe({
      next: () => this.router.navigate(['/feed']),
      error: (err) => {
        this.errorMessage =
          err.error?.message || "Erreur lors de l'inscription";
        this.isSubmitting = false;
      },
    });
  }
}
