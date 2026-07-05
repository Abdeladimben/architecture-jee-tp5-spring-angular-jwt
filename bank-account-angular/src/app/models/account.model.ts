export interface BankAccount {
  id: string;
  balance: number;
  createdAt: Date;
  status: string;
  type: string;
  customerDTO: Customer;
  overDraft?: number;
  interestRate?: number;
}

export interface Customer {
  id?: number;
  name: string;
  email: string;
}
