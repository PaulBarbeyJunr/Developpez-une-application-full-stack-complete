import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import {
  Comment,
  CreateCommentRequest,
} from '../models/comment.interface';
import {
  CreatePostRequest,
  Post,
  PostDetail,
  SortDirection,
} from '../models/post.interface';

@Injectable({
  providedIn: 'root',
})
export class PostService {
  private readonly apiUrl = `${environment.apiUrl}/posts`;

  constructor(private http: HttpClient) {}

  getFeed(sort: SortDirection): Observable<Post[]> {
    const params = new HttpParams().set('sort', sort);
    return this.http.get<Post[]>(this.apiUrl, { params });
  }

  getById(id: number): Observable<PostDetail> {
    return this.http.get<PostDetail>(`${this.apiUrl}/${id}`);
  }

  create(request: CreatePostRequest): Observable<PostDetail> {
    return this.http.post<PostDetail>(this.apiUrl, request);
  }

  addComment(
    postId: number,
    request: CreateCommentRequest
  ): Observable<Comment> {
    return this.http.post<Comment>(
      `${this.apiUrl}/${postId}/comments`,
      request
    );
  }
}
