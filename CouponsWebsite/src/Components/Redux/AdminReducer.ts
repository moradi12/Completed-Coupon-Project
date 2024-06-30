export class AdminReducer {
    public adminReducer: AdminReducer[] = [];
}

export enum AdminActionType {
    addCompany = "ADD_COMPANY",
    deleteCompany = "DELETE_COMPANY",
    updateCompany = "UPDATE_COMPANY",
    getCompanyList = "GET_COMPANY_LIST"
}
