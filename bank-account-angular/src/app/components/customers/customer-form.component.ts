import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { CustomerService } from '../../services/customer.service';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-customer-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="container mt-4">
      <div class="card">
        <div class="card-header" [class.bg-success]="!isEdit" [class.bg-warning]="isEdit" class="text-white">
          <h3>{{ isEdit ? 'Modifier' : 'Ajouter' }} un Client</h3>
        </div>
        <div class="card-body">
          <form (ngSubmit)="save()">
            <div class="mb-3">
              <label class="form-label">Nom</label>
              <input type="text" class="form-control" [(ngModel)]="customer.name" name="name" required>
            </div>
            <div class="mb-3">
              <label class="form-label">Email</label>
              <input type="email" class="form-control" [(ngModel)]="customer.email" name="email" required>
            </div>
            <button type="submit" class="btn btn-primary">{{ isEdit ? 'Mettre à jour' : 'Enregistrer' }}</button>
            <a routerLink="/customers" class="btn btn-secondary ms-2">Annuler</a>
          </form>
        </div>
      </div>
    </div>
  `
})
export class CustomerFormComponent implements OnInit {
  customer: Customer = { name: '', email: '' };
  isEdit = false;

  constructor(
    private service: CustomerService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEdit = true;
      this.service.getCustomer(+id).subscribe(data => this.customer = data);
    }
  }

  save(): void {
    if (this.isEdit) {
      this.service.updateCustomer(this.customer.id!, this.customer)
        .subscribe(() => this.router.navigate(['/customers']));
    } else {
      this.service.saveCustomer(this.customer)
        .subscribe(() => this.router.navigate(['/customers']));
    }
  }
}
