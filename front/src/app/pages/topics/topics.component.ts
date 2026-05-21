import { Component, OnInit } from '@angular/core';
import { Topic } from '../../core/models/topic.interface';
import { TopicService } from '../../core/services/topic.service';

@Component({
  selector: 'app-topics',
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.scss'],
})
export class TopicsComponent implements OnInit {
  topics: Topic[] = [];
  isLoading = true;
  errorMessage = '';

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.loadTopics();
  }

  loadTopics(): void {
    this.topicService.getAll().subscribe({
      next: (topics) => {
        this.topics = topics;
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || 'Impossible de charger les thèmes';
        this.isLoading = false;
      },
    });
  }

  subscribe(topic: Topic): void {
    this.topicService.subscribe(topic.id).subscribe({
      next: () => {
        topic.isSubscribed = true;
      },
      error: (err) => {
        this.errorMessage =
          err.error?.message || "Impossible de s'abonner au thème";
      },
    });
  }
}
