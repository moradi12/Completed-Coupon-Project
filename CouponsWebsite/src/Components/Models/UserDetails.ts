export class UserDetails{
    public userName:string;
    public firstName?:string;
    public lastName?:string;
    public email:string;
    public password:string;
    public id?:string;
    public userType:string;
    constructor(userName:string,email:string,password:string,id = undefined,userType:string,firstName = undefined,lastName = undefined ){
       this.userName = userName;
       this.email = email;
       this.password = password;
       this.userType = userType;
       this.id = id;
       this.firstName = firstName;
       this.lastName = lastName;
    }}