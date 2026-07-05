import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-chatbot',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header bg-primary text-white">
          <h4>Assistant Bancaire IA</h4>
        </div>
        <div class="card-body" style="height: 400px; overflow-y: auto;" #chatBox>
          <div *ngFor="let msg of messages"
               class="mb-3"
               [class.text-end]="msg.role === 'user'">
            <div class="d-inline-block p-2 rounded"
                 [class.bg-primary]="msg.role === 'user'"
                 [class.bg-light]="msg.role === 'bot'"
                 [class.text-white]="msg.role === 'user'">
              <strong>{{ msg.role === 'user' ? 'Vous' : 'Bot' }}:</strong>
              <span class="ms-1">{{ msg.content }}</span>
            </div>
          </div>
        </div>
        <div class="card-footer">
          <div class="input-group">
            <input type="text" class="form-control" [(ngModel)]="userMessage"
                   (keyup.enter)="sendMessage()" placeholder="Posez une question...">
            <button class="btn btn-primary" (click)="sendMessage()" [disabled]="loading">
              {{ loading ? '...' : 'Envoyer' }}
            </button>
          </div>
        </div>
      </div>
    </div>
  `
})
export class ChatbotComponent {
  messages: { role: string; content: string }[] = [
    { role: 'bot', content: 'Bonjour ! Je suis votre assistant bancaire. Posez-moi des questions sur les clients, comptes ou soldes.' }
  ];
  userMessage = '';
  loading = false;

  constructor(private http: HttpClient) {}

  sendMessage(): void {
    if (!this.userMessage.trim()) return;

    this.messages.push({ role: 'user', content: this.userMessage });
    this.loading = true;

    this.http.post<any>('http://localhost:8085/api/chat', { message: this.userMessage })
      .subscribe({
        next: (res) => {
          this.messages.push({ role: 'bot', content: res.reply });
          this.loading = false;
        },
        error: () => {
          this.messages.push({ role: 'bot', content: 'Erreur de connexion au serveur.' });
          this.loading = false;
        }
      });

    this.userMessage = '';
  }
}
