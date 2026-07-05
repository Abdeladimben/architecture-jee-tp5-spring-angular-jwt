import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { AccountService } from '../../services/account.service';
import { CustomerService } from '../../services/customer.service';
import { BankAccount } from '../../models/account.model';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-accounts',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  template: `
    <div class="container mt-4">
      <div class="card mb-4">
        <div class="card-header bg-success text-white">
          <h4>Nouveau Compte</h4>
        </div>
        <div class="card-body">
          <form (ngSubmit)="createAccount()" class="row g-3">
            <div class="col-md-3">
              <select class="form-select" [(ngModel)]="newAccount.type" name="type">
                <option value="CA">Compte Courant</option>
                <option value="SA">Compte Épargne</option>
              </select>
            </div>
            <div class="col-md-3">
              <select class="form-select" [(ngModel)]="newAccount.customerId" name="customerId">
                <option *ngFor="let c of customers" [value]="c.id">{{ c.name }}</option>
              </select>
            </div>
            <div class="col-md-2">
              <input type="number" class="form-control" placeholder="Solde initial"
                     [(ngModel)]="newAccount.balance" name="balance">
            </div>
            <div class="col-md-2">
              <input type="number" class="form-control"
                     [placeholder]="newAccount.type === 'CA' ? 'Découvert' : 'Taux %'"
                     [(ngModel)]="newAccount.extra" name="extra">
            </div>
            <div class="col-md-2">
              <button type="submit" class="btn btn-success w-100">Créer</button>
            </div>
          </form>
        </div>
      </div>

      <div class="card">
        <div class="card-header bg-primary text-white"><h3>Liste des Comptes</h3></div>
        <div class="card-body">
          <table class="table table-striped">
            <thead><tr><th>ID</th><th>Type</th><th>Solde</th><th>Client</th><th>Statut</th><th>Actions</th></tr></thead>
            <tbody>
              <tr *ngFor="let a of accounts">
                <td>{{ a.id | slice:0:8 }}...</td>
                <td><span class="badge" [class.bg-info]="a.type==='CA'" [class.bg-warning]="a.type==='SA'">
                  {{ a.type === 'CA' ? 'Courant' : 'Épargne' }}</span></td>
                <td>{{ a.balance | number:'1.2-2' }} €</td>
                <td>{{ a.customerDTO?.name }}</td>
                <td>{{ a.status }}</td>
                <td>
                  <a [routerLink]="['/accounts', a.id]" class="btn btn-sm btn-info me-1">Détail</a>
                  <button (click)="deleteAccount(a.id)" class="btn btn-sm btn-danger">Supprimer</button>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `
})
export class AccountsComponent implements OnInit {
  accounts: BankAccount[] = [];
  customers: Customer[] = [];
  newAccount = { type: 'CA', customerId: null, balance: 0, extra: 0 };

  constructor(private accountService: AccountService, private customerService: CustomerService) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    this.accountService.getAccounts().subscribe(data => this.accounts = data);
    this.customerService.getCustomers().subscribe(data => this.customers = data);
  }

  createAccount(): void {
    const data: any = {
      initialBalance: this.newAccount.balance,
      customerId: this.newAccount.customerId
    };
    if (this.newAccount.type === 'CA') {
      Object.assign(data, { overDraft: this.newAccount.extra });
      this.accountService.saveCurrentAccount(data).subscribe(() => this.loadData());
    } else {
      Object.assign(data, { interestRate: this.newAccount.extra });
      this.accountService.saveSavingAccount(data).subscribe(() => this.loadData());
    }
  }

  deleteAccount(id: string): void {
    if (confirm('Supprimer ce compte ?')) {
      this.accountService.deleteAccount(id).subscribe(() => this.loadData());
    }
  }
}
