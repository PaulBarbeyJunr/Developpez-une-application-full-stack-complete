import { Topic } from './topic.interface';

export interface UserProfile {
  id: number;
  username: string;
  email: string;
  subscriptions: Topic[];
}

export interface UpdateProfileRequest {
  username?: string;
  email?: string;
  password?: string;
}
