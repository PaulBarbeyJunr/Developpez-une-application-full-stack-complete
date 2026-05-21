import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PostDetail } from '../../core/models/post.interface';
import { PostService } from '../../core/services/post.service';

@Component({
  selector: 'app-post-detail',
  templateUrl: './post-detail.component.html',
  styleUrls: ['./post-detail.component.scss'],
})
export class PostDetailComponent implements OnInit {
  post: PostDetail | null = null;
  isLoading = true;
  errorMessage = '';

  commentForm: FormGroup;
  commentError = '';
  isSubmittingComment = false;

  constructor(
    private route: ActivatedRoute,
    private postService: PostService,
    private fb: FormBuilder
  ) {
    this.commentForm = this.fb.group({
      content: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    const id = Number(this.route.snapshot.paramMap.get('id'));
    this.loadPost(id);
  }

  loadPost(id: number): void {
    this.postService.getById(id).subscribe({
      next: (post) => {
        this.post = post;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || "Impossible de charger l'article";
        this.isLoading = false;
      },
    });
  }

  onSubmitComment(): void {
    if (this.commentForm.invalid || !this.post) {
      return;
    }
    this.isSubmittingComment = true;
    this.commentError = '';

    this.postService
      .addComment(this.post.id, this.commentForm.value)
      .subscribe({
        next: (comment) => {
          this.post?.comments.push(comment);
          this.commentForm.reset();
          this.isSubmittingComment = false;
        },
        error: (err) => {
          this.commentError =
            err.error?.message || "Impossible d'ajouter le commentaire";
          this.isSubmittingComment = false;
        },
      });
  }
}
