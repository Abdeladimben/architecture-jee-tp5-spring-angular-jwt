import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule, ActivatedRoute } from '@angular/router';
import { AccountService } from '../../services/account.service';
import { BankAccount } from '../../models/account.model';
import { AccountOperation } from '../../models/operation.model';

@Component({
  selector: 'app-account-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  template: `
    <div class="container mt-4">
      <div *ngIf="account" class="card mb-4">
        <div class="card-header bg-info text-white">
          <h3>Compte {{ account.id | slice:0:12 }}...</h3>
        </div>
        <div class="card-body">
          <div class="row">
            <div class="col-md-3"><strong>Solde:</strong> {{ account.balance | number:'1.2-2' }} €</div>
            <div class="col-md-3"><strong>Type:</strong> {{ account.type === 'CA' ? 'Courant' : 'Épargne' }}</div>
            <div class="col-md-3"><strong>Client:</strong> {{ account.customerDTO?.name }}</div>
            <div class="col-md-3"><strong>Statut:</strong> {{ account.status }}</div>
          </div>
        </div>
      </div>

      <div class="row mb-4">
        <div class="col-md-4">
          <div class="card border-success">
            <div class="card-body">
              <h5>Créditer</h5>
              <input type="number" class="form-control mb-2" [(ngModel)]="creditAmount" placeholder="Montant">
              <button class="btn btn-success w-100" (click)="doCredit()">Créditer</button>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-danger">
            <div class="card-body">
              <h5>Débiter</h5>
              <input type="number" class="form-control mb-2" [(ngModel)]="debitAmount" placeholder="Montant">
              <button class="btn btn-danger w-100" (click)="doDebit()">Débiter</button>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card border-primary">
            <div class="card-body">
              <h5>Transférer</h5>
              <input type="text" class="form-control mb-2" [(ngModel)]="transferDest" placeholder="Compte destinataire">
              <input type="number" class="form-control mb-2" [(ngModel)]="transferAmount" placeholder="Montant">
              <button class="btn btn-primary w-100" (click)="doTransfer()">Transférer</button>
            </div>
          </div>
        </div>
      </div>

      <div class="card">
        <div class="card-header bg-dark text-white"><h4>Historique des opérations</h4></div>
        <div class="card-body">
          <table class="table">
            <thead><tr><th>Date</th><th>Type</th><th>Montant</th><th>Description</th><th>Utilisateur</th></tr></thead>
            <tbody>
              <tr *ngFor="let op of operations">
                <td>{{ op.operationDate | date:'short' }}</td>
                <td><span class="badge" [class.bg-success]="op.type==='CREDIT'" [class.bg-danger]="op.type==='DEBIT'">
                  {{ op.type }}</span></td>
                <td>{{ op.amount | number:'1.2-2' }} €</td>
                <td>{{ op.description }}</td>
                <td>{{ op.userId }}</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `
})
export class AccountDetailComponent implements OnInit {
  account: BankAccount | null = null;
  operations: AccountOperation[] = [];
  creditAmount = 0;
  debitAmount = 0;
  transferDest = '';
  transferAmount = 0;
  accountId = '';

  constructor(private route: ActivatedRoute, private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountId = this.route.snapshot.paramMap.get('id') || '';
    this.loadData();
  }

  loadData(): void {
    this.accountService.getAccount(this.accountId).subscribe(data => this.account = data);
    this.accountService.getOperations(this.accountId).subscribe(data => this.operations = data);
  }

  doCredit(): void {
    this.accountService.credit({ accountId: this.accountId, amount: this.creditAmount, description: 'Crédit manuel' })
      .subscribe(() => { this.loadData(); this.creditAmount = 0; });
  }

  doDebit(): void {
    this.accountService.debit({ accountId: this.accountId, amount: this.debitAmount, description: 'Débit manuel' })
      .subscribe(() => { this.loadData(); this.debitAmount = 0; });
  }

  doTransfer(): void {
    this.accountService.transfer({ accountSource: this.accountId, accountDestination: this.transferDest, amount: this.transferAmount })
      .subscribe(() => { this.loadData(); this.transferAmount = 0; this.transferDest = ''; });
  }
}
