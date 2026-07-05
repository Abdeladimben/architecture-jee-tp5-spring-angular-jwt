import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header bg-primary text-white d-flex justify-content-between">
          <h3>Gestion des Clients</h3>
          <a routerLink="/customers/add" class="btn btn-success btn-sm">+ Nouveau Client</a>
        </div>
        <div class="card-body">
          <div class="mb-3">
            <input type="text" class="form-control" placeholder="Rechercher un client..."
                   [(ngModel)]="keyword" (input)="search()">
          </div>
          <table class="table table-striped">
            <thead><tr><th>ID</th><th>Nom</th><th>Email</th><th>Actions</th></tr></thead>
            <tbody>
              <tr *ngFor="let c of customers">
                <td>{{ c.id }}</td>
                <td>{{ c.name }}</td>
                <td>{{ c.email }}</td>
                <td>
                  <a [routerLink]="['/customers/edit', c.id]" class="btn btn-sm btn-warning me-1">Modifier</a>
                  <button (click)="deleteCustomer(c.id!)" class="btn btn-sm btn-danger">Supprimer</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `
})
export class CustomersComponent implements OnInit {
  customers: Customer[] = [];
  keyword = '';

  constructor(private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadCustomers();
  }

  loadCustomers(): void {
    this.customerService.getCustomers(this.keyword).subscribe(data => this.customers = data);
  }

  search(): void {
    this.loadCustomers();
  }

  deleteCustomer(id: number): void {
    if (confirm('Supprimer ce client ?')) {
      this.customerService.deleteCustomer(id).subscribe(() => this.loadCustomers());
    }
  }
}
