import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Topic } from '../../core/models/topic.interface';
import { TopicService } from '../../core/services/topic.service';

@Component({
  selector: 'app-topics',
  templateUrl: './topics.component.html',
  styleUrls: ['./topics.component.scss'],
})
export class TopicsComponent implements OnInit {
  topics$!: Observable<Topic[]>;

  constructor(private topicService: TopicService) {}

  ngOnInit(): void {
    this.topics$ = this.topicService.getAll();
  }
}
