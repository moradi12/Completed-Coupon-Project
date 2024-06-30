export class Customer {
    customerID: number;
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    coupons: Coupon[];

    constructor(customerID: number, firstName: string, lastName: string, email: string, password: string, coupons: Coupon[]) {
        this.customerID = customerID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.coupons = coupons;
    }
}

export class Coupon {
}
