//////////// dont fogrget to add client type


export class UserDetailsModel{
    public id:number;
    public name:string;
    public password:string;
    public email:string; 

    constructor(id:number,name:string,password:string,email:string,){
         this.id=id;
         this.name=name;
         this.password=password;
         this.email=email;
    }
}