import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Topic } from '../../core/models/topic.interface';
import { PostService } from '../../core/services/post.service';
import { TopicService } from '../../core/services/topic.service';

@Component({
  selector: 'app-create-post',
  templateUrl: './create-post.component.html',
  styleUrls: ['./create-post.component.scss'],
})
export class CreatePostComponent implements OnInit {
  postForm: FormGroup;
  topics: Topic[] = [];
  errorMessage = '';
  isSubmitting = false;

  constructor(
    private fb: FormBuilder,
    private postService: PostService,
    private topicService: TopicService,
    private router: Router
  ) {
    this.postForm = this.fb.group({
      topicId: [null, [Validators.required]],
      title: ['', [Validators.required, Validators.maxLength(200)]],
      content: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.topicService.getAll().subscribe({
      next: (topics) => (this.topics = topics),
      error: (err) => {
        this.errorMessage =
          err.error?.message || 'Impossible de charger les thèmes';
      },
    });
  }

  onSubmit(): void {
    if (this.postForm.invalid) {
      return;
    }
    this.isSubmitting = true;
    this.errorMessage = '';

    this.postService.create(this.postForm.value).subscribe({
      next: (post) => this.router.navigate(['/posts', post.id]),
      error: (err) => {
        this.errorMessage =
          err.error?.message || "Erreur lors de la création de l'article";
        this.isSubmitting = false;
      },
    });
  }
}
