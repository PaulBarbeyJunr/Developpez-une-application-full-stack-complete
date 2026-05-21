import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { Topic } from '../models/topic.interface';

@Injectable({
  providedIn: 'root',
})
export class TopicService {
  private readonly apiUrl = `${environment.apiUrl}/topics`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Topic[]> {
    return this.http.get<Topic[]>(this.apiUrl);
  }

  subscribe(topicId: number): Observable<void> {
    return this.http.post<void>(`${this.apiUrl}/${topicId}/subscribe`, {});
  }

  unsubscribe(topicId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${topicId}/subscribe`);
  }
}
