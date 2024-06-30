export class Coupon {
    [x: string]: any;
    id: number;
    name: string;
    description: string;
    companyName: string;

    companyId: number;
    category: string;
    title: string;
    startDate: Date;
    endDate: Date;
    price: number;
    discountPercentage: number;
    amount: number;
    available: boolean;
    image: string;

    constructor(id: number,companyName: string, name: string, description: string, companyId: number, category: string, title: string, startDate: Date, endDate: Date, price: number, discountPercentage: number, amount: number, available: boolean, image: string) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.companyId = companyId;
        this.category = category;
        this.title = title;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.amount = amount;
        this.available = available;
        this.image = image;
        this.companyName = companyName;
    }
}

