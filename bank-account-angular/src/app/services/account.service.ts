import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { BankAccount } from '../models/account.model';
import { AccountOperation } from '../models/operation.model';

@Injectable({ providedIn: 'root' })
export class AccountService {
  private baseUrl = 'http://localhost:8085/api';

  constructor(private http: HttpClient) {}

  getAccounts(): Observable<BankAccount[]> {
    return this.http.get<BankAccount[]>(`${this.baseUrl}/accounts`);
  }

  getAccount(id: string): Observable<BankAccount> {
    return this.http.get<BankAccount>(`${this.baseUrl}/accounts/${id}`);
  }

  saveCurrentAccount(data: any): Observable<BankAccount> {
    return this.http.post<BankAccount>(`${this.baseUrl}/accounts/current`, data);
  }

  saveSavingAccount(data: any): Observable<BankAccount> {
    return this.http.post<BankAccount>(`${this.baseUrl}/accounts/saving`, data);
  }

  deleteAccount(id: string): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/accounts/${id}`);
  }

  getOperations(accountId: string): Observable<AccountOperation[]> {
    return this.http.get<AccountOperation[]>(`${this.baseUrl}/accounts/${accountId}/operations`);
  }

  debit(data: any): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/operations/debit`, data);
  }

  credit(data: any): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/operations/credit`, data);
  }

  transfer(data: any): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/operations/transfer`, data);
  }
}
