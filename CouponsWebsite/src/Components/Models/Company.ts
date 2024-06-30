import { Coupon } from './Customer';
// Company.ts
export class Company {
  public id: number;
  public name: string;
  public email: string;
  public password: string;
  public coupons:Coupon[];

  constructor(id: number, name: string, email: string, password: string,coupons:Coupon[]=[]) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.password = password;
    this.coupons=coupons;
  }

  // Getters
  public getId(): number {
    return this.id;
  }

  public getName(): string {
    return this.name;
  }
  public getCoupons(): number {
    return this.id;
  }
 
  public getEmail(): string {
    return this.email;
  }

  public getPassword(): string {
    return this.password;
  }
}
