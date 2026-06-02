export interface Comment {
  id: number;
  content: string;
  author: string;
  createdAt: string;
}

export interface CreateCommentRequest {
  content: string;
}
