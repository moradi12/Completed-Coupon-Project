export class CustomerModel {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    customerID: number;

    constructor(firstName: string, lastName: string, email: string, password: string, customerID: number) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.customerID = customerID;
    }
}
