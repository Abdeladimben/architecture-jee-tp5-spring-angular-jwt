import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { NgChartsModule } from 'ng2-charts';
import { ChartConfiguration, ChartData, ChartType } from 'chart.js';
import { AccountService } from '../../services/account.service';
import { CustomerService } from '../../services/customer.service';
import { BankAccount } from '../../models/account.model';
import { Customer } from '../../models/customer.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, NgChartsModule],
  template: `
    <div class="container mt-4">
      <h2 class="mb-4">Tableau de Bord</h2>

      <div class="row mb-4">
        <div class="col-md-3">
          <div class="card text-bg-primary">
            <div class="card-body text-center">
              <h5>Clients</h5>
              <p class="display-6">{{ totalCustomers }}</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card text-bg-success">
            <div class="card-body text-center">
              <h5>Comptes</h5>
              <p class="display-6">{{ totalAccounts }}</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card text-bg-warning">
            <div class="card-body text-center">
              <h5>Solde Total</h5>
              <p class="display-6">{{ totalBalance | number:'1.0-0' }} €</p>
            </div>
          </div>
        </div>
        <div class="col-md-3">
          <div class="card text-bg-info">
            <div class="card-body text-center">
              <h5>Opérations</h5>
              <p class="display-6">{{ totalOperations }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="row mb-4">
        <div class="col-md-6">
          <div class="card">
            <div class="card-header bg-dark text-white">
              <h5>Répartition des Comptes (par type)</h5>
            </div>
            <div class="card-body">
              <canvas baseChart
                [data]="pieChartData"
                [type]="pieChartType"
                [options]="pieChartOptions"
                style="max-height: 300px;">
              </canvas>
            </div>
          </div>
        </div>
        <div class="col-md-6">
          <div class="card">
            <div class="card-header bg-dark text-white">
              <h5>Soldes par Client</h5>
            </div>
            <div class="card-body">
              <canvas baseChart
                [data]="barChartData"
                [type]="barChartType"
                [options]="barChartOptions"
                style="max-height: 300px;">
              </canvas>
            </div>
          </div>
        </div>
      </div>

      <div class="card mb-4">
        <div class="card-header bg-dark text-white"><h5>Clients et leurs comptes</h5></div>
        <div class="card-body">
          <table class="table table-striped">
            <thead><tr><th>Nom</th><th>Email</th><th>Nb Comptes</th><th>Solde Total</th></tr></thead>
            <tbody>
              <tr *ngFor="let c of customers">
                <td>{{ c.name }}</td>
                <td>{{ c.email }}</td>
                <td>{{ getCustomerAccountCount(c.id!) }}</td>
                <td>{{ getCustomerTotalBalance(c.id!) | number:'1.2-2' }} €</td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  `
})
export class DashboardComponent implements OnInit {
  customers: Customer[] = [];
  accounts: BankAccount[] = [];
  totalCustomers = 0;
  totalAccounts = 0;
  totalBalance = 0;
  totalOperations = 0;

  pieChartType: ChartType = 'pie';
  pieChartData: ChartData<'pie'> = {
    labels: ['Comptes Courants', 'Comptes Épargne'],
    datasets: [{ data: [0, 0], backgroundColor: ['#0d6efd', '#ffc107'] }]
  };
  pieChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { position: 'bottom' } }
  };

  barChartType: ChartType = 'bar';
  barChartData: ChartData<'bar'> = {
    labels: [],
    datasets: [{
      label: 'Solde Total (€)',
      data: [],
      backgroundColor: '#198754'
    }]
  };
  barChartOptions: ChartConfiguration['options'] = {
    responsive: true,
    plugins: { legend: { display: false } },
    scales: { y: { beginAtZero: true } }
  };

  constructor(private customerService: CustomerService, private accountService: AccountService) {}

  ngOnInit(): void {
    this.customerService.getCustomers().subscribe(data => {
      this.customers = data;
      this.totalCustomers = data.length;
    });
    this.accountService.getAccounts().subscribe(data => {
      this.accounts = data;
      this.totalAccounts = data.length;
      this.totalBalance = data.reduce((s, a) => s + a.balance, 0);
      this.updateCharts();
    });
  }

  updateCharts(): void {
    const ca = this.accounts.filter(a => a.type === 'CA').length;
    const sa = this.accounts.filter(a => a.type === 'SA').length;
    this.pieChartData.datasets[0].data = [ca, sa];

    const customerBalances = this.customers.map(c => ({
      name: c.name,
      total: this.accounts
        .filter(a => a.customerDTO?.id === c.id)
        .reduce((s, a) => s + a.balance, 0)
    }));
    this.barChartData.labels = customerBalances.map(c => c.name);
    this.barChartData.datasets[0].data = customerBalances.map(c => c.total);
  }

  getCustomerAccountCount(customerId: number): number {
    return this.accounts.filter(a => a.customerDTO?.id === customerId).length;
  }

  getCustomerTotalBalance(customerId: number): number {
    return this.accounts
      .filter(a => a.customerDTO?.id === customerId)
      .reduce((s, a) => s + a.balance, 0);
  }
}
