import { Comment } from './comment.interface';

export interface Post {
  id: number;
  title: string;
  content: string;
  author: string;
  topicTitle: string;
  createdAt: string;
}

export interface PostDetail extends Post {
  comments: Comment[];
}

export interface CreatePostRequest {
  topicId: number;
  title: string;
  content: string;
}

export type SortDirection = 'asc' | 'desc';
