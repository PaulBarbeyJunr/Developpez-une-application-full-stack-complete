import { Component, OnInit } from '@angular/core';
import { Post, SortDirection } from '../../core/models/post.interface';
import { PostService } from '../../core/services/post.service';

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.scss'],
})
export class FeedComponent implements OnInit {
  posts: Post[] = [];
  sort: SortDirection = 'desc';
  isLoading = true;
  errorMessage = '';

  constructor(private postService: PostService) {}

  ngOnInit(): void {
    this.loadFeed();
  }

  loadFeed(): void {
    this.isLoading = true;
    this.postService.getFeed(this.sort).subscribe({
      next: (posts) => {
        this.posts = posts;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || "Impossible de charger le fil d'actualité";
        this.isLoading = false;
      },
    });
  }

  toggleSort(): void {
    this.sort = this.sort === 'desc' ? 'asc' : 'desc';
    this.loadFeed();
  }
}
